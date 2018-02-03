class Solution
  def sum(root)
    return 0 unless root
    root.data + sum(root.left) + sum(root.right)
  end
end
