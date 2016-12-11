package org.algohub.engine.judge

import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.nio.CharBuffer
import java.util.*
import javax.tools.*

/**
 * Simple interface to Java compiler using JSR 199 Compiler API.
 */
internal class MemoryJavaCompiler {

    private fun standardFileManager(): StandardJavaFileManager {
        return javaCompiler.getStandardFileManager(null, null, null)
    }

    /**
     * Compile a single normal method.
     * @return [Object, Method], a class instance and the method
     */
    @Throws(ClassNotFoundException::class, CompileErrorException::class)
    fun compileMethod(qualifiedClassName: String, methodName: String,
                      source: String): Array<Any> {

        val classBytes = compile("$qualifiedClassName.java", source)
        val clazz = Class.forName(
                qualifiedClassName, true, MemoryClassLoader(classBytes)
        )
        val methods = clazz.declaredMethods

        methods.filter { it.name == methodName }
                .forEach {
                    try {
                        return arrayOf(clazz.newInstance(), it)
                    } catch (e: InstantiationException) {
                        LOG.error("Error during creating of new class instance", e)
                        throw IllegalStateException(e.message)
                    } catch (e: IllegalAccessException) {
                        LOG.error("Error during creating of new class instance", e)
                        throw IllegalStateException(e.message)
                    }
                }

        throw NoSuchMethodError(methodName)
    }

    /**
     * compile given String source and return bytecodes as a Map.

     * @param fileName source fileName to be used for error messages etc.
     * *
     * @param source   Java source as String
     */
    @Throws(CompileErrorException::class)
    private fun compile(fileName: String, source: String): MutableMap<String, ByteArray?>? {

        val fileManager = MemoryJavaFileManager(standardFileManager())
        val diagnostics = DiagnosticCollector<JavaFileObject>()

        val compilationTask = compilationTask(
                fileName, source, fileManager, diagnostics
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
        return Arrays.asList("-Xlint:all", "-deprecation")
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

    /**
     * ClassLoader that loads .class bytes from memory.
     */
    private class MemoryClassLoader internal constructor(private val classNameToBytecode: MutableMap<String, ByteArray?>?)
        : URLClassLoader(arrayOfNulls<URL>(0), ClassLoader.getSystemClassLoader()) {

        @Throws(ClassNotFoundException::class)
        override fun findClass(className: String): Class<*> {
            val buf = classNameToBytecode!![className]
            if (buf == null) {
                return super.findClass(className)
            } else {
                clearByteMap(className)
                return defineClass(className, buf, 0, buf.size)
            }
        }

        private fun clearByteMap(className: String) {
            classNameToBytecode!!.put(className, null)
        }
    }

    companion object {

        private val LOG = LoggerFactory.getLogger(MemoryJavaCompiler::class.java)
        private val javaCompiler: javax.tools.JavaCompiler

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
