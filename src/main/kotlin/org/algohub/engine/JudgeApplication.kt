package org.algohub.engine

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
open class JudgeApplication

fun main(args: Array<String>) {
    SpringApplication.run(JudgeApplication::class.java, *args)
}
