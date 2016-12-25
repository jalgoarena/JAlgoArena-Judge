package com.jalgoarena.judge

import com.jalgoarena.compile.CompileErrorException
import com.jalgoarena.compile.MemoryJavaCompiler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MemoryJavaCompilerTest {

    private val SOURCE_CODE =
            "public final class Solution {\npublic static String greeting(String name) {\n\treturn \"Hello \" + name;\n}\n}\n"

    @Test
    fun compileAndRunStaticMethod() {

        val (instance, method) = MemoryJavaCompiler().compileMethod(
                "Solution", "greeting", SOURCE_CODE
        )

        val result = method.invoke(instance, "Julia")
        assertThat(result).isEqualTo("Hello Julia")
    }

    @Test(expected = NoSuchMethodError::class)
    fun throwsNoSuchMethodExceptionWhenInvokingNonExistingMethod() {
        MemoryJavaCompiler().compileMethod(
                "Solution", "dummy", SOURCE_CODE
        )
    }

    @Test(expected = IllegalStateException::class)
    fun throwsIllegalStateExceptionWhenTryingToInvokeClassWithPrivateConstructor() {
        val sourceCode = "public final class Solution { private Solution() { }; public void dummy() {} }"

        MemoryJavaCompiler().compileMethod(
                "Solution", "dummy", sourceCode
        )
    }

    @Test(expected = CompileErrorException::class)
    fun throwsCompileErrorExceptionWhenSourceCodeDoesNotCompile() {
        val sourceCodeMissingReturnString = "public final class Solution { private Solution() { }; public String dummy() {} }"

        MemoryJavaCompiler().compileMethod(
                "Solution", "dummy", sourceCodeMissingReturnString
        )
    }
}
