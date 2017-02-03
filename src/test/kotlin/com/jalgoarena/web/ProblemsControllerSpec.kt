package com.jalgoarena.web

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.codegeneration.JavaCodeGenerator
import com.jalgoarena.codegeneration.KotlinCodeGenerator
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.inject.Inject

@RunWith(SpringRunner::class)
@WebMvcTest(ProblemsController::class)
@ContextConfiguration(classes = arrayOf(ProblemsControllerSpec.ControllerTestConfiguration::class))
class ProblemsControllerSpec {

    @Inject
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var problemsClient: ProblemsRepository

    @Test
    fun get_problems_returns_200_and_all_available_problems() {
        given(problemsClient.findAll()).willReturn(listOf(PROBLEM))

        mockMvc.perform(get("/problems")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$[0].id", `is`("fib")))
                .andExpect(jsonPath("$[0].title", `is`("Fibonacci")))
                .andExpect(jsonPath("$", hasSize<ArrayNode>(1)))
    }

    @Test
    fun get_problem_returns_200_and_a_problem() {
        given(problemsClient.find("fib")).willReturn(PROBLEM)

        mockMvc.perform(get("/problems/fib")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id", `is`("fib")))
                .andExpect(jsonPath("$.title", `is`("Fibonacci")))
    }

    @TestConfiguration
    open class ControllerTestConfiguration {

        @Bean
        open fun kotlinCodeGenerator() = KotlinCodeGenerator()
        @Bean
        open fun javaCodeGenerator() = JavaCodeGenerator()

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

    private val PROBLEM = jacksonObjectMapper().readValue(PROBLEM_AS_JSON, Problem::class.java)
}
