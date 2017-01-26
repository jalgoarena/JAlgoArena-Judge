package com.jalgoarena.codegeneration

import com.jalgoarena.domain.Function
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KotlinCodeGeneratorTest {

    private val kotlinCodeGenerator = KotlinCodeGenerator()

    @Test
    fun generates_skeleton_function_for_TWO_SUM() {
        val twoSumGenerated = kotlinCodeGenerator.generateEmptyFunction(TWO_SUM)

        val twoSumExpected = """import java.util.*
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
        assertThat(twoSumGenerated).isEqualTo(twoSumExpected)
    }

    @Test
    fun generates_skeleton_function_for_WORD_LADDER() {
        val wordLadderGenerated = kotlinCodeGenerator.generateEmptyFunction(WORD_LADDER)

        val wordLadderExpected = """import java.util.*
import com.jalgoarena.type.*

class Solution {
    /**
     * @param begin_word the begin word
     * @param end_word the end word
     * @param dict the dictionary
     * @return The shortest length
     */
    fun ladderLength(begin_word: String, end_word: String, dict: HashSet<String>): Int {
        // Write your code here
    }
}
"""
        assertThat(wordLadderGenerated).isEqualTo(wordLadderExpected)
    }

    @Test
    fun generates_skeleton_function_for_VERTICAL_FLIP() {
        val verticalFlipGenerated = kotlinCodeGenerator.generateEmptyFunction(VERTICAL_FLIP)

        val verticalFlipExpected = """import java.util.*
import com.jalgoarena.type.*

class Solution {
    /**
     * @param matrix Image matrix to flip
     * @return Operation in-place
     */
    fun flipVerticalAxis(matrix: Array<IntArray>) {
        // Write your code here
    }
}
"""
        assertThat(verticalFlipGenerated).isEqualTo(verticalFlipExpected)
    }

    @Test
    fun generates_skeleton_function_for_INSERT_RANGES_with_generics() {
        val generatedCode = kotlinCodeGenerator.generateEmptyFunction(INSERT_RANGE)

        val expectedCode = """import java.util.*
import com.jalgoarena.type.*

class Solution {
    /**
     * @param intervalsList sorted, non-overlapping list of Intervals
     * @param insert interval to insert
     * @return Array with inserted ranges
     */
    fun insertRange(intervalsList: ArrayList<Interval>, insert: Interval): ArrayList<Interval> {
        // Write your code here
    }
}
"""
        assertThat(generatedCode).isEqualTo(expectedCode)
    }

    companion object {
        private val TWO_SUM = Function("twoSum",
                Function.Return("[I",
                        "[index1 + 1, index2 + 1] (index1 < index2)"),
                listOf(Function.Parameter("numbers", "[I", "An array of Integers"),
                        Function.Parameter("target", "java.lang.Integer",
                                "target = numbers[index1] + numbers[index2]")
                )
        )

        private val WORD_LADDER = Function("ladderLength",
                Function.Return("java.lang.Integer", "The shortest length"),
                listOf(Function.Parameter("begin_word", "java.lang.String", "the begin word"),
                        Function.Parameter("end_word", "java.lang.String", "the end word"),
                        Function.Parameter("dict", "java.util.HashSet", "the dictionary", "String")
                )
        )

        private val INSERT_RANGE = Function("insertRange",
                Function.Return("java.util.ArrayList", "Array with inserted ranges", "Interval"),
                listOf(Function.Parameter("intervalsList", "java.util.ArrayList", "sorted, non-overlapping list of Intervals", "Interval"),
                        Function.Parameter("insert", "com.jalgoarena.type.Interval", "interval to insert")
                )
        )

        private val VERTICAL_FLIP = Function("flipVerticalAxis",
                Function.Return("void", "Operation in-place"),
                listOf(Function.Parameter("matrix", "[[I", "Image matrix to flip")))
    }
}
