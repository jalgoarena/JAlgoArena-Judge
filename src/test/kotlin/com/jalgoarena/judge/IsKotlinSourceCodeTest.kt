package com.jalgoarena.judge

import com.jalgoarena.compile.IsKotlinSourceCode
import com.jalgoarena.domain.Function
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class IsKotlinSourceCodeTest {

    @Test
    fun identifies_kotlin_source_code() {
        val isKotlin = IsKotlinSourceCode().findIn(TWO_SUM_KOTLIN_SOURCE_CODE, TWO_SUM_FUNCTION)
        assertThat(isKotlin).isTrue()
    }

    @Test
    fun identifies_non_kotlin_source_code() {
        val isKotlin = IsKotlinSourceCode().findIn(TWO_SUM_JAVA_SOURCE_CODE, TWO_SUM_FUNCTION)
        assertThat(isKotlin).isFalse()
    }

    companion object {
        private val TWO_SUM_KOTLIN_SOURCE_CODE = """import java.util.*
import com.jalgoarena.type.*

class Solution {
    /**
     * @param numbers An array of Integers
     * @param target target = numbers[index1] + numbers[index2]
     * @return [index1 + 1, index2 + 1] (index1 < index2)
     */
    fun twoSum(numbers: IntArray, target: Int): IntArray {
        // Write your code here
    }
}
"""
        private val TWO_SUM_JAVA_SOURCE_CODE = """import java.util.*;
import com.jalgoarena.type.*;

public class Solution {
    /**
     * @param numbers An array of Integers
     * @param target target = numbers[index1] + numbers[index2]
     * @return [index1 + 1, index2 + 1] (index1 < index2)
     */
    public int[] twoSum(int[] numbers, int target) {
        // Write your code here
    }
}
"""

        private val TWO_SUM_FUNCTION = Function("twoSum",
                Function.Return("[I",
                        "[index1 + 1, index2 + 1] (index1 < index2)"),
                listOf(Function.Parameter("numbers", "[I", "An array of Integers"),
                        Function.Parameter("target", "java.lang.Integer",
                                "target = numbers[index1] + numbers[index2]")
                )
        )
    }
}
