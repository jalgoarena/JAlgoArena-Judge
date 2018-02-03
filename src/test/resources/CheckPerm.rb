class Solution
  def permutation(str1, str2)
    return false unless str1.length == str2.length

    str2.split("").each do |char|
      return false unless str1[char]

      str1[char] = ''
    end

    true
  end
end
