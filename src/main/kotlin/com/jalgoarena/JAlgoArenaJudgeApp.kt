package com.jalgoarena

import com.jalgoarena.security.SandboxSecurityManger
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
open class JAlgoArenaJudgeApp

fun main(args: Array<String>) {
    System.setSecurityManager(SandboxSecurityManger())

    SpringApplication.run(JAlgoArenaJudgeApp::class.java, *args)
}

