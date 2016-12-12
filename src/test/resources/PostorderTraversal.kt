import java.util.*
import org.algohub.engine.type.*

class PostorderTraversal {

    fun postorderTraversal(root: TreeNode): IntArray {
        val items = ArrayList<Int>()
        postorder(root, items)
        return items.toIntArray()
    }

    private fun postorder(root: TreeNode?, items: MutableList<Int>) {
        if (root != null) {
            postorder(root.left, items)
            postorder(root.right, items)
            items.add(root.data)
        }
    }
}
