import java.util.*;
import org.algohub.engine.type.*;

public class Solution {
    /**
     * @param linkedList Linked List where we need to remove duplicates
     * @return  Linked List with removed duplicates
     */
    public LinkedListNode<Integer> removeDuplicates(LinkedListNode<Integer> node) {
        LinkedListNode<Integer> root = node;
        HashSet<Integer> set = new HashSet<>();
        LinkedListNode<Integer> previous = null;

        while (node != null) {
            if (set.contains(node.value)) {
                previous.next = node.next;
            } else {
                set.add(node.value);
                previous = node;
            }
            node = node.next;
        }

        return root;
    }
}