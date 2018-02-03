package com.jalgoarena.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.codegeneration.JvmCodeGenerator
import com.jalgoarena.compile.InMemoryJavaCompiler
import com.jalgoarena.compile.RubyCompiler
import com.jalgoarena.compile.KotlinCompiler
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.data.SubmissionsRepository
import com.jalgoarena.domain.Submission
import com.jalgoarena.judge.JvmJudgeEngine
import com.jalgoarena.web.JudgeController
import com.jalgoarena.web.ProblemsController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class TestApplicationConfiguration : ApplicationConfiguration() {

    @Bean
    open fun problemsRepository(): ProblemsRepository =
            ProblemsClientForTests()

    @Bean
    open fun judgeEngine(objectMapper: ObjectMapper) = JvmJudgeEngine(objectMapper, listOf(
            InMemoryJavaCompiler(), KotlinCompiler(), RubyCompiler()
    ))

    @Bean
    open fun submissionsRepository() = FakeSubmissionRepository()

    @Bean
    open fun judgeController(
            problemsClient: ProblemsRepository,
            submissionsRepository: SubmissionsRepository,
            judgeEngine: JvmJudgeEngine
    ) = JudgeController(problemsClient, submissionsRepository, judgeEngine)

    @Bean
    open fun problemsController(problemsClient: ProblemsRepository, codeGenerators: List<JvmCodeGenerator>) =
            ProblemsController(problemsClient, codeGenerators)

    class FakeSubmissionRepository : SubmissionsRepository {
        override fun save(submission: Submission, token: String?): Submission? {
            return submission.copy(id = "0-0")
        }

    }
}

