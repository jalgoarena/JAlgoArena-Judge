package com.jalgoarena

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
open class JudgeApplication

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

fun main(args: Array<String>) {
    SpringApplication.run(JudgeApplication::class.java, *args)
}
