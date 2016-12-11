package org.algohub.engine.judge

import org.junit.Test

import java.lang.reflect.Method

import org.assertj.core.api.Assertions.assertThat

class MemoryJavaCompilerTest {

    @Test
    @Throws(Throwable::class)
    fun compileAndRunStaticMethod() {

        val greetingObject = MemoryJavaCompiler().compileMethod(
                "Solution", "greeting", SOURCE_CODE
        )

        val obj = greetingObject[0]
        val greeting = greetingObject[1] as Method

        val result = greeting.invoke(obj, "Julia")
        assertThat(result).isEqualTo("Hello Julia")
    }

    @Test(expected = NoSuchMethodError::class)
    @Throws(Exception::class)
    fun throwsNoSuchMethodExceptionWhenInvokingNonExistingMethod() {
        MemoryJavaCompiler().compileMethod(
                "Solution", "dummy", SOURCE_CODE
        )
    }

    @Test(expected = IllegalStateException::class)
    @Throws(Exception::class)
    fun throwsIllegalStateExceptionWhenTryingToInvokeClassWithPrivateConstructor() {
        val sourceCode = "public final class Solution { private Solution() { }; public void dummy() {} }"

        MemoryJavaCompiler().compileMethod(
                "Solution", "dummy", sourceCode
        )
    }

    @Test(expected = CompileErrorException::class)
    @Throws(Exception::class)
    fun throwsCompileErrorExceptionWhenSourceCodeDoesNotCompile() {
        val sourceCodeMissingReturnString = "public final class Solution { private Solution() { }; public String dummy() {} }"

        MemoryJavaCompiler().compileMethod(
                "Solution", "dummy", sourceCodeMissingReturnString
        )
    }

    companion object {
        private val SOURCE_CODE =
                "public final class Solution {\npublic static String greeting(String name) {\n\treturn \"Hello \" + name;\n}\n}\n"
    }
}
