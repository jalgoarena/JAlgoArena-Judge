package com.jalgoarena

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.kafka.annotation.EnableKafka
import java.io.FileDescriptor
import java.security.Permission

@SpringBootApplication
@EnableKafka
open class JAlgoArenaJudgeApp

fun main(args: Array<String>) {
    System.setSecurityManager(SandboxSecurityManger())

    SpringApplication.run(JAlgoArenaJudgeApp::class.java, *args)
}

class SandboxSecurityManger : SecurityManager() {
    override fun checkExit(p: Int) {
        throw SecurityException()
    }

    override fun checkDelete(p0: String?) {
        throw SecurityException()
    }

    override fun checkExec(p0: String?) {
        throw SecurityException()
    }

    override fun checkWrite(p0: FileDescriptor?) {
        throw SecurityException()
    }

    override fun checkWrite(p0: String?) {
        throw SecurityException()
    }

    override fun checkPermission(p0: Permission?) {
        // allow on all others
    }
}
