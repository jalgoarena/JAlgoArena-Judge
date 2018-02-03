class Solution
  def isRotation(s1, s2)
    length = s1.length

    if length == s2.length && length > 0
      s1s1 = s1 + s1
      return (s1 + s1).include?(s2)
    end

    false
  end
end