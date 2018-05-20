package com.jalgoarena.type

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class WeightedGraph(@JvmField var nodes: Array<WeightedGraphNode>, @JvmField var edges: Array<WeightedGraphEdge>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeightedGraph

        if (!Arrays.equals(nodes, other.nodes)) return false
        if (!Arrays.equals(edges, other.edges)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(nodes)
        result = 31 * result + Arrays.hashCode(edges)
        return result
    }
}