class Solution
  def postorderTraversal(root)
    items = []
    postorder(root, items)
    items.map(&:to_i)
  end

  def postorder(root, items)
    if root
      postorder(root.left, items)
      postorder(root.right, items)
      items << root.data
    end
  end
end