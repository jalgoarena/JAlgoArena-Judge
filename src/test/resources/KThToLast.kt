import org.algohub.engine.type.*

class KThToLast {
    internal inner class Index {
        var value = -1
    }

    /**
     * @param head Linked List where we need to find kth to last element
     * *
     * @param k index of element to find
     * *
     * @return  kth to last element of a singly linked list
     */
    fun kthToLast(head: ListNode, k: Int): Int {
        val idx = Index()
        return kthToLast(head, k, idx).value
    }

    private fun kthToLast(head: ListNode?, k: Int, idx: Index): ListNode {
        if (head == null) {
            return ListNode(0)
        }

        val node = kthToLast(head.next, k, idx)
        idx.value += 1

        if (idx.value == k) {
            return head
        }

        return node
    }
}
