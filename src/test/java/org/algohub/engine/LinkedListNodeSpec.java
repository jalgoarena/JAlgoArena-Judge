package org.algohub.engine;

import org.algohub.engine.type.LinkedListNode;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkedListNodeSpec {

    private static final LinkedListNode<Integer> LINKED_LIST_1_2_3_4 = new LinkedListNode<>(1,
            new LinkedListNode<>(2,
                    new LinkedListNode<>(3,
                            new LinkedListNode<>(4)
                    )
            )
    );

    @Test
    public void stringRepresentationCorrespondentsLinkedListValues() throws Exception {

        assertThat(LINKED_LIST_1_2_3_4.toString()).isEqualTo("1->2->3->4");
    }

    @Test
    public void addsNewValueAsNodeToTail() throws Exception {
        LINKED_LIST_1_2_3_4.add(5);

        assertThat(LINKED_LIST_1_2_3_4.toString()).isEqualTo("1->2->3->4->5");
    }

    @Test
    public void defaultConstructorCreatesEmptyNode() throws Exception {
        assertThat(new LinkedListNode<Integer>().value).isNull();
    }
}