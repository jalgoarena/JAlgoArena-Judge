import java.util.*
import org.algohub.engine.type.*

class RemoveDups {
    /**
     * @param linkedList Linked List where we need to remove duplicates
     * *
     * @return  Linked List with removed duplicates
     */
    fun removeDuplicates(listNode: ListNode?): ListNode? {
        var node = listNode
        val root = node
        val set = HashSet<Int>()
        var previous: ListNode? = null

        while (node != null) {
            if (set.contains(node.value)) {
                previous!!.next = node.next
            } else {
                set.add(node.value)
                previous = node
            }
            node = node.next
        }

        return root
    }
}