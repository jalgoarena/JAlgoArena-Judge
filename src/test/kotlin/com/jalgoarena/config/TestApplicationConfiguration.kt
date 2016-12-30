package com.jalgoarena.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.ProblemsConfiguration
import com.jalgoarena.codegeneration.JavaCodeGenerator
import com.jalgoarena.codegeneration.KotlinCodeGenerator
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.judge.JudgeEngine
import com.jalgoarena.web.JudgeController
import com.jalgoarena.web.ProblemsController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class TestApplicationConfiguration : ApplicationConfiguration() {

    @Bean
    open fun problemsConfiguration() = ProblemsConfiguration().apply {
        gatewayUrl = "https://jalgoarena-api.herokuapp.com"
    }

    @Bean
    open fun problemsRepository(objectMapper: ObjectMapper, problemsConfiguration: ProblemsConfiguration) =
            ProblemsRepository(objectMapper, problemsConfiguration)

    @Bean
    open fun judgeEngine(objectMapper: ObjectMapper) = JudgeEngine(objectMapper)

    @Bean
    open fun judgeController(problemsRepository: ProblemsRepository, judgeEngine: JudgeEngine) =
            JudgeController(problemsRepository, judgeEngine)

    @Bean
    open fun problemsController(
            problemsRepository: ProblemsRepository,
            kotlinCodeGenerator: KotlinCodeGenerator,
            javaCodeGenerator: JavaCodeGenerator): ProblemsController {

        return ProblemsController(
                problemsRepository,
                kotlinCodeGenerator,
                javaCodeGenerator)
    }
}
