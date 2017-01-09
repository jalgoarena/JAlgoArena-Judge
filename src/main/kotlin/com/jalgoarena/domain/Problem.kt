package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Problem(val id: String,
              val title: String,
              val description: String,
              val timeLimit: Long,
              val memoryLimit: Int,
              val function: Function?,
              val testCases: Array<TestCase>?,
              val skeletonCode: String?,
              val kotlinSkeletonCode: String?,
              val level: Int) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    class TestCase(val input: ArrayNode,
                   val output: JsonNode)

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
