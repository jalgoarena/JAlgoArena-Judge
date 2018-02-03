class Solution
  PartialSum = Struct.new(:sum, :carry)

  def addLists(l1, l2)
    len1 = length(l1)
    len2 = length(l2)

    if len1 < len2
      l1 = pad_list(l1, len2 - len1)
    else
      l2 = pad_list(l2, len1 - len2)
    end

    sum = add_lists_helper(l1, l2)

    if sum.carry.zero?
      sum.sum
    else
      insert_before(sum.sum, sum.carry)
    end
  end

  private

  def pad_list(list, padding)
    head = list

    padding.times do
      head = insert_before(head, 0)
    end

    head
  end

  def add_lists_helper(l1, l2)
    if !l1 && !l2
      return PartialSum.new(nil, 0)
    end

    sum = add_lists_helper(l1.next, l2.next)

    val = sum.carry + l1.value + l2.value

    full_result = insert_before(sum.sum, val % 10)

    sum.sum = full_result
    sum.carry = val / 10
    sum
  end

  def insert_before(list, data)
    node = com.jalgoarena.type.ListNode.new(data)
    node.next = list if list
    node
  end

  def length(node)
    return 0 unless node
    1 + length(node.next)
  end
end
