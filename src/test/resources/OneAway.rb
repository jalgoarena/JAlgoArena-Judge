class Solution
  def oneEditAway(first, second)
    return true if first == second
    return false if isLengthDifferenceBiggerThanOne(first, second)

    shorter = first.length < second.length ? first : second
    longer  = first.length < second.length ? second : first

    isOneEditAway(shorter, longer)
  end

  private

  def isOneEditAway(shorter, longer)
    index_shorter = 0
    index_longer = 0
    found_difference = false

    while index_longer < longer.length && index_shorter < shorter.length
      if shorter[index_shorter] != longer[index_longer]
        return false if found_difference
        found_difference = true

        if shorter.length == longer.length
          index_shorter += 1
        end
      else
        index_shorter += 1
      end

      index_longer += 1
    end

    true
  end

  def isLengthDifferenceBiggerThanOne(first, second)
    (first.length - second.length).abs > 1
  end
end
