class Solution
  def deleteAtTail(head)
    return if !head || !head.next

    pre_tail = head

    while pre_tail.next.next
      pre_tail = pre_tail.next
    end

    pre_tail.next = nil
    head
  end
end
