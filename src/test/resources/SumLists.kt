import org.algohub.engine.type.*

class SumLists {
    /**
     * @param l1 First Linked List to add
     * *
     * @param l2 Second Linked List to add
     * *
     * @return  linked list node containing result of sum
     */
    fun addLists(l1: ListNode, l2: ListNode): ListNode {
        return addLists(l1, l2, 0) as ListNode
    }

    private fun addLists(l1: ListNode?, l2: ListNode?, carry: Int): ListNode? {
        if (l1 == null && l2 == null && carry == 0) {
            return null
        }


        var value = carry
        if (l1 != null) {
            value += l1.value
        }
        if (l2 != null) {
            value += l2.value
        }

        val result = ListNode(value % 10)

        if (l1 != null || l2 != null) {
            val more = addLists(
                    l1?.next,
                    l2?.next,
                    if (value >= 10) 1 else 0
            )
            result.next = more
        }

        return result
    }
}
