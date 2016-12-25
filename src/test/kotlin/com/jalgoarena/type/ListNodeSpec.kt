package com.jalgoarena.type

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ListNodeSpec {

    @Test
    @Throws(Exception::class)
    fun stringRepresentationCorrespondentsLinkedListValues() {

        assertThat(linkedList_1_2_3_4().toString()).isEqualTo("[1, 2, 3, 4]")
    }

    @Test
    @Throws(Exception::class)
    fun addsNewValueAsNodeToTail() {
        val node = ListNode(1)
        node.add(5)

        assertThat(node.toString()).isEqualTo("[1, 5]")
    }

    @Test
    @Throws(Exception::class)
    fun equalsItself() {
        val node = ListNode(1)
        assertThat(node == node).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun equalsFalseIfComparedToDifferentObject() {

        assertThat(ListNode(3) == Any()).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun equalsFalseIfLinkedListIsDifferentByAnyElement() {

        val list1 = linkedList_1_2_3_4()
        list1.add(5)
        val list2 = linkedList_1_2_3_4()

        assertThat(list1 == list2).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun hashCodeIsDifferentForDifferentLinkedLists() {

        val list1 = linkedList_1_2_3_4()
        list1.add(5)
        val list2 = linkedList_1_2_3_4()

        assertThat(list1.hashCode() != list2.hashCode()).isTrue()
    }

    private fun linkedList_1_2_3_4(): ListNode {
        return ListNode(1).add(2).add(3).add(4)
    }
}
