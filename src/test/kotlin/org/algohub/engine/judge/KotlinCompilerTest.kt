package org.algohub.engine.judge

import org.algohub.engine.compile.CompileErrorException
import org.algohub.engine.compile.JvmCompiler
import org.algohub.engine.compile.KotlinCompiler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KotlinCompilerTest {

    @Test
    @Throws(Throwable::class)
    fun compileAndRunStaticMethod() {

        val greetingObject = JvmCompiler().compileMethod(
                "Solution", "greeting", HELLO_WORLD_SOURCE_CODE, KotlinCompiler()
        )

        val obj = greetingObject.first
        val greeting = greetingObject.second

        val result = greeting.invoke(obj, "Julia")
        assertThat(result).isEqualTo("Hello Julia")
    }

    @Test(expected = NoSuchMethodError::class)
    @Throws(Exception::class)
    fun throwsNoSuchMethodExceptionWhenInvokingNonExistingMethod() {
        JvmCompiler().compileMethod(
                "Solution", "dummy", SOURCE_CODE_CORRECT, KotlinCompiler()
        )
    }

    @Test(expected = IllegalStateException::class)
    @Throws(Exception::class)
    fun throwsIllegalStateExceptionWhenTryingToInvokeClassWithPrivateConstructor() {
        val sourceCode = """class Solution private constructor() {
  fun dummy() {}
}
"""

        JvmCompiler().compileMethod(
                "Solution", "dummy", sourceCode, KotlinCompiler()
        )
    }

    @Test(expected = CompileErrorException::class)
    @Throws(Exception::class)
    fun throwsCompileErrorExceptionWhenSourceCodeDoesNotCompile() {

        JvmCompiler().compileMethod(
                "Solution", "fib", SOURCE_CODE_WITH_ERROR, KotlinCompiler()
        )
    }

    companion object {

        private val HELLO_WORLD_SOURCE_CODE = """class Solution {
    fun greeting(name: String): String {
        return "Hello " + name
    }
}
"""

        private val SOURCE_CODE_CORRECT = """import java.util.*
import org.algohub.engine.type.*

class Solution {
    /**
     * @param n id of fibonacci term to be returned
     * @return  N'th term of Fibonacci sequence
     */
    fun fib(n: Int): Long {
        if (n < 2) return n.toLong()
        return fib(n - 1) + fib(n - 2)
    }
}
"""

        private val SOURCE_CODE_WITH_ERROR = """import java.util.*
import org.algohub.engine.type.*

class Solution {
    /**
     * @param n id of fibonacci term to be returned
     * @return  N'th term of Fibonacci sequence
     */
    fun fib(n: Int): Long {
        if (n < 2) return n
        return fib(n - 1) + fib(n - 2)
    }
}
"""
    }
}
