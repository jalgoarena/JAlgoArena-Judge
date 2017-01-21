package com.jalgoarena.type

data class Vertex(
        @JvmField var name: String,
        @JvmField var adjacencies: List<Edge>? = null,
        @JvmField var previous: Vertex? = null,
        @JvmField var minDistance: Double = Double.POSITIVE_INFINITY
)
