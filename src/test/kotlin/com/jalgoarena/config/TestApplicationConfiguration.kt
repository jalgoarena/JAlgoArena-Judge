package com.jalgoarena.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.codegeneration.JvmCodeGenerator
import com.jalgoarena.compile.InMemoryJavaCompiler
import com.jalgoarena.compile.KotlinCompiler
import com.jalgoarena.compile.RubyCompiler
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.SubmissionResult
import com.jalgoarena.judge.JvmJudgeEngine
import com.jalgoarena.web.SubmissionsListener
import com.jalgoarena.web.ProblemsController
import com.nhaarman.mockito_kotlin.whenever
import org.apache.kafka.clients.producer.Producer
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFuture

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
    ) = SubmissionsListener(problemsClient, judgeEngine)

    @Bean
    open fun problemsController(problemsClient: ProblemsRepository, codeGenerators: List<JvmCodeGenerator>) =
            ProblemsController(problemsClient, codeGenerators)

    @Bean
    open fun kafkaTemplate(): KafkaTemplate<*, *> {
        val producerFactory = mock(ProducerFactory::class.java)
        val producer = mock(Producer::class.java)
        whenever(producerFactory.createProducer()).thenReturn(producer)
        return KafkaTemplate(producerFactory)
    }

}

