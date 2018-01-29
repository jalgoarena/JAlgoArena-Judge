package com.jalgoarena.judge

import com.jalgoarena.compile.CompileErrorException
import com.jalgoarena.compile.RubyCompiler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RubyCompilerTest {

    @Test
    fun compileAndRunStaticMethod() {
        val (instance, method) = RubyCompiler().compileMethod(
                "Solution", "greeting", 1, HELLO_WORLD_SOURCE_CODE
        )

        val result = method.invoke(instance, "Julia")
        assertThat(result).isEqualTo("Hello Julia")
    }

    @Test(expected = NoSuchMethodError::class)
    fun throwsNoSuchMethodExceptionWhenInvokingNonExistingMethod() {
        RubyCompiler().compileMethod(
                "Solution", "dummy", 1, SOURCE_CODE_CORRECT
        )
    }


    @Test(expected = CompileErrorException::class)
    fun throwsCompileErrorExceptionWhenSourceCodeDoesNotCompile() {

        RubyCompiler().compileMethod(
                "Solution", "fib", 1, SOURCE_CODE_WITH_ERROR
        )
    }

    companion object {

        private val HELLO_WORLD_SOURCE_CODE = """class Solution
  def greeting(name)
    "Hello #{name}"
  end
end
"""
        private val SOURCE_CODE_CORRECT = """class Solution
  def fib(n)
    return n if n < 2
    fib(n - 1) + fib(n - 2)
  end
end
"""

        private val SOURCE_CODE_WITH_ERROR = """class Solution
  def fib(n
    return n if n < 2
    fib(n - 1) + fib(n - 2)
  end
end
"""

    }
}
