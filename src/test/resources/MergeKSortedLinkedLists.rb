class Solution
  def mergeKLists(lists)
    return if lists.empty?

    queue = java.util.PriorityQueue.new(
      lists.size,
      java.util.Comparator.impl do |method, *args|
        case method
        when :compare
          args[0].value <=> args[1].value
        end
      end
    )

    lists.each do |list|
      queue.add(list) if list
    end

    head = com.jalgoarena.type.ListNode.new(0)
    curr = head

    while queue.size > 0
      temp = queue.poll
      curr.next = temp
      queue.add(temp.next) if temp.next
      curr = curr.next
    end

    head.next
  rescue => e
    File.write("/home/dominik/test.log", "#{e.message}")
  end
end
