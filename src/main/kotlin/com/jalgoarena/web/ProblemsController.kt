package com.jalgoarena.web

import com.jalgoarena.codegeneration.JvmCodeGenerator
import com.jalgoarena.data.DataRepository
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Problem
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
class ProblemsController(
        @Inject private val problemsClient: DataRepository<Problem>,
        @Inject private val codeGenerators: List<JvmCodeGenerator>
) {

    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems() = problemsClient.findAll().asList()
            .map { enhancedProblem(it) }

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String) =
            enhancedProblem(problemsClient.find(id))

    private fun enhancedProblem(problem: Problem): Problem {
        return problem.copy(
                function = null,
                testCases = null,
                skeletonCode = generateSkeletonCodes(problem.function!!)
        )
    }

    private fun generateSkeletonCodes(function: Function) = codeGenerators.map {
        it.programmingLanguage() to it.generateEmptyFunction(function)
    }.toMap()
}
