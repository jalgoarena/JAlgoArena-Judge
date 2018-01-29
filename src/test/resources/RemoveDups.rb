require 'set'

class Solution
  def removeDuplicates(node)
    root = node
    set = Set.new

    previous = nil

    while node
      if set.include?(node.value)
        previous.next = node.next
      else
        set << node.value
        previous = node
      end

      node = node.next
    end

    root
  end
end
