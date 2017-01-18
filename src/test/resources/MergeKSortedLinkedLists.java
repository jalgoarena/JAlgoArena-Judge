import java.util.*;

import com.jalgoarena.type.*;

public class Solution {
    public ListNode mergeKLists(ArrayList<ListNode> lists) {
        if (lists.size() == 0) return null;
        PriorityQueue<ListNode> queue = new PriorityQueue<ListNode>(lists.size(),
                new Comparator<ListNode>() {
                    public int compare(ListNode node1, ListNode node2) {
                        if (node1.value > node2.value)
                            return 1;
                        else if (node1.value == node2.value)
                            return 0;
                        else
                            return -1;
                    }
                });
        for (ListNode list : lists) {
            if (list != null)
                queue.add(list);
        }
        ListNode head = new ListNode(0), curr = head;
        while (queue.size() > 0) {
            ListNode temp = queue.poll();
            curr.next = temp;
            if (temp.next != null)
                queue.add(temp.next);
            curr = curr.next;
        }
        return head.next;
    }
}
