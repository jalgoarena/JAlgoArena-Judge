class Solution
  def compress(str)
    return str if str.to_s.empty?
    compressed = ''
    count = 0

    str.length.times do |i|
      count += 1

      if i + 1 >= str.length || str[i] != str[i + 1]
        compressed += str[i]
        compressed += count.to_s
        count = 0
      end
    end

    compressed.length < str.length ? compressed : str
  end
end
