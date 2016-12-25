package com.jalgoarena

import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.judge.JudgeEngine
import com.jalgoarena.judge.JudgeResult
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class JudgeController {

    val problemsRepository = ProblemsRepository()
    val judgeEngine = JudgeEngine()

    @PostMapping("/problems/{id}/submit", produces = arrayOf("application/json"))
    fun judge(@PathVariable id: String, @RequestBody sourceCode: String): JudgeResult {
        val problem = problemsRepository.find(id) ?:
                return JudgeResult.runtimeError("Wrong problem id: " + id)

        return judgeEngine.judge(problem, sourceCode)
    }
}
