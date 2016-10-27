import java.util.*;
import org.algohub.engine.type.*;

public class Solution {

    public int[] preorder(TreeNode root) {
        List<Integer> items = new ArrayList<>();
        preorderInternal(root, items);
        return items.stream().mapToInt(i -> i).toArray();
    }

    private void preorderInternal(TreeNode root, List<Integer> items) {
        if (root != null) {
            items.add(root.data);
            preorderInternal(root.left, items);
            preorderInternal(root.right, items);
        }
    }
}
