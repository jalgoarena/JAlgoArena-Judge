package com.jalgoarena.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.type.GraphNode
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test

class GraphNodeSerializationSpec {

    private lateinit var objectMapper: ObjectMapper

    @Before
    fun setUp() {
        objectMapper = ApplicationConfiguration().objectMapper()
    }

    @Test
    fun deserialize_simple_graph() {
        val node = objectMapper.readValue(SIMPLE_GRAPH_JSON, GraphNode::class.java)

        assertThat(node.adjacentNodes).hasSize(2)
        assertThat(node.adjacentNodes[0].adjacentNodes).hasSize(2)
        assertThat(node.adjacentNodes[1].adjacentNodes).hasSize(2)

        assertThat(node).isEqualTo(node.adjacentNodes[0].adjacentNodes[0])
    }

    companion object {
        @Language("JSON")
        private const val SIMPLE_GRAPH_JSON = """
{"nodes":[{"name":"A"},{"name":"B"},{"name":"C"}],"edges":[{"from":"A","to":"B"},{"from":"B","to":"C"},{"from":"C","to":"A"}]}
"""
    }
}