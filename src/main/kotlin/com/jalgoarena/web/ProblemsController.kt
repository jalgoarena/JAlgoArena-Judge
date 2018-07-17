package com.jalgoarena.web

import com.jalgoarena.codegeneration.JvmCodeGenerator
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProblemsController(
        @Autowired private val problemsRepository: ProblemsRepository,
        @Autowired private val codeGenerator: JvmCodeGenerator
) {

    @GetMapping("/problems", produces = ["application/json"])
    fun problems() = problemsRepository.findAll()
            .map { enhancedProblem(it) }

    @GetMapping("/rawProblems", produces = ["application/json"])
    fun rawProblems() = problemsRepository.findAll()
            .map { enhancedRawProblem(it) }

    @GetMapping("/problems/{id}", produces = ["application/json"])
    fun problem(@PathVariable id: String) =
            enhancedProblem(problemsRepository.find(id)!!)

    private fun enhancedProblem(problem: Problem): Problem {
        return problem.copy(
                func = null,
                testCases = null,
                skeletonCode = codeGenerator.generateEmptyFunction(problem.func!!)
        )
    }

    private fun enhancedRawProblem(problem: Problem): Problem {
        return problem.copy(
                skeletonCode = codeGenerator.generateEmptyFunction(problem.func!!)
        )
    }
}
