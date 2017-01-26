package com.jalgoarena.web

import com.jalgoarena.codegeneration.JavaCodeGenerator
import com.jalgoarena.codegeneration.KotlinCodeGenerator
import com.jalgoarena.data.DataRepository
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Problem
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@CrossOrigin
@RestController
class ProblemsController(
        @Inject private val problemsClient: DataRepository<Problem>,
        @Inject private val kotlinCodeGenerator: KotlinCodeGenerator,
        @Inject private val javaCodeGenerator: JavaCodeGenerator
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
                skeletonCode = mapOf(
                        Pair("java", javaSourceCode(problem.function!!)),
                        Pair("kotlin", kotlinSourceCodeOf(problem.function))
                )
        )
    }

    private fun javaSourceCode(function: Function) =
            javaCodeGenerator.generateEmptyFunction(function)

    private fun kotlinSourceCodeOf(function: Function) =
            kotlinCodeGenerator.generateEmptyFunction(function)
}
