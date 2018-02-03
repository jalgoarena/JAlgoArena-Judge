class Solution
  def findMiddleNode(head)
    return unless head
    return head unless head.next

    slow = head
    fast = head.next

    while fast && fast.next
      slow = slow.next
      fast = fast.next.next
    end

    slow
  end
end
