package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Problem(val id: String,
                   val title: String,
                   val description: String,
                   val timeLimit: Long,
                   val memoryLimit: Int,
                   val func: Function?,
                   val testCases: List<TestCase>?,
                   val skeletonCode: Map<String, String>?,
                   val level: Int) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TestCase(val input: ArrayNode, val output: JsonNode)
}
