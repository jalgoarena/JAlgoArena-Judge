package com.jalgoarena.domain

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.boot.test.json.JacksonTester

class ProblemJsonSerializationTest {

    private lateinit var json: JacksonTester<Problem>

    @Before
    fun setup() {
        val objectMapper = jacksonObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }

    @Test
    fun serializes_problem() {
        assertThat(json.write(PROBLEM))
                .isEqualToJson("problem.json")
    }

    @Test
    fun deserialize_problem() {
        assertThat(json.parse(PROBLEM_JSON))
                .isEqualTo(PROBLEM)
    }

    private val TWO_SUM_FUNCTION = Function("twoSum",
            Function.Return("[I",
                    "[index1 + 1, index2 + 1] (index1 < index2)"),
            listOf(Function.Parameter("numbers", "[I", "An array of Integers"),
                    Function.Parameter("target", "java.lang.Integer",
                            "target = numbers[index1] + numbers[index2]")
            )
    )

    private val PROBLEM = Problem(
            id = "dummy_id",
            title = "dummy_title",
            description = "dummy description",
            level = 3,
            timeLimit = 1,
            func = TWO_SUM_FUNCTION,
            skeletonCode = mapOf(
                    Pair("java", "dummy code")
            ),
            testCases = listOf(
                    Problem.TestCase(
                            ArrayNode(JsonNodeFactory.instance).add(1).add(2),
                            IntNode(3)
                    )
            )
    )

    //language=JSON
    private val PROBLEM_JSON = """{
  "id": "dummy_id",
  "title": "dummy_title",
  "description": "dummy description",
  "timeLimit": 1,
  "func": {
    "name": "twoSum",
    "returnStatement": {
      "type": "[I",
      "comment": "[index1 + 1, index2 + 1] (index1 < index2)"
    },
    "parameters": [
      {
        "name": "numbers",
        "type": "[I",
        "comment": "An array of Integers"
      },
      {
        "name": "target",
        "type": "java.lang.Integer",
        "comment": "target = numbers[index1] + numbers[index2]"
      }
    ]
  },
  "testCases": [{"input":[1,2],"output":3}],
  "skeletonCode": {
    "java": "dummy code"
  },
  "level": 3
}
"""
}
