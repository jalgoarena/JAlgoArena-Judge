package org.algohub.engine

import org.algohub.engine.data.ProblemsRepository
import org.algohub.engine.judge.JudgeEngine
import org.algohub.engine.judge.JudgeResult
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class JudgeController {

    val problemsRepository = ProblemsRepository()

    @RequestMapping(path = arrayOf("/problems/{id}/submit"), method = arrayOf(RequestMethod.POST), produces = arrayOf("application/json"))
    fun judge(@PathVariable id: String, @RequestBody sourceCode: String): JudgeResult {
        val problem = problemsRepository.find(id) ?:
                return JudgeResult.runtimeError("Wrong problem id: " + id)

        return JudgeEngine.judge(problem, sourceCode)
    }
}
