require 'set'

class Solution
  def isUniqueChars(str)
    set = Set.new

    str.to_s.chars.each do |char|
      return false if set.include?(char)
      set << char
    end

    true
  end
end