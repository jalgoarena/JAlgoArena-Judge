class Solution
  def zeroMatrix(matrix)
    return if matrix.length == 0 || matrix.length != matrix[0].length

    rows, columns = storeTheRowAndColumnIndexWithValueZero(matrix)

    rows.each { |i| nullify_row(matrix, i) }
    columns.each { |i| nullify_column(matrix, i) }
  end

  def nullify_column(matrix, index)
    matrix.length.times do |i|
      matrix[i][index] = 0
    end
  end

  def nullify_row(matrix, index)
    matrix.length.times do |i|
      matrix[index][i] = 0
    end
  end

  def storeTheRowAndColumnIndexWithValueZero(matrix)
    rows, columns = [], []
    matrix.each_with_index do |row, i|
      row.each_with_index do |cell, j|
        if cell == 0
          rows << i
          columns << j
        end
      end
    end

    [rows, columns]
  end
end