package com.jalgoarena

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
open class JudgeApplication

fun main(args: Array<String>) {
    SpringApplication.run(JudgeApplication::class.java, *args)
}
