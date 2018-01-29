class Solution
  def binarySearch(ary, n)
    return -1 if Array(ary).empty?

    lo = 0
    hi = ary.length - 1

    while lo <= hi
      mid = lo + (hi - lo) / 2

      if n < ary[mid]
        hi = mid - 1
      elsif n > ary[mid]
        lo = mid + 1
      else
        return mid
      end
    end

    return -1
  end
end
