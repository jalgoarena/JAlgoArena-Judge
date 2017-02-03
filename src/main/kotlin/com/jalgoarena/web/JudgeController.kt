package com.jalgoarena.web

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.data.SubmissionsRepository
import com.jalgoarena.domain.JudgeRequest
import com.jalgoarena.domain.JudgeResult
import com.jalgoarena.domain.JudgeResult.RuntimeError
import com.jalgoarena.domain.StatusCode
import com.jalgoarena.domain.Submission
import com.jalgoarena.judge.JudgeEngine
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@RestController
class JudgeController(
        @Inject private val problemsRepository: ProblemsRepository,
        @Inject private val submissionsRepository: SubmissionsRepository,
        @Inject private val judgeEngine: JudgeEngine) {

    @PostMapping("/problems/{id}/submit", produces = arrayOf("application/json"))
    fun judge(
            @PathVariable id: String,
            @RequestBody judgeRequest: JudgeRequest,
            @RequestHeader("X-Authorization", required = false) token: String? = null
    ): JudgeResult {
        val judgeResult = judgeEngine.judge(problemsRepository.find(id), judgeRequest)

        return when {
            judgeResult.statusCode == StatusCode.ACCEPTED.toString() ->
                submitAndReturnResult(id, judgeRequest, judgeResult, token)
            else ->
                judgeResult
        }
    }

    private fun submitAndReturnResult(id: String, judgeRequest: JudgeRequest, judgeResult: JudgeResult, token: String?): JudgeResult {
        val submission = submissionsRepository.save(Submission(
                problemId = id,
                userId = judgeRequest.userId,
                statusCode = judgeResult.statusCode,
                sourceCode = judgeRequest.sourceCode,
                elapsedTime = judgeResult.elapsedTime,
                language = judgeRequest.language
        ), token)

        if (submission != null && submission.id != null) {
            return judgeResult
        } else {
            return RuntimeError("Couldn't save submission, please try again")
        }
    }
}
