package com.jalgoarena.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.codegeneration.JvmCodeGenerator
import com.jalgoarena.data.DataRepository
import com.jalgoarena.domain.Problem
import com.jalgoarena.judge.JudgeEngine
import com.jalgoarena.web.JudgeController
import com.jalgoarena.web.ProblemsController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class TestApplicationConfiguration : ApplicationConfiguration() {

    @Bean
    open fun problemsRepository(): DataRepository<Problem> =
            ProblemsClientForTests()

    @Bean
    open fun judgeEngine(objectMapper: ObjectMapper) = JudgeEngine(objectMapper)

    @Bean
    open fun judgeController(problemsClient: DataRepository<Problem>, judgeEngine: JudgeEngine) =
            JudgeController(problemsClient, judgeEngine)

    @Bean
    open fun problemsController(problemsClient: DataRepository<Problem>, codeGenerators: List<JvmCodeGenerator>) =
            ProblemsController(problemsClient, codeGenerators)
}
