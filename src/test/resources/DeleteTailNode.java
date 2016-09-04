import java.util.*;
import org.algohub.engine.type.*;

public class Solution {
    /**
     * @param head Linked List head
     * @return  Initial linked list with removed tail
     */
    public LinkedListNode<Integer> deleteAtTail(LinkedListNode<Integer> head) {
        if (head == null || head.next == null) {
            return null;
        }

        LinkedListNode<Integer> preTail = head;

        while (preTail.next.next != null) {
            preTail = preTail.next;
        }

        preTail.next = null;
        return head;
    }
}
