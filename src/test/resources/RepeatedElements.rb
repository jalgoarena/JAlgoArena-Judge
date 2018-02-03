class Solution
  def findDuplicates(arr)
    result = java.util.HashSet.new

    arr = Array(arr).sort

    if arr.empty?
      return result.to_s
    end

    current = arr[0]
    idx = 1

    while idx < arr.length
      if arr[idx] == current
        result << current
      else
        current = arr[idx]
      end

      idx += 1
    end

    result.to_s
  end
end
