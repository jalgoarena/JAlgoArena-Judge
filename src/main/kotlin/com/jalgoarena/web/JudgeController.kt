package com.jalgoarena.web

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.judge.JudgeEngine
import com.jalgoarena.judge.JudgeResult
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@CrossOrigin
@RestController
class JudgeController(
        @Inject val problemsRepository: ProblemsRepository,
        @Inject var judgeEngine: JudgeEngine) {

    @PostMapping("/problems/{id}/submit", produces = arrayOf("application/json"))
    fun judge(@PathVariable id: String, @RequestBody sourceCode: String): JudgeResult {
        val problem = problemsRepository.find(id) ?:
                return JudgeResult.RuntimeError("Wrong problem id: $id")

        return judgeEngine.judge(problem, sourceCode)
    }
}
