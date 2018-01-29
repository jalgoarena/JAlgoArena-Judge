class Solution
  def singleNumber(numbers)
    register = Hash.new { |hsh, key| hsh[key] = 0 }

    numbers.each do |number|
      register[number] += 1
    end

    value = register.find { |_, v| v == 1 }
    value ? value.first : 0
  end
end
