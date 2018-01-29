class Solution
  def findFirstNonRepeatedChar(str)
    return if str.to_s.empty?

    register = Hash.new { |hsh, key| hsh[key] = 0 }

    str.chars.each do |char|
      register[char] += 1
    end

    value = register.find { |_, v| v == 1 }
    value && value.first
  end
end
