package com.jalgoarena.web

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Problem
import com.netflix.appinfo.InstanceInfo
import com.netflix.discovery.EurekaClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.web.client.RestOperations

class ProblemsClientSpec {

    private val PROBLEMS_SERVICE_DUMMY_URL = "http://localhost:5002"
    private val PROBLEM_ID = "fib"

    private val discoveryClient = mock(EurekaClient::class.java)
    private val restTemplate = mock(RestOperations::class.java)

    private val problemsClient = ProblemsClient(discoveryClient, restTemplate)

    @Test
    fun delegates_get_problems_request_to_problems_service_endpoint() {
        givenDiscoveryService()

        given(restTemplate.getForObject(
                "$PROBLEMS_SERVICE_DUMMY_URL/problems",
                Array<Problem>::class.java)
        ).willReturn(arrayOf(PROBLEM))

        val problems = problemsClient.findAll()

        assertThat(problems).hasSize(1)
        assertThat(problems[0].id).isEqualTo(PROBLEM_ID)
    }

    @Test
    fun delegates_get_fib_problem_request_to_problems_service_endpoint() {
        givenDiscoveryService()

        given(restTemplate.getForObject(
                "$PROBLEMS_SERVICE_DUMMY_URL/problems/$PROBLEM_ID",
                Problem::class.java)
        ).willReturn(PROBLEM)

        val problem = problemsClient.find(PROBLEM_ID)

        assertThat(problem.id).isEqualTo(PROBLEM_ID)
    }

    private fun givenDiscoveryService() {
        val instanceInfo = mock(InstanceInfo::class.java)

        given(instanceInfo.homePageUrl).willReturn(PROBLEMS_SERVICE_DUMMY_URL)
        given(discoveryClient.getNextServerFromEureka("jalgoarena-problems", false)).willReturn(instanceInfo)
    }


    private val FIB_FUNCTION = Function("fib",
            Function.Return("java.lang.Integer",
                    "Fibonacci number"),
            listOf(Function.Parameter("input", "java.lang.Integer", "Input"))
    )

    private val PROBLEM = Problem(
            id = "fib",
            title = "Fibonacci",
            description = "dummy description",
            level = 3,
            timeLimit = 1,
            func = FIB_FUNCTION,
            skeletonCode = mapOf(
                    Pair("java", "dummy code"),
                    Pair("kotln", "kotlin dummy code")
            ),
            testCases = listOf(
                    Problem.TestCase(
                            ArrayNode(JsonNodeFactory.instance).add(1).add(2),
                            IntNode(3)
                    )
            )
    )
}
