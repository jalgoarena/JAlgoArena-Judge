import java.util.*;
import org.algohub.engine.type.*;

public class Solution {
    /**
     * @param head Linked List head
     * @return  Middle node
     */
    public LinkedListNode<Integer> findMiddleNode(LinkedListNode<Integer> head) {
        if (head == null) return null;
        if (head.next == null) return head;

        LinkedListNode slow = head;
        LinkedListNode fast = head.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }
}
