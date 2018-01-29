class Solution
  def transposeMatrix(matrix)
    n = matrix.length - 1

    (0..n).each do |i|
      ((i + 1)..n).each do |j|
        temp = matrix[i][j]
        matrix[i][j] = matrix[j][i]
        matrix[j][i] = temp
      end
    end
  end
end
