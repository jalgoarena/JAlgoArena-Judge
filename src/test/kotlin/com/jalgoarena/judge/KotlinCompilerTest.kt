package com.jalgoarena.judge

import com.jalgoarena.compile.CompileErrorException
import com.jalgoarena.compile.KotlinCompiler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KotlinCompilerTest {

    @Test
    fun compileAndRunStaticMethod() {

        val (instance, method) = KotlinCompiler().compileMethod(
                "Solution", "greeting", 1, HELLO_WORLD_SOURCE_CODE
        )

        val result = method.invoke(instance, "Julia")
        assertThat(result).isEqualTo("Hello Julia")
    }

    @Test(expected = NoSuchMethodError::class)
    fun throwsNoSuchMethodExceptionWhenInvokingNonExistingMethod() {
        KotlinCompiler().compileMethod(
                "Solution", "dummy", 1, SOURCE_CODE_CORRECT
        )
    }

    @Test(expected = IllegalStateException::class)
    fun throwsIllegalStateExceptionWhenTryingToInvokeClassWithPrivateConstructor() {
        val sourceCode = """class Solution private constructor() {
  fun dummy() {}
}
"""

        KotlinCompiler().compileMethod(
                "Solution", "dummy", 0, sourceCode
        )
    }

    @Test(expected = CompileErrorException::class)
    fun throwsCompileErrorExceptionWhenSourceCodeDoesNotCompile() {

        KotlinCompiler().compileMethod(
                "Solution", "fib", 1, SOURCE_CODE_WITH_ERROR
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
import com.jalgoarena.type.*

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
import com.jalgoarena.type.*

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
