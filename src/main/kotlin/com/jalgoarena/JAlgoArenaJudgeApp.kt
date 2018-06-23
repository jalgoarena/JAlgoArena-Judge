package com.jalgoarena

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableKafka
open class JAlgoArenaJudgeApp

fun main(args: Array<String>) {
    SpringApplication.run(JAlgoArenaJudgeApp::class.java, *args)
}
