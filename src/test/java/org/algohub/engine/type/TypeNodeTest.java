package org.algohub.engine.type;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TypeNodeTest {

    @Test
    public void checkAngleBracketsTest() {
        assertFalse(TypeNode.checkAngleBrackets("<"));
        assertTrue(TypeNode.checkAngleBrackets("<>"));
        assertFalse(TypeNode.checkAngleBrackets("><"));
        assertFalse(TypeNode.checkAngleBrackets("<abcd<e>"));
        assertTrue(TypeNode.checkAngleBrackets("<a><b>"));
    }

    @Test
    public void findRightBracketTest() {
        assertEquals(10, TypeNode.findRightBracket("vector<int>", 6));
        assertEquals(22, TypeNode.findRightBracket("map<string,vector<int>>", 3));
        assertEquals(21, TypeNode.findRightBracket("map<string,vector<int>>", 17));
    }

    @Test
    public void intFromString() {
        final TypeNode node1 = TypeNode.fromString("int");
        assertThat(node1.getValue()).isEqualTo(IntermediateType.INT);
        assertThat(node1.getElementType()).isNull();
        assertThat(node1.getKeyType()).isNull();
        assertThat(node1.getParent()).isNull();
    }

    @Test
    public void listOfIntFromString() {
        final TypeNode node2 = TypeNode.fromString("list<int>");
        assertThat(node2.getValue()).isEqualTo(IntermediateType.LIST);
        assertThat(node2.getElementType()).isNotNull();
        assertThat(node2.getKeyType()).isNull();
        assertThat(node2.getParent()).isNull();
    }

    @Test
    public void listIntItemFromString() {
        final TypeNode node2 = TypeNode.fromString("list<int>");
        final TypeNode node2Child = node2.getElementType();

        assertThat(node2Child.getValue()).isEqualTo(IntermediateType.INT);
        assertThat(node2Child.getElementType()).isNull();
        assertThat(node2Child.getKeyType()).isNull();
        assertThat(node2Child.getParent()).isNotNull();
        assertThat(node2Child.getParent().getValue()).isEqualTo(IntermediateType.LIST);
    }

    @Test
    public void mapOfKeyAsStringAndValueAsListOfIntFromString() {
        final TypeNode mapNode = TypeNode.fromString("map<string,list<int>>");

        assertThat(mapNode.getValue()).isEqualTo(IntermediateType.MAP);
        assertThat(mapNode.getElementType()).isNotNull();
        assertThat(mapNode.getKeyType()).isNotNull();
        assertThat(IntermediateType.STRING).isEqualTo(mapNode.getKeyType().getValue());
        assertThat(IntermediateType.LIST).isEqualTo(mapNode.getElementType().getValue());
        assertThat(IntermediateType.INT).isEqualTo(
                mapNode.getElementType().getElementType().getValue());
    }
}
