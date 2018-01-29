package com.jalgoarena.compile
import org.jetbrains.kotlin.cli.common.ExitCode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.util.*
import org.jruby.javasupport.JavaEmbedUtils
import org.jruby.Ruby


class RubyCompiler : JvmCompiler {
    open class InMemoryJRubyCompiler : InMemoryJavaCompiler() {
        override fun javacOptions(): List<String> {
            return listOf(
                    "-nowarn",
                    "-classpath",
                    listOf(
                            File("lib/jruby-complete-9.1.14.0.jar"),
                            File("build/classes/main").absolutePath
                    ).joinToString(File.pathSeparator)
            )
        }
    }

    override fun programmingLanguage() = "ruby"

    override fun run(className: String, source: String): MutableMap<String, ByteArray?> {

        val tmpDir = createTmpDir()
        val origErr = System.err

        try {
            val sourceFile = writeSourceFile(tmpDir, "$className.rb", source)
            val out = File(tmpDir, "out")
            out.mkdirs()

            val errMessageBytes = ByteArrayOutputStream()
            System.setErr(PrintStream(errMessageBytes))

            when (compileAndReturnExitCode(out, sourceFile)) {
                ExitCode.OK -> return runWithJavaCompiler(className, out)
                else -> throw CompileErrorException(errMessageBytes.toString("utf-8"))
            }
        } finally {
            tmpDir.deleteRecursively()
            System.setErr(origErr)
        }
    }

    private fun runWithJavaCompiler(className: String, out: File): MutableMap<String, ByteArray?> {
        val javaSourceFile = out.listFiles()
                .filter { it.absolutePath.endsWith(".java") }
                .first()

        val source = File(out, javaSourceFile.name).readText()

        val compiler = InMemoryJRubyCompiler()
        return compiler.run(className, source)
    }

    private fun compileAndReturnExitCode(out: File, sourceFile: File): ExitCode {
        val template = "require 'jruby/jrubyc'\n" +
                "status = JRuby::Compiler::compile_argv(['--target', '%s', '--dir', '%s', '--java', '%s'])\n" +
                "raise StandardError if status != 0"

        val compilationCode = String.format(
                template,
                sanitizePath(out),
                sanitizePath(sourceFile.parentFile),
                sanitizePath(sourceFile)
        )

        try {
            val runtime = Ruby.getGlobalRuntime()
            val evaler = JavaEmbedUtils.newRuntimeAdapter()
            evaler.eval(runtime, compilationCode)
        } catch (e: Exception) {
            return ExitCode.COMPILATION_ERROR
        }

        return ExitCode.OK
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

    private fun sanitizePath(file: File): String {
        return file.absolutePath.replace("\\", "/")
    }
}
