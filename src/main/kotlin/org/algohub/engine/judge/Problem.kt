package org.algohub.engine.judge

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Problem(val id: String,
              val title: String,
              val description: String,
              @JsonProperty("time_limit") val timeLimit: Long,
              @JsonProperty("memory_limit") val memoryLimit: Int,
              val function: Function?,
              @JsonProperty("test_cases") val testCases: Array<Problem.TestCase>?,
              @JsonProperty("skeleton_code") val skeletonCode: String?,
              @JsonProperty("kotlin_skeleton_code") val kotlinSkeletonCode: String?,
              val level: Int) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    class TestCase(@JsonProperty("input") val input: ArrayNode,
                   @JsonProperty("output") val output: JsonNode)

    fun problemWithoutFunctionAndTestCases(skeletonCode: String, kotlinSkeletonCode: String): Problem {
        return Problem(
                id,
                title,
                description,
                timeLimit,
                memoryLimit,
                null,
                null,
                skeletonCode,
                kotlinSkeletonCode,
                level
        )
    }
}
