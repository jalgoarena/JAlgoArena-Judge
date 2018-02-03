class Solution
  Index = Struct.new(:value)

  def kthToLast(head, k)
    idx = Index.new(-1)
    kthToLastIndex(head, k, idx).value
  end

  def kthToLastIndex(head, k, idx)
    if !head
      return com.jalgoarena.type.ListNode.new(0)
    end

    node = kthToLastIndex(head.next, k, idx)
    idx.value += 1

    if idx.value == k
      head
    else
      node
    end
  end
end
