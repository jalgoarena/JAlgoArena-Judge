package com.jalgoarena.type

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class WeightedGraphEdge(@JvmField var from: String, @JvmField var to: String, @JvmField var weight: Int? = null)
