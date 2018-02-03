class Solution
  def inorderTraversal(root)
    items = []
    inorder(root, items)
    items.map(&:to_i)
  end

  def inorder(root, items)
    if root
      inorder(root.left, items)
      items << root.data
      inorder(root.right, items)
    end
  end
end