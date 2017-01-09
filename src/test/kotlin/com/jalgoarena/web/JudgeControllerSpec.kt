package com.jalgoarena.web

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.data.DataRepository
import com.jalgoarena.domain.Problem
import com.jalgoarena.domain.StatusCode
import com.jalgoarena.judge.JudgeEngine
import com.jalgoarena.type.ListNode
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.endsWith
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.internal.matchers.GreaterThan
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.inject.Inject

@RunWith(SpringRunner::class)
@WebMvcTest(JudgeController::class)
@ContextConfiguration(classes = arrayOf(JudgeControllerSpec.ControllerTestConfiguration::class))
class JudgeControllerSpec {

    @Inject
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var problemsClient: DataRepository<Problem>

    @Test
    fun post_judge_returns_200_and_accepted_judge_result_for_correct_submission() {
        givenFibProblem()

        mockMvc.perform(post("/problems/fib/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FIB_SOURCE_CODE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.statusCode", `is`(StatusCode.ACCEPTED.name)))
                .andExpect(jsonPath("$.elapsedTime", `is`(GreaterThan(0.0))))
                .andExpect(jsonPath("$.testcaseResults", hasSize<ArrayNode>(8)))
    }

    @Test
    fun post_judge_returns_200_and_compilation_error_judge_result_for_wrong_source_code() {
        givenFibProblem()

        mockMvc.perform(post("/problems/fib/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FIB_SOURCE_CODE_WITH_COMPILE_ERROR))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.statusCode", `is`(StatusCode.COMPILE_ERROR.name)))
                .andExpect(jsonPath("$.errorMessage", endsWith("/Solution.kt:10:5: error: a 'return' expression required in a function with a block body ('{...}')\n    }\n    ^\n")))
    }

    @Test
    fun post_judge_returns_200_and_runtime_error_judge_result_for_source_code_with_runtime_error() {
        givenFibProblem()

        mockMvc.perform(post("/problems/fib/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FIB_SOURCE_CODE_WITH_RUNTIME_ERROR))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.statusCode", `is`(StatusCode.RUNTIME_ERROR.name)))
                .andExpect(jsonPath("$.errorMessage", `is`("java.lang.InterruptedException: java.lang.Exception: null")))
    }

    private fun givenFibProblem() {
        val problem = jacksonObjectMapper().readValue(PROBLEM_AS_JSON, Problem::class.java)
        given(problemsClient.find("fib")).willReturn(problem)
    }

    @TestConfiguration
    open class ControllerTestConfiguration {

        @Bean
        open fun objectMapper(): ObjectMapper {
            val objectMapper = jacksonObjectMapper()
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

            val customModule = SimpleModule()
            customModule.addDeserializer(ListNode::class.java, ListNode.Deserializer())
            objectMapper.registerModule(customModule)
            return objectMapper
        }

        @Bean
        open fun judgeEngine(objectMapper: ObjectMapper) = JudgeEngine(objectMapper)
    }

    //language=JSON
    private val PROBLEM_AS_JSON = """{
  "id": "fib",
  "title": "Fibonacci",
  "description": "Write the `fib` function to return the N'th term.\r\nWe start counting from:\r\n* fib(0) = 0\r\n* fib(1) = 1.\r\n\r\n### Examples\r\n\r\n* `0` -> `0`\r\n* `6` -> `8`",
  "timeLimit": 1,
  "memoryLimit": 32,
  "function": {
    "name": "fib",
    "return": {
      "type": "java.lang.Long",
      "comment": " N'th term of Fibonacci sequence"
    },
    "parameters": [
      {
        "name": "n",
        "type": "java.lang.Integer",
        "comment": "id of fibonacci term to be returned"
      }
    ]
  },
  "testCases": [
    {
      "input": [
        "0"
      ],
      "output": 0
    },
    {
      "input": [
        "1"
      ],
      "output": 1
    },
    {
      "input": [
        "2"
      ],
      "output": 1
    },
    {
      "input": [
        "3"
      ],
      "output": 2
    },
    {
      "input": [
        "4"
      ],
      "output": 3
    },
    {
      "input": [
        "5"
      ],
      "output": 5
    },
    {
      "input": [
        "6"
      ],
      "output": 8
    },
    {
      "input": [
        "20"
      ],
      "output": 6765
    }
  ],
  "level": 1
}
"""

    private val FIB_SOURCE_CODE = """import java.util.*
import com.jalgoarena.type.*

class Solution {
    /**
     * @param n id of fibonacci term to be returned
     * @return  N'th term of Fibonacci sequence
     */
    fun fib(n: Int): Long {
        if (n < 2) return n.toLong()
        return fib(n - 1) + fib(n - 2)
    }
}
"""

    private val FIB_SOURCE_CODE_WITH_COMPILE_ERROR = """import java.util.*
import com.jalgoarena.type.*

class Solution {
    /**
     * @param n id of fibonacci term to be returned
     * @return  N'th term of Fibonacci sequence
     */
    fun fib(n: Int): Long {
    }
}
"""

    private val FIB_SOURCE_CODE_WITH_RUNTIME_ERROR = """import java.util.*
import com.jalgoarena.type.*

class Solution {
    /**
     * @param n id of fibonacci term to be returned
     * @return  N'th term of Fibonacci sequence
     */
    fun fib(n: Int): Long {
        throw Exception()
    }
}
"""
}
