package org.algohub.engine;

import org.algohub.engine.judge.Function;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaCodeGeneratorTest {


    private static final Function TWO_SUM = new Function("twoSum",
            new Function.Return("[I",
                    "[index1 + 1, index2 + 1] (index1 < index2)"), new Function.Parameter[]{
            new Function.Parameter("numbers", "[I", "An array of Integers"),
            new Function.Parameter("target", "java.lang.Integer",
                    "target = numbers[index1] + numbers[index2]")});

    private static final Function WORD_LADDER = new Function("ladderLength",
            new Function.Return("java.lang.Integer", "The shortest length"),
            new Function.Parameter[]{
                    new Function.Parameter("begin_word", "java.lang.String", "the begin word"),
                    new Function.Parameter("end_word", "java.lang.String", "the end word"),
                    new Function.Parameter("dict", "java.util.HashSet", "the dictionary")});


    @Test
    public void generates_skeleton_function_for_TWO_SUM() throws Exception {
        String twoSumGenerated = JavaCodeGenerator.INSTANCE.generateEmptyFunction(TWO_SUM);

        String twoSumExpected = "import java.util.*;\n" +
                "import org.algohub.engine.type.*;\n\n" +
                "public class Solution {\n" + "    /**\n" + "     * @param numbers An array of Integers\n"
                        + "     * @param target target = numbers[index1] + numbers[index2]\n"
                        + "     * @return [index1 + 1, index2 + 1] (index1 < index2)\n" + "     */\n"
                        + "    public int[] twoSum(int[] numbers, int target) {\n"
                        + "        // Write your code here\n" + "    }\n" + "}\n";

        assertThat(twoSumGenerated).isEqualTo(twoSumExpected);
    }

    @Test
    public void generates_skeleton_function_for_WORD_LADDER() throws Exception {
        String wordLadderGenerated = JavaCodeGenerator.INSTANCE.generateEmptyFunction(WORD_LADDER);

        String wordLadderExpected = "import java.util.*;\n" +
                "import org.algohub.engine.type.*;\n\n" +
                "public class Solution {\n" + "    /**\n" + "     * @param begin_word the begin word\n"
                + "     * @param end_word the end word\n" + "     * @param dict the dictionary\n"
                + "     * @return The shortest length\n" + "     */\n"
                + "    public int ladderLength(String begin_word, String end_word, HashSet "
                + "dict) {\n"
                + "        // Write your code here\n" + "    }\n" + "}\n";

        assertThat(wordLadderGenerated).isEqualTo(wordLadderExpected);
    }
}
