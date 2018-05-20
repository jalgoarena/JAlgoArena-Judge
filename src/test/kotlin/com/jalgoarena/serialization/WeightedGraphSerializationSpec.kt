package com.jalgoarena.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.type.WeightedGraph
import com.jalgoarena.type.WeightedGraphEdge
import com.jalgoarena.type.WeightedGraphNode
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test

class WeightedGraphSerializationSpec {

    private lateinit var objectMapper: ObjectMapper

    @Before
    fun setUp() {
        objectMapper = ApplicationConfiguration().objectMapper()
    }

    @Test
    fun serializes_simple_graph() {
        val graphAsString = objectMapper.writeValueAsString(GRAPH)
        assertThat(graphAsString).isEqualToIgnoringWhitespace(SIMPLE_GRAPH_JSON)
    }

    @Test
    fun deserializes_simple_graph() {
        val graph = objectMapper.readValue(SIMPLE_GRAPH_JSON, WeightedGraph::class.java)

        assertThat(graph.nodes).hasSize(3)
        assertThat(graph.edges).hasSize(3)
    }

    companion object {
        @Language("JSON")
        private const val SIMPLE_GRAPH_JSON = """
{"nodes":[{"name":"A"},{"name":"B"},{"name":"C"}],"edges":[{"from":"A","to":"B"},{"from":"B","to":"C"},{"from":"C","to":"A"}]}
"""
        private val GRAPH = WeightedGraph(
                arrayOf(WeightedGraphNode("A"), WeightedGraphNode("B"), WeightedGraphNode("C")),
                arrayOf(WeightedGraphEdge("A", "B"), WeightedGraphEdge("B", "C"), WeightedGraphEdge("C", "A"))
        )
    }
}