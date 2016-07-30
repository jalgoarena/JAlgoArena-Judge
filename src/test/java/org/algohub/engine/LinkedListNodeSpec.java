package org.algohub.engine;

import org.algohub.engine.type.LinkedListNode;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkedListNodeSpec {

    @Test
    public void stringRepresentationCorrespondentsLinkedListValues() throws Exception {

        LinkedListNode<Integer> node = new LinkedListNode<>(1,
                new LinkedListNode<>(2,
                        new LinkedListNode<>(3,
                                new LinkedListNode<>(4)
                        )
                )
        );
        assertThat(node.toString()).isEqualTo("[1, 2, 3, 4]");
    }

    @Test
    public void addsNewValueAsNodeToTail() throws Exception {
        LinkedListNode<Integer> node = new LinkedListNode<>(1);
        node.add(5);

        assertThat(node.toString()).isEqualTo("[1, 5]");
    }

    @Test
    public void defaultConstructorCreatesEmptyNode() throws Exception {
        assertThat(new LinkedListNode<Integer>().value).isNull();
    }
}