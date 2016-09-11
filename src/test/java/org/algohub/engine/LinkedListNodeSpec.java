package org.algohub.engine;

import org.algohub.engine.type.ListNode;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkedListNodeSpec {

    @Test
    public void stringRepresentationCorrespondentsLinkedListValues() throws Exception {

        assertThat(linkedList_1_2_3_4().toString()).isEqualTo("[1, 2, 3, 4]");
    }

    @Test
    public void addsNewValueAsNodeToTail() throws Exception {
        ListNode node = new ListNode(1);
        node.add(5);

        assertThat(node.toString()).isEqualTo("[1, 5]");
    }

    @Test
    @SuppressWarnings("all")
    public void equalsItself() throws Exception {
        ListNode node = new ListNode(1);
        assertThat(node.equals(node)).isTrue();
    }

    @Test
    public void equalsFalseIfComparedToDifferentObject() throws Exception {

        assertThat(new ListNode(3).equals(new Object())).isFalse();
    }

    @Test
    public void equalsFalseIfLinkedListIsDifferentByAnyElement() throws Exception {

        ListNode list1 = linkedList_1_2_3_4();
        list1.add(5);
        ListNode list2 = linkedList_1_2_3_4();

        assertThat(list1.equals(list2)).isFalse();
    }

    @Test
    public void hashCodeIsDifferentForDifferentLinkedLists() throws Exception {

        ListNode list1 = linkedList_1_2_3_4();
        list1.add(5);
        ListNode list2 = linkedList_1_2_3_4();

        assertThat(list1.hashCode() != list2.hashCode()).isTrue();
    }

    private static ListNode linkedList_1_2_3_4() {
        return new ListNode(1).add(2).add(3).add(4);
    }
}