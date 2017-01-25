package com.jalgoarena

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.codegeneration.JavaCodeGenerator
import com.jalgoarena.codegeneration.KotlinCodeGenerator
import com.jalgoarena.type.ListNode
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.cache.CacheManager
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Configuration
@EnableCaching
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
    open fun kotlinCodeGenerator() = KotlinCodeGenerator()

    @Bean
    open fun javaCodeGenerator() = JavaCodeGenerator()

    @Component
    open class CacheManagerCheck(private val cacheManager: CacheManager) : CommandLineRunner {

        private val LOG = LoggerFactory.getLogger(this.javaClass)

        override fun run(vararg strings: String) {
            LOG.info("""

=========================================================
Using cache manager: ${this.cacheManager.javaClass.name}
=========================================================

"""
            )
        }
    }
}
