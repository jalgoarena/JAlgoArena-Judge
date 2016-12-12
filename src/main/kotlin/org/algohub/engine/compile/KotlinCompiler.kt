package org.algohub.engine.compile

import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.File
import java.util.*

class KotlinCompiler : Compiler {
    override fun run(className: String, source: String): MutableMap<String, ByteArray?>? {

        val tmpDir = createTmpDir()

        try {
            val sourceFile = writeSourceFile(tmpDir, "$className.kt", source)
            val out = File(tmpDir, "out")

            val exitCode = compiler.exec(
                    System.out,
                    sourceFile.absolutePath,
                    "-include-runtime",
                    "-d", out.absolutePath,
                    "-kotlin-home", File("kotlinHome").absolutePath,
                    "-classpath", File("build/classes/main").absolutePath
            )

            println("ExitCode: $exitCode")

            val classBytes: MutableMap<String, ByteArray?>? = HashMap()

            out.listFiles()
                    .filter { it.absolutePath.endsWith(".class") }
                    .forEach {
                        val byteCode = File(out, it.name).readBytes()
                        classBytes!!.put(it.nameWithoutExtension, byteCode)
                    }

            return classBytes
        } finally {
            tmpDir.deleteRecursively()
        }
    }

    private fun createTmpDir(): File {
        val randomUUID = UUID.randomUUID()

        val tmpDir = File("tmp", "$randomUUID")
        tmpDir.mkdirs()
        return tmpDir
    }

    private fun writeSourceFile(tmpDir: File, fileName: String, sourceCode: String): File {
        val sourceFile = File(tmpDir, fileName)
        sourceFile.writeText(sourceCode)
        return sourceFile
    }

    companion object {
        private val compiler = K2JVMCompiler()
    }
}