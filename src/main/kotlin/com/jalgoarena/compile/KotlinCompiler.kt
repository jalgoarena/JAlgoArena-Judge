package com.jalgoarena.compile

import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.util.*


class KotlinCompiler : JvmCompiler {

    private val compiler = K2JVMCompiler()

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
            "-classpath", listOf(
                File("build/classes/main").absolutePath,
                File("build/resources/main").absolutePath,
                File("lib/kotlin-runtime-1.0.6.jar").absolutePath,
                File("lib/kotlin-stdlib-1.0.6.jar").absolutePath
            ).joinToString(File.pathSeparator)

    )

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
