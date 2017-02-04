package com.jalgoarena.web

import com.jalgoarena.codegeneration.JvmCodeGenerator
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Problem
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
class ProblemsController(
        @Inject private val problemsRepository: ProblemsRepository,
        @Inject private val codeGenerators: List<JvmCodeGenerator>
) {

    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems() = problemsRepository.findAll()
            .map { enhancedProblem(it) }

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String) =
            enhancedProblem(problemsRepository.find(id))

    private fun enhancedProblem(problem: Problem): Problem {
        return problem.copy(
                func = null,
                testCases = null,
                skeletonCode = generateSkeletonCodes(problem.func!!)
        )
    }

    private fun generateSkeletonCodes(function: Function) = codeGenerators.map {
        it.programmingLanguage() to it.generateEmptyFunction(function)
    }.toMap()
}
