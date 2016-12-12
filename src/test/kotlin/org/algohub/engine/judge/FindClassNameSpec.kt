package org.algohub.engine.judge

import org.algohub.engine.compile.FindJavaClassName
import org.junit.Test

import java.util.Optional

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

class FindClassNameSpec {

    @Test
    @Throws(Exception::class)
    fun findsClassInJavaCode() {

        val result = FindJavaClassName().findIn(JAVA_CODE)

        if (result.isPresent) {
            assertThat(result.get()).isEqualTo("MyClass")
        } else {
            fail("Class not found")
        }
    }

    @Test
    @Throws(Exception::class)
    fun indicatesIfThereIsNoClassInTheInput() {

        val result = FindJavaClassName().findIn(DUMMY_STRING)

        assertThat(result.isPresent).isFalse()
    }

    companion object {
        private val JAVA_CODE = "public class MyClass { }"
        private val DUMMY_STRING = "abcdef {}"
    }
}