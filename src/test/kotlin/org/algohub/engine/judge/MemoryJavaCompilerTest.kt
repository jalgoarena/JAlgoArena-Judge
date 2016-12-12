package org.algohub.engine.judge

import org.algohub.engine.compile.CompileErrorException
import org.algohub.engine.compile.JvmCompiler
import org.algohub.engine.compile.MemoryJavaCompiler
import org.junit.Test

import java.lang.reflect.Method

import org.assertj.core.api.Assertions.assertThat

class MemoryJavaCompilerTest {

    @Test
    @Throws(Throwable::class)
    fun compileAndRunStaticMethod() {

        val greetingObject = JvmCompiler().compileMethod(
                "Solution", "greeting", SOURCE_CODE, MemoryJavaCompiler()
        )

        val obj = greetingObject[0]
        val greeting = greetingObject[1] as Method

        val result = greeting.invoke(obj, "Julia")
        assertThat(result).isEqualTo("Hello Julia")
    }

    @Test(expected = NoSuchMethodError::class)
    @Throws(Exception::class)
    fun throwsNoSuchMethodExceptionWhenInvokingNonExistingMethod() {
        JvmCompiler().compileMethod(
                "Solution", "dummy", SOURCE_CODE, MemoryJavaCompiler()
        )
    }

    @Test(expected = IllegalStateException::class)
    @Throws(Exception::class)
    fun throwsIllegalStateExceptionWhenTryingToInvokeClassWithPrivateConstructor() {
        val sourceCode = "public final class Solution { private Solution() { }; public void dummy() {} }"

        JvmCompiler().compileMethod(
                "Solution", "dummy", sourceCode, MemoryJavaCompiler()
        )
    }

    @Test(expected = CompileErrorException::class)
    @Throws(Exception::class)
    fun throwsCompileErrorExceptionWhenSourceCodeDoesNotCompile() {
        val sourceCodeMissingReturnString = "public final class Solution { private Solution() { }; public String dummy() {} }"

        JvmCompiler().compileMethod(
                "Solution", "dummy", sourceCodeMissingReturnString, MemoryJavaCompiler()
        )
    }

    companion object {
        private val SOURCE_CODE =
                "public final class Solution {\npublic static String greeting(String name) {\n\treturn \"Hello \" + name;\n}\n}\n"
    }
}
