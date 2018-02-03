class Solution
  def isPalindrome(head)
    reversed = reverse_and_clone(head)
    is_equal(head, reversed)
  end

  private

  def reverse_and_clone(node)
    head = nil

    while node
      n = com.jalgoarena.type.ListNode.new(node.value)
      n.next = head
      head = n
      node = node.next
    end

    head
  end

  def is_equal(one, two)
    while one && two
      return false if one.value != two.value

      one = one.next
      two = two.next
    end

    !one && !two
  end
end
