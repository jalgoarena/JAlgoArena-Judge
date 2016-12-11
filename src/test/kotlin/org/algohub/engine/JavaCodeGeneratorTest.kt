package org.algohub.engine

import org.algohub.engine.judge.Function
import org.junit.Test

import org.assertj.core.api.Assertions.assertThat

class JavaCodeGeneratorTest {


    @Test
    @Throws(Exception::class)
    fun generates_skeleton_function_for_TWO_SUM() {
        val twoSumGenerated = JavaCodeGenerator.generateEmptyFunction(TWO_SUM)

        val twoSumExpected = "import java.util.*;\n" +
                "import org.algohub.engine.type.*;\n\n" +
                "public class Solution {\n" + "    /**\n" + "     * @param numbers An array of Integers\n" +
                "     * @param target target = numbers[index1] + numbers[index2]\n" +
                "     * @return [index1 + 1, index2 + 1] (index1 < index2)\n" + "     */\n" +
                "    public int[] twoSum(int[] numbers, int target) {\n" +
                "        // Write your code here\n" + "    }\n" + "}\n"

        assertThat(twoSumGenerated).isEqualTo(twoSumExpected)
    }

    @Test
    @Throws(Exception::class)
    fun generates_skeleton_function_for_WORD_LADDER() {
        val wordLadderGenerated = JavaCodeGenerator.generateEmptyFunction(WORD_LADDER)

        val wordLadderExpected = "import java.util.*;\n" +
                "import org.algohub.engine.type.*;\n\n" +
                "public class Solution {\n" + "    /**\n" + "     * @param begin_word the begin word\n" +
                "     * @param end_word the end word\n" + "     * @param dict the dictionary\n" +
                "     * @return The shortest length\n" + "     */\n" +
                "    public int ladderLength(String begin_word, String end_word, HashSet " + "dict) {\n" +
                "        // Write your code here\n" + "    }\n" + "}\n"

        assertThat(wordLadderGenerated).isEqualTo(wordLadderExpected)
    }

    companion object {


        private val TWO_SUM = Function("twoSum",
                Function.Return("[I",
                        "[index1 + 1, index2 + 1] (index1 < index2)"),
                        arrayOf(Function.Parameter("numbers", "[I", "An array of Integers"),
                                Function.Parameter("target", "java.lang.Integer",
                                "target = numbers[index1] + numbers[index2]")
                        )
        )

        private val WORD_LADDER = Function("ladderLength",
                Function.Return("java.lang.Integer", "The shortest length"),
                        arrayOf(Function.Parameter("begin_word", "java.lang.String", "the begin word"),
                                Function.Parameter("end_word", "java.lang.String", "the end word"),
                                Function.Parameter("dict", "java.util.HashSet", "the dictionary")
                        )
        )
    }
}
