package org.algohub.engine.compile

import org.slf4j.LoggerFactory
import java.net.URL
import java.net.URLClassLoader

class JvmCompiler {

    @Throws(ClassNotFoundException::class, CompileErrorException::class)
    fun compileMethod(qualifiedClassName: String, methodName: String, source: String, compiler: Compiler): Array<Any> {

        val classBytes = compiler.run(qualifiedClassName, source)
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
        private val LOG = LoggerFactory.getLogger(JvmCompiler::class.java)
    }
}