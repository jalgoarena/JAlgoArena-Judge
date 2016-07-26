package org.algohub.engine.compiler.java;

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
}
