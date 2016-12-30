package com.jalgoarena.web

import com.jalgoarena.codegeneration.JavaCodeGenerator
import com.jalgoarena.codegeneration.KotlinCodeGenerator
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.judge.Function
import com.jalgoarena.judge.Problem
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@CrossOrigin
@RestController
class ProblemsController(
        @Inject val problemsRepository: ProblemsRepository,
        @Inject val kotlinCodeGenerator: KotlinCodeGenerator,
        @Inject val javaCodeGenerator: JavaCodeGenerator) {

    private val LOG = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems(): List<Problem> {
        return problemsRepository.findAll().asList()
                .map { x -> x.problemWithoutFunctionAndTestCases(
                        sourceCodeOf(x.function!!), kotlinSourceCodeOf(x.function)
                )}
    }

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String): Problem {

        val problem = problemsRepository.find(id) ?:
                throw IllegalArgumentException("Invalid problem id: " + id)

        return problem.problemWithoutFunctionAndTestCases(
                sourceCodeOf(problem.function!!), kotlinSourceCodeOf(problem.function)
        )
    }

    private fun sourceCodeOf(function: Function): String {
        try {
            return javaCodeGenerator.generateEmptyFunction(function)
        } catch (e: ClassNotFoundException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException("Illegal type: " + e.message)
        }
    }

    private fun kotlinSourceCodeOf(function: Function): String {
        try {
            return kotlinCodeGenerator.generateEmptyFunction(function)
        } catch (e: ClassNotFoundException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException("Illegal type: " + e.message)
        }
    }
}
