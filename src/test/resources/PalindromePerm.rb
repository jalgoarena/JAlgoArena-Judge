class Solution
  def isPermutationOfPalindrome(phrase)
    return false if phrase.to_s.empty?

    table = build_char_frequency_table(phrase.downcase)
    check_max_one_odd(table)
  end

  private

  def check_max_one_odd(table)
    found_odd = false

    table.each do |count|
      if count.to_i % 2 == 1
        return false if found_odd
        found_odd = true
      end
    end

    true
  end

  def get_char_number(c)
    a = 'a'.bytes.first
    z = 'z'.bytes.first
    val = c.bytes.first

    if a <= val && val <= z
      val - a
    else
      -1
    end
  end

  def build_char_frequency_table(phrase)
    table = []

    phrase.split('').each_with_index do |c, i|
      x = get_char_number(c)
      table[x] = table[x].to_i + 1 if x != -1
    end

    table
  end
end
