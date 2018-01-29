class Solution
  def preorderTraversal(root)
    items = []
    preorder(root, items)
    items.map(&:to_i)
  end

  def preorder(root, items)
    if root
      items << root.data
      preorder(root.left, items)
      preorder(root.right, items)
    end
  end
end