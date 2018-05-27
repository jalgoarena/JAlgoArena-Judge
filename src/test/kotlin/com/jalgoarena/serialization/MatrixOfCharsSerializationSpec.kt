package com.jalgoarena.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jalgoarena.ApplicationConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test

class MatrixOfCharsSerializationSpec {

    private lateinit var objectMapper: ObjectMapper

    @Before
    fun setUp() {
        objectMapper = ApplicationConfiguration().objectMapper()
    }

    @Test
    fun serializes_matrix_of_chars() {
        val matrixAsString = objectMapper.writeValueAsString(MATRIX)
        assertThat(matrixAsString).isEqualToIgnoringWhitespace(MATRIX_JSON)
    }

    @Test
    fun deserializes_matrix_of_chars() {
        val graph = objectMapper.readValue<Array<Array<Char>>>(MATRIX_JSON)

        assertThat(graph[0][0]).isEqualTo('X')
        assertThat(graph[1][1]).isEqualTo('0')
        assertThat(graph[2][2]).isEqualTo('.')
    }

    companion object {
        @Language("JSON")
        private const val MATRIX_JSON = """
[["X","0","X"],[".","0","."],[".","X","."]]
"""

        private val MATRIX: Array<Array<Char>> = arrayOf(
                arrayOf('X', '0', 'X'),
                arrayOf('.', '0', '.'),
                arrayOf('.', 'X', '.')
        )
    }
}