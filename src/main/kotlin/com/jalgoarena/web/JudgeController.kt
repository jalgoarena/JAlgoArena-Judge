package com.jalgoarena.web

import com.jalgoarena.data.DataRepository
import com.jalgoarena.domain.Problem
import com.jalgoarena.judge.JudgeEngine
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@CrossOrigin
@RestController
class JudgeController(
        @Inject private val problemsClient: DataRepository<Problem>,
        @Inject private val judgeEngine: JudgeEngine) {

    @PostMapping("/problems/{id}/submit", produces = arrayOf("application/json"))
    fun judge(@PathVariable id: String, @RequestBody sourceCode: String) =
            judgeEngine.judge(problemsClient.find(id), sourceCode)
}
