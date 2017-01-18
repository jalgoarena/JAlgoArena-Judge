import java.util.*

import com.jalgoarena.type.*

class MergeKSortedLinkedLists {
    fun mergeKLists(lists: ArrayList<ListNode?>): ListNode? {
        if (lists.size == 0) return null
        val queue = PriorityQueue(lists.size,
                Comparator<ListNode> { node1, node2 ->
                    if (node1.value > node2.value)
                        1
                    else if (node1.value == node2.value)
                        0
                    else
                        -1
                })
        for (list in lists) {
            if (list != null)
                queue.add(list)
        }
        val head: ListNode? = ListNode(0)
        var curr = head
        while (queue.size > 0) {
            val temp = queue.poll()
            curr!!.next = temp
            if (temp.next != null)
                queue.add(temp.next)
            curr = curr.next
        }
        return head!!.next
    }
}
