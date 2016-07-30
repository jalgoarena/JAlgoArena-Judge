package org.algohub.engine.judge;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class FindClassNameSpec {

    private static final String JAVA_CODE = "public class MyClass { }";
    private static final String DUMMY_STRING = "abcdef {}";

    @Test
    public void findsClassInJavaCode() throws Exception {
        FindClassName findClassName = new FindClassName();

        Optional<String> result = findClassName.in(JAVA_CODE);

        if (result.isPresent()) {
            assertThat(result.get()).isEqualTo("MyClass");
        } else {
            fail("Class not found");
        }
    }

    @Test
    public void indicatesIfThereIsNoClassInTheInput() throws Exception {
        FindClassName findClassName = new FindClassName();

        Optional<String> result = findClassName.in(DUMMY_STRING);

        assertThat(result.isPresent()).isFalse();
    }
}