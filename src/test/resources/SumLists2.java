import java.util.*;
import org.algohub.engine.type.*;

public class Solution {
    class PartialSum {
        public LinkedListNode<Integer> sum = null;
        public int carry = 0;
    }

    /**
     * @param l1 First Linked List to add
     * @param l2 Second Linked List to add
     * @return  linked list node containing result of sum
     */
    public LinkedListNode<Integer> addLists(LinkedListNode<Integer> l1, LinkedListNode<Integer> l2) {
        int len1 = length(l1);
        int len2 = length(l2);

        if (len1 < len2) {
            l1 = padList(l1, len2 - len1);
        } else {
            l2 = padList(l2, len1 - len2);
        }

        PartialSum sum = addListsHelper(l1, l2);

        if (sum.carry == 0) {
            return sum.sum;
        } else {
            LinkedListNode<Integer> result = insertBefore(sum.sum, sum.carry);
            return result;
        }
    }

    private PartialSum addListsHelper(LinkedListNode<Integer> l1, LinkedListNode<Integer> l2) {
        if (l1 == null && l2 == null) {
            return new PartialSum();
        }

        PartialSum sum = addListsHelper(l1.next, l2.next);

        int val = sum.carry + l1.value + l2.value;

        LinkedListNode fullResult = insertBefore(sum.sum, val % 10);

        sum.sum = fullResult;
        sum.carry = val / 10;
        return sum;
    }

    private LinkedListNode<Integer> insertBefore(LinkedListNode<Integer> list, int data) {
        LinkedListNode<Integer> node = new LinkedListNode<>(data);
        if (list != null) {
            node.next = list;
        }
        return node;
    }

    private LinkedListNode<Integer> padList(LinkedListNode<Integer> list, int padding) {
        LinkedListNode<Integer> head = list;
        for (int i = 0; i < padding; i++) {
            head = insertBefore(head, 0);
        }
        return head;
    }

    private int length(LinkedListNode<Integer> node) {
        if (node == null || node.value == null) return 0;

        return 1 + length(node.next);
    }
}
