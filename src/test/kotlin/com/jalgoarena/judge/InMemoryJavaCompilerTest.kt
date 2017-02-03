package com.jalgoarena.judge

import com.jalgoarena.compile.CompileErrorException
import com.jalgoarena.compile.InMemoryJavaCompiler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InMemoryJavaCompilerTest {

    private val SOURCE_CODE =
            "public final class Solution {\npublic static String greeting(String name) {\n\treturn \"Hello \" + name;\n}\n}\n"

    @Test
    fun compileAndRunStaticMethod() {

        val (instance, method) = InMemoryJavaCompiler().compileMethod(
                "Solution", "greeting", 1, SOURCE_CODE
        )

        val result = method.invoke(instance, "Julia")
        assertThat(result).isEqualTo("Hello Julia")
    }

    @Test(expected = NoSuchMethodError::class)
    fun throwsNoSuchMethodExceptionWhenInvokingNonExistingMethod() {
        InMemoryJavaCompiler().compileMethod(
                "Solution", "dummy", 1, SOURCE_CODE
        )
    }

    @Test(expected = IllegalStateException::class)
    fun throwsIllegalStateExceptionWhenTryingToInvokeClassWithPrivateConstructor() {
        val sourceCode = "public final class Solution { private Solution() { }; public void dummy() {} }"

        InMemoryJavaCompiler().compileMethod(
                "Solution", "dummy", 0, sourceCode
        )
    }

    @Test(expected = CompileErrorException::class)
    fun throwsCompileErrorExceptionWhenSourceCodeDoesNotCompile() {
        val sourceCodeMissingReturnString = "public final class Solution { private Solution() { }; public String dummy() {} }"

        InMemoryJavaCompiler().compileMethod(
                "Solution", "dummy", 0, sourceCodeMissingReturnString
        )
    }
}
