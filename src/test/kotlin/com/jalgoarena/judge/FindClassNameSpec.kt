package com.jalgoarena.judge

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test

class FindClassNameSpec {

    @Test
    fun findsClassInJavaCode() {

        val result = JAVA_CODE.findJavaClassName()

        if (result.isPresent) {
            assertThat(result.get()).isEqualTo("MyClass")
        } else {
            fail("Class not found")
        }
    }

    @Test
    fun indicatesIfThereIsNoClassInTheInput() {

        val result = DUMMY_STRING.findJavaClassName()
        assertThat(result.isPresent).isFalse()
    }

    companion object {
        private val JAVA_CODE = "public class MyClass { }"
        private val DUMMY_STRING = "abcdef {}"
    }
}
