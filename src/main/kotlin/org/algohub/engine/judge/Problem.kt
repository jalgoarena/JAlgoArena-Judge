package org.algohub.engine.judge

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Problem
@JsonCreator
constructor(@JsonProperty("id") val id: String,
            @JsonProperty("title") val title: String,
            @JsonProperty("description") val description: String,
            @JsonProperty("time_limit") val timeLimit: Long,
            @JsonProperty("memory_limit") val memoryLimit: Int,
            @JsonProperty("function") val function: Function?,
            @JsonProperty("test_cases") val testCases: Array<Problem.TestCase>?,
            @JsonProperty("source_code") val skeletonCode: String?,
            @JsonProperty("source_code_kotlin") val kotlinSkeletonCode: String?,
            @JsonProperty("level") val level: Int) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    class TestCase
    @JsonCreator
    constructor(@JsonProperty("input") val input: ArrayNode,
                @JsonProperty("output") val output: JsonNode)

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Example
    @JsonCreator
    constructor(@JsonProperty("input") val input: String,
                @JsonProperty("output") val output: String)

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
