class Solution
  def ladderLength(starts, ends, dict)
    result = 0

    if dict.size == 0
      return result
    end

    dict.add(starts)
    dict.add(ends)

    bfs(starts, ends, dict)
  end

  def bfs(starts, ends, dict)
    queue = java.util.LinkedList.new
    length = java.util.LinkedList.new

    queue.add(starts)
    length.add(1)

    while !queue.empty?
      word = queue.poll.to_java
      len = length.poll

      if word == ends
        return len
      end

      word.length.times do |i|
        arr = word.to_char_array
        (97..122).each do |c| # a..z
          next if arr[i] == c

          arr[i] = c

          str = java.lang.String.valueOf(arr)

          if dict.contains(str)
            queue.add(str)
            length.add(len + 1)
            dict.remove(word)
          end

          str = nil
        end
      end
    end
    0

  rescue => e
    puts e.inspect
    puts e.backtrace

    0
  end
end
