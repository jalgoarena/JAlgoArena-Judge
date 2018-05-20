package com.jalgoarena.type

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

@JsonIgnoreProperties(ignoreUnknown = true)
data class GraphNode(@JvmField var name: String, @JvmField var adjacentNodes: ArrayList<GraphNode>) {

    class Deserializer : JsonDeserializer<GraphNode>() {
        override fun deserialize(jsonParser: JsonParser, context: DeserializationContext): GraphNode {
            val weightedGraph = jsonParser.codec.readValue(jsonParser, WeightedGraph::class.java)

            val nodes = mutableListOf<GraphNode>()
            weightedGraph.nodes.forEach { weightedGraphNode -> nodes.add(GraphNode(weightedGraphNode.name, ArrayList())) }

            weightedGraph.edges.forEach { weightedGraphEdge ->
                val from = nodes.first { it -> it.name == weightedGraphEdge.from }
                val to = nodes.first { it -> it.name == weightedGraphEdge.to }

                from.adjacentNodes.add(to)
                to.adjacentNodes.add(from)
            }

            return nodes.first()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GraphNode

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}