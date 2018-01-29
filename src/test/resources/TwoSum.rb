class TwoSum
  def twoSum(numbers, target)
    map = {}
    result = []

    numbers.each_with_index do |number, i|
      if (index = map[number])
        result[0] = index + 1
        result[1] = i + 1
        break
      else
        map[target - number] = i
      end
    end

    result
  end
end
