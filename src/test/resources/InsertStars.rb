class Solution
  def insertPairStar(str)
    return str if str.to_s.length <= 1

    if str[0] == str[1]
      "#{str[0]}*#{insertPairStar(str[1..-1])}"
    else
      "#{str[0]}#{insertPairStar(str[1..-1])}"
    end
  end
end
