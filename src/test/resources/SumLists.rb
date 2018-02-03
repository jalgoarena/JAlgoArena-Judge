class Solution
  def addLists(l1, l2)
    addListsCarry(l1, l2, 0)
  end

  private

  def addListsCarry(l1, l2, carry)
    return if l1.nil? && l2.nil? && carry == 0

    value = carry

    if l1
      value += l1.value
    end

    if l2
      value += l2.value
    end

    result = com.jalgoarena.type.ListNode.new(value % 10)

    if l1 || l2
      more = addListsCarry(
        l1&.next,
        l2&.next,
        value >= 10 ? 1 : 0
      )
      result.next = more
    end

    result
  end
end
