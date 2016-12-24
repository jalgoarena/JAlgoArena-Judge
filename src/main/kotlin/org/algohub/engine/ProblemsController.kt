package org.algohub.engine

import org.algohub.engine.codegeneration.JavaCodeGenerator
import org.algohub.engine.codegeneration.KotlinCodeGenerator
import org.algohub.engine.data.ProblemsRepository
import org.algohub.engine.judge.Function
import org.algohub.engine.judge.Problem
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

@CrossOrigin
@RestController
class ProblemsController {

    val problemsRepository = ProblemsRepository()
    private val LOG = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/problems", produces = arrayOf("application/json"))
    fun problems(): List<Problem> {
        return Arrays.stream(problemsRepository.findAll())
                .map { x -> x.problemWithoutFunctionAndTestCases(
                        sourceCodeOf(x.function!!), kotlinSourceCodeOf(x.function)
                )}
                .collect(Collectors.toList<Problem>())
    }

    @GetMapping("/problems/{id}", produces = arrayOf("application/json"))
    fun problem(@PathVariable id: String): Problem {

        val problem = problemsRepository.find(id) ?: throw IllegalArgumentException("Invalid problem id: " + id)

        return problem.problemWithoutFunctionAndTestCases(
                sourceCodeOf(problem.function!!), kotlinSourceCodeOf(problem.function)
        )
    }

    private fun sourceCodeOf(function: Function): String {
        try {
            return JavaCodeGenerator.generateEmptyFunction(function)
        } catch (e: ClassNotFoundException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException("Illegal type: " + e.message)
        }
    }

    private fun kotlinSourceCodeOf(function: Function): String {
        try {
            return KotlinCodeGenerator.generateEmptyFunction(function)
        } catch (e: ClassNotFoundException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException("Illegal type: " + e.message)
        }
    }
}
