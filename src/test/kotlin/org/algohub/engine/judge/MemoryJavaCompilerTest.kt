package org.algohub.engine.judge

import org.algohub.engine.compile.CompileErrorException
import org.algohub.engine.compile.MemoryJavaCompiler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MemoryJavaCompilerTest {

    @Test
    @Throws(Throwable::class)
    fun compileAndRunStaticMethod() {

        val (instance, method) = MemoryJavaCompiler().compileMethod(
                "Solution", "greeting", SOURCE_CODE
        )

        val result = method.invoke(instance, "Julia")
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
