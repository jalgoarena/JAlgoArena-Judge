package org.algohub.engine;

import org.algohub.engine.judge.Function;
import org.algohub.engine.type.TypeNode;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class JavaCodeGeneratorTest {
    private static final TypeNode ARRAY_INT = TypeNode.fromString("array<int>");
    private static final TypeNode LIST_INT = TypeNode.fromString("list<int>");
    private static final TypeNode LIST_ARRAY_INT = TypeNode.fromString("list<array<int>>");
    private static final TypeNode LIST_LIST_INT = TypeNode.fromString("list<list<int>>");
    private static final TypeNode ARRAY_LIST_SET_MAP =
            TypeNode.fromString("array<list<set<map<string,LinkedListNode<int>>>>>");

    private static final Function TWO_SUM = new Function("twoSum",
            new Function.Return(TypeNode.fromString("array<int>"),
                    "[index1 + 1, index2 + 1] (index1 < index2)"), new Function.Parameter[]{
            new Function.Parameter("numbers", TypeNode.fromString("array<int>"), "An array of Integers"),
            new Function.Parameter("target", TypeNode.fromString("int"),
                    "target = numbers[index1] + numbers[index2]")});

    private static final Function WORD_LADDER = new Function("ladderLength",
            new Function.Return(TypeNode.fromString("int"), "The shortest length"),
            new Function.Parameter[]{
                    new Function.Parameter("begin_word", TypeNode.fromString("string"), "the begin word"),
                    new Function.Parameter("end_word", TypeNode.fromString("string"), "the end word"),
                    new Function.Parameter("dict", TypeNode.fromString("set<string>"), "the dictionary")});


    @Test
    public void generateEmptyFunctionTest1() {
        final String twoSumGenerated =
                JavaCodeGenerator.generateEmptyFunction(TWO_SUM);
        final String twoSumExpected = "import java.util.*;\n" +
                "import org.algohub.engine.type.*;\n\n" +
                "public class Solution {\n" + "    /**\n" + "     * @param numbers An array of Integers\n"
                        + "     * @param target target = numbers[index1] + numbers[index2]\n"
                        + "     * @return [index1 + 1, index2 + 1] (index1 < index2)\n" + "     */\n"
                        + "    public int[] twoSum(int[] numbers, int target) {\n"
                        + "        // Write your code here\n" + "    }\n" + "}\n";
        assertEquals(twoSumExpected, twoSumGenerated);

        final String wordLadderGenerated =
                JavaCodeGenerator.generateEmptyFunction(WORD_LADDER);
        final String wordLadderExpected = "import java.util.*;\n" +
                "import org.algohub.engine.type.*;\n\n" +
                "public class Solution {\n" + "    /**\n" + "     * @param begin_word the begin word\n"
                        + "     * @param end_word the end word\n" + "     * @param dict the dictionary\n"
                        + "     * @return The shortest length\n" + "     */\n"
                        + "    public int ladderLength(String begin_word, String end_word, HashSet<String> "
                        + "dict) {\n"
                        + "        // Write your code here\n" + "    }\n" + "}\n";
        assertEquals(wordLadderExpected, wordLadderGenerated);
    }

    @Test
    public void generateArrayOfInt() {
        final String typeStr1 = JavaCodeGenerator.generateTypeDeclaration(ARRAY_INT);
        assertEquals("int[]", typeStr1);

    }

    @Test
    public void generatesArrayListOfInt() {
        final String typeStr2 = JavaCodeGenerator.generateTypeDeclaration(LIST_INT);
        assertEquals("ArrayList<Integer>", typeStr2);
    }

    @Test
    public void generatesArrayListOfArray() {
        final String typeStr3 = JavaCodeGenerator.generateTypeDeclaration(LIST_ARRAY_INT);
        assertEquals("ArrayList<int[]>", typeStr3);

    }

    @Test
    public void generatesArrayListOfArrayList() {
        final String typeStr4 = JavaCodeGenerator.generateTypeDeclaration(LIST_LIST_INT);
        assertEquals("ArrayList<ArrayList<Integer>>", typeStr4);

    }

    @Test
    public void generatesComplexArrayList() {
        final String typeStr5 = JavaCodeGenerator.generateTypeDeclaration(ARRAY_LIST_SET_MAP);
        assertEquals("ArrayList<HashSet<HashMap<String,LinkedListNode<Integer>>>>[]", typeStr5);
    }

    @Test
    public void generatesLinkedListNode() throws Exception {
        TypeNode linkedListTypeNode = TypeNode.fromString("LinkedListNode<int>");
        String linkedListJavaTypeDeclaration = JavaCodeGenerator.generateTypeDeclaration(
                linkedListTypeNode
        );

        assertThat(linkedListJavaTypeDeclaration).isEqualTo("LinkedListNode<Integer>");
    }
}
