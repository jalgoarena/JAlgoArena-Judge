package com.jalgoarena.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.codegeneration.JavaCodeGenerator
import com.jalgoarena.codegeneration.KotlinCodeGenerator
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import javax.inject.Inject

@RunWith(SpringRunner::class)
@WebFluxTest(ProblemsController::class)
@ContextConfiguration(classes = [(ProblemsControllerSpec.ControllerTestConfiguration::class)])
class ProblemsControllerSpec {

    @Inject
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var problemsClient: ProblemsRepository

    @Test
    fun get_problems_returns_200_and_all_available_problems() {
        given(problemsClient.findAll()).willReturn(listOf(PROBLEM))

        webTestClient.get()
                .uri("/problems")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$").isArray
                .jsonPath("$[0].id").isEqualTo("fib")
                .jsonPath("$[0].title").isEqualTo("Fibonacci")
    }

    @Test
    fun get_problem_returns_200_and_a_problem() {
        given(problemsClient.find("fib")).willReturn(PROBLEM)

        webTestClient.get()
                .uri("/problems/fib")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo("fib")
                .jsonPath("$.title").isEqualTo("Fibonacci")
    }

    @TestConfiguration
    open class ControllerTestConfiguration {

        @Bean
        open fun kotlinCodeGenerator() = KotlinCodeGenerator()
        @Bean
        open fun javaCodeGenerator() = JavaCodeGenerator()

    }

    companion object {
        //language=JSON
        private const val PROBLEM_AS_JSON = """{
      "id": "fib",
      "title": "Fibonacci",
      "description": "Write the `fib` function to return the N'th term.\r\nWe start counting from:\r\n* fib(0) = 0\r\n* fib(1) = 1.\r\n\r\n### Examples\r\n\r\n* `0` -> `0`\r\n* `6` -> `8`",
      "timeLimit": 1,
      "func": {
        "name": "fib",
        "returnStatement": {
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
}
