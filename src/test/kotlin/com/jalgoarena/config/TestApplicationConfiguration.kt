package com.jalgoarena.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.codegeneration.JvmCodeGenerator
import com.jalgoarena.compile.InMemoryJavaCompiler
import com.jalgoarena.compile.KotlinCompiler
import com.jalgoarena.compile.RubyCompiler
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.judge.JvmJudgeEngine
import com.jalgoarena.web.SubmissionsProcessor
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
    open fun submissionsListener(
            problemsClient: ProblemsRepository,
            judgeEngine: JvmJudgeEngine
    ) = SubmissionsProcessor(problemsClient, judgeEngine)

    @Bean
    open fun problemsController(problemsClient: ProblemsRepository, codeGenerators: List<JvmCodeGenerator>) =
            ProblemsController(problemsClient, codeGenerators)

}

