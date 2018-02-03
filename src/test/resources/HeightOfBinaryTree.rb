class Solution
  # @param root Root of binary tree
  # @return  Height of binary tree
  def findHeight(root)
    return 0 unless root

    lh = findHeight(root.left)
    rh = findHeight(root.right)
    lh > rh ? lh + 1 : rh + 1
  end
end
