package org.algohub.engine.judge;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryJavaCompilerTest {

    private static final String SOURCE_CODE =
            "public final class Solution {\n" +
                    "public static String greeting(String name) {\n" +
                    "\treturn \"Hello \" + name;\n" + "}\n}\n";

    @Test
    public void compileAndRunStaticMethod() throws Throwable {

        Object[] greetingObject = MemoryJavaCompiler.INSTANCE.compileMethod(
                "Solution", "greeting", SOURCE_CODE
        );

        Object obj = greetingObject[0];
        Method greeting = (Method) greetingObject[1];

        Object result = greeting.invoke(obj, "Julia");
        assertThat(result).isEqualTo("Hello Julia");
    }

    @Test(expected = NoSuchMethodError.class)
    public void throwsNoSuchMethodExceptionWhenInvokingNonExistingMethod() throws Exception {
        MemoryJavaCompiler.INSTANCE.compileMethod(
                "Solution", "dummy", SOURCE_CODE
        );
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionWhenTryingToInvokeClassWithPrivateConstructor() throws Exception {
        String sourceCode =
                "public final class Solution { private Solution() { }; public void dummy() {} }";

        MemoryJavaCompiler.INSTANCE.compileMethod(
                "Solution", "dummy", sourceCode
        );
    }

    @Test(expected = CompileErrorException.class)
    public void throwsCompileErrorExceptionWhenSourceCodeDoesNotCompile() throws Exception {
        String sourceCodeMissingReturnString =
                "public final class Solution { private Solution() { }; public String dummy() {} }";

        MemoryJavaCompiler.INSTANCE.compileMethod(
                "Solution", "dummy", sourceCodeMissingReturnString
        );
    }
}
