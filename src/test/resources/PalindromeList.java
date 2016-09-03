import java.util.*;
import org.algohub.engine.type.*;

public class Solution {
    /**
     * @param head Linked List to check if it's palindrome
     * @return  Indicates if input linked list is palindrome
     */
    public boolean isPalindrome(LinkedListNode<Integer> head) {
        LinkedListNode<Integer> reversed = reverseAndClone(head);
        return isEqual(head, reversed);
    }

    private LinkedListNode<Integer> reverseAndClone(LinkedListNode<Integer> node) {
        LinkedListNode<Integer> head = null;
        while (node != null) {
            LinkedListNode<Integer> n = new LinkedListNode<>(node.value);
            n.next = head;
            head = n;
            node = node.next;
        }
        return head;
    }

    private boolean isEqual(LinkedListNode<Integer> one, LinkedListNode<Integer> two) {
        while ((one != null && !one.isNull()) && (two != null && !two.isNull())) {
            if (one.value != two.value) {
                return false;
            }
            one = one.next;
            two = two.next;
        }
        return (one == null || one.isNull()) && (two == null || two.isNull());
    }
}
