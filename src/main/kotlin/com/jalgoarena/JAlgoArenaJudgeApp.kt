package com.jalgoarena

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
open class JAlgoArenaJudgeApp

fun main(args: Array<String>) {
    SpringApplication.run(JAlgoArenaJudgeApp::class.java, *args)
}
