package com.jalgoarena

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.codegeneration.JavaCodeGenerator
import com.jalgoarena.codegeneration.JvmCodeGenerator
import com.jalgoarena.codegeneration.KotlinCodeGenerator
import com.jalgoarena.compile.JvmCompiler
import com.jalgoarena.compile.KotlinCompiler
import com.jalgoarena.compile.InMemoryJavaCompiler
import com.jalgoarena.type.ListNode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class ApplicationConfiguration {

    @Bean
    open fun objectMapper(): ObjectMapper {
        val objectMapper = jacksonObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        val customModule = SimpleModule()
        customModule.addDeserializer(ListNode::class.java, ListNode.Deserializer())
        objectMapper.registerModule(customModule)
        return objectMapper
    }

    @Bean
    open fun restTemplate() = RestTemplate()

    @Bean
    open fun codeGenerators(): List<JvmCodeGenerator> = listOf(
            JavaCodeGenerator(),
            KotlinCodeGenerator()
    )

    @Bean
    open fun codeCompilers(): List<JvmCompiler> = listOf(
            InMemoryJavaCompiler(),
            KotlinCompiler()
    )
}
