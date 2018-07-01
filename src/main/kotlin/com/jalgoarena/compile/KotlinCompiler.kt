package com.jalgoarena.compile

import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.util.*


class KotlinCompiler : JvmCompiler {

    private val compiler = K2JVMCompiler()

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun programmingLanguage() = "kotlin"

    override fun run(className: String, source: String): MutableMap<String, ByteArray?> {

        val tmpDir = createTmpDir()
        val origErr = System.err

        try {
            val sourceFile = writeSourceFile(tmpDir, "$className.kt", source)
            val out = File(tmpDir, "out")

            val errMessageBytes = ByteArrayOutputStream()
            System.setErr(PrintStream(errMessageBytes))

            when (compileAndReturnExitCode(out, sourceFile)) {
                ExitCode.OK -> return readClassBytes(out)
                else -> throw CompileErrorException(errMessageBytes.toString("utf-8"))
            }
        } finally {
            tmpDir.deleteRecursively()
            System.setErr(origErr)
        }
    }

    private fun readClassBytes(out: File): MutableMap<String, ByteArray?> {
        val classBytes: MutableMap<String, ByteArray?> = HashMap()

        out.listFiles()
                .filter { it.absolutePath.endsWith(".class") }
                .forEach {
                    val byteCode = File(out, it.name).readBytes()
                    classBytes.put(it.nameWithoutExtension, byteCode)
                }

        return classBytes
    }

    private fun compileAndReturnExitCode(out: File, sourceFile: File): ExitCode = compiler.exec(
            System.err,
            sourceFile.absolutePath,
            "-d", out.absolutePath,
            "-no-stdlib",
            "-classpath", buildClassPath()
    )

    private fun buildClassPath(): String {
        val classpath = mutableListOf<String>()

        addToClassPath(classpath, "build/classes/kotlin/main")
        addToClassPath(classpath, "lib")
        addToClassPath(classpath, "lib/kotlin-runtime-1.2.50.jar")
        addToClassPath(classpath, "lib/kotlin-stdlib-1.2.50.jar")
        addToClassPath(classpath, "lib/kotlin-compiler-1.2.50.jar")

        val result = classpath.joinToString(File.pathSeparator)
        logger.info("Classpath: $result")
        return result
    }

    private fun addToClassPath(classpath: MutableList<String>, path: String) {
        val dockerPath = "/app/$path"

        when {
            File(path).exists() -> classpath.add(File(path).absolutePath)
            File(dockerPath).exists() -> classpath.add(File(dockerPath).absolutePath)
            else -> logger.error("Could not find $path nor $dockerPath!!")
        }
    }

    private fun createTmpDir(): File {
        val tmpDir = File("tmp", "${UUID.randomUUID()}")
        tmpDir.mkdirs()
        return tmpDir
    }

    private fun writeSourceFile(tmpDir: File, fileName: String, sourceCode: String): File {
        val sourceFile = File(tmpDir, fileName)
        sourceFile.writeText(sourceCode)
        return sourceFile
    }
}
