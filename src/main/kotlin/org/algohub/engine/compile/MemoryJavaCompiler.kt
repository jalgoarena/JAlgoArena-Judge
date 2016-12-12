package org.algohub.engine.compile

import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.CharBuffer
import java.util.*
import javax.tools.*

/**
 * Simple interface to Java compiler using JSR 199 Compiler API.
 */
class MemoryJavaCompiler : Compiler {

    private fun standardFileManager(): StandardJavaFileManager {
        return javaCompiler.getStandardFileManager(null, null, null)
    }

    /**
     * compile given String source and return bytecodes as a Map.
     * @param className source className to be used for error messages etc.
     * *
     * @param source   Java source as String
     */
    @Throws(CompileErrorException::class)
    override fun run(className: String, source: String): MutableMap<String, ByteArray?>? {

        val javaFileName = "$className.java"

        val fileManager = MemoryJavaFileManager(standardFileManager())
        val diagnostics = DiagnosticCollector<JavaFileObject>()

        val compilationTask = compilationTask(
                javaFileName, source, fileManager, diagnostics
        )

        if (!compilationTask.call()) {
            return getCompilationError(diagnostics)
        }

        val classBytes = fileManager.classBytes
        try {
            fileManager.close()
        } catch (exp: IOException) {
            LOG.error("Cannot close in memory file", exp)
        }

        return classBytes
    }

    private fun compilationTask(
            fileName: String,
            source: String,
            fileManager: MemoryJavaFileManager,
            diagnostics: DiagnosticCollector<JavaFileObject>): JavaCompiler.CompilationTask {

        return javaCompiler.getTask(
                null,
                fileManager,
                diagnostics,
                javacOptions(),
                null,
                prepareTheCompilationUnit(fileName, source)
        )
    }

    private fun prepareTheCompilationUnit(fileName: String, source: String): List<JavaFileObject> {
        return listOf(makeStringSource(fileName, source))
    }

    private fun javacOptions(): List<String> {
        return Arrays.asList(
                "-Xlint:all",
                "-deprecation",
                "-classpath", File("build/classes/main").absolutePath
        )
    }

    @Throws(CompileErrorException::class)
    private fun getCompilationError(
            diagnostics: DiagnosticCollector<JavaFileObject>): MutableMap<String, ByteArray?> {

        val errorMsg = StringBuilder()
        val diagnostics1 = diagnostics.diagnostics
        for (aDiagnostics1 in diagnostics1) {
            errorMsg.append(aDiagnostics1)
            errorMsg.append(System.lineSeparator())
        }
        throw CompileErrorException(errorMsg.toString())
    }

    /**
     * A file object used to represent Java source coming from a string.
     */
    private class SourceCodeStringInputBuffer internal constructor(fileName: String, internal val code: String)
        : SimpleJavaFileObject(MemoryJavaFileManager.toUri(fileName), JavaFileObject.Kind.SOURCE) {

        override fun getCharContent(ignoreEncodingErrors: Boolean): CharBuffer {
            return CharBuffer.wrap(code)
        }
    }



    companion object {

        private val LOG = LoggerFactory.getLogger(MemoryJavaCompiler::class.java)
        private val javaCompiler: JavaCompiler

        init {
            javaCompiler = ToolProvider.getSystemJavaCompiler()
            Preconditions.checkNotNull(
                    javaCompiler, "Could not get Java compiler. Please, ensure that JDK is used instead of JRE."
            )
        }

        private fun makeStringSource(fileName: String, code: String): JavaFileObject {
            return SourceCodeStringInputBuffer(fileName, code)
        }
    }
}
