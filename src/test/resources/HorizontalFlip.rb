class Solution
  def flipHorizontalAxis(matrix)
    (matrix.length / 2).times do |i|
      tmp = matrix[i]
      matrix[i] = matrix[matrix.length - 1 - i]
      matrix[matrix.length - 1 - i] = tmp
    end
  end
end
