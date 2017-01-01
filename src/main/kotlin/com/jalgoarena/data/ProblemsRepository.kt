package com.jalgoarena.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApiGatewayConfiguration
import com.jalgoarena.judge.Problem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import javax.inject.Inject

@Repository
class ProblemsRepository(
        @Inject val objectMapper: ObjectMapper,
        @Inject val apiGatewayConfiguration: ApiGatewayConfiguration
) {

    private val LOG = LoggerFactory.getLogger(this.javaClass)

    private fun problemsServiceUrl() = "${apiGatewayConfiguration.apiGatewayUrl}/problems/api"
    private val CLIENT = OkHttpClient()

    fun find(problemId: String): Problem? {
        val request = Request.Builder()
                .url("${problemsServiceUrl()}/problems/$problemId")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemAsJson = response.body().string()

        if (problemAsJson.isEmpty() || problemAsJson.contains("com.netflix.zuul.exception.ZuulException")) {
            LOG.error("There is error in querying for $problemId problem. Response: $problemAsJson")
            return null
        }

        return objectMapper.readValue(problemAsJson, Problem::class.java)
    }

    fun findAll(): Array<Problem> {
        val request = Request.Builder()
                .url("${problemsServiceUrl()}/problems")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemsAsJsonArray = response.body().string()

        return objectMapper.readValue(problemsAsJsonArray, Array<Problem>::class.java)
    }
}
