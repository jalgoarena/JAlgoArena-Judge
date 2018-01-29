class Solution
  def flipVerticalAxis(matrix)
    r = matrix.length - 1
    c = matrix[0].length - 1

    (0..r).each do |i|
      (0..(c / 2)).each do |j|
        temp = matrix[i][j]
        matrix[i][j] = matrix[i][c - j]
        matrix[i][c - j] = temp
      end
    end
  end
end
