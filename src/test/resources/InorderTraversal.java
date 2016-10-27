import java.util.*;
import org.algohub.engine.type.*;

public class Solution {

    public int[] inorderTraversal(TreeNode root) {
        List<Integer> items = new ArrayList<>();
        inorder(root, items);
        return items.stream().mapToInt(i -> i).toArray();
    }

    private void inorder(TreeNode root, List<Integer> items) {
        if (root != null) {
            inorder(root.left, items);
            items.add(root.data);
            inorder(root.right, items);
        }
    }
}
