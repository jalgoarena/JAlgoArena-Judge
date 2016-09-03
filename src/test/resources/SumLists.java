import java.util.*;
import org.algohub.engine.type.*;

public class Solution {
    /**
     * @param l1 First Linked List to add
     * @param l2 Second Linked List to add
     * @return  linked list node containing result of sum
     */
    public LinkedListNode<Integer> addLists(LinkedListNode<Integer> l1, LinkedListNode<Integer> l2) {
        return addLists(l1, l2, 0);
    }

    private LinkedListNode<Integer> addLists(LinkedListNode<Integer> l1, LinkedListNode<Integer> l2, int carry) {
        if (l1 == null && l2 == null && carry == 0) {
            return null;
        }

        LinkedListNode<Integer> result = new LinkedListNode<>();
        int value = carry;
        if (l1 != null && l1.value != null) {
            value += l1.value;
        }
        if (l2 != null && l2.value != null) {
            value += l2.value;
        }

        result.value = value % 10;

        if (l1 != null || l2 != null) {
            LinkedListNode<Integer> more = addLists(
                    l1 == null ? null : l1.next,
                    l2 == null ? null : l2.next,
                    value >= 10 ? 1 : 0
            );
            result.next = more;
        }

        return result;
    }
}
