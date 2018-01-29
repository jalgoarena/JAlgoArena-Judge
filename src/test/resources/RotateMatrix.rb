class Solution
  def rotate(matrix)
    return if matrix.length.zero? || matrix.length != matrix[0].length

    n = matrix.length

    (n / 2).times do |layer|
      first = layer
      last = n - 1 - layer

      (first...last).each do |i|
        offset = i - first
        top = matrix[first][i]

        matrix[first][i] = matrix[last - offset][first]
        matrix[last - offset][first] = matrix[last][last - offset]
        matrix[last][last - offset] = matrix[i][last]
        matrix[i][last] = top
      end
    end
  end
end
