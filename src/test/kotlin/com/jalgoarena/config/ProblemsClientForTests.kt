package com.jalgoarena.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.data.DataRepository
import com.jalgoarena.judge.Problem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ProblemsClientForTests : DataRepository<Problem> {

    private val LOG = LoggerFactory.getLogger(this.javaClass)
    private val objectMapper = jacksonObjectMapper()

    private fun problemsServiceUrl() = "https://jalgoarena-api.herokuapp.com/problems/api"
    private val CLIENT = OkHttpClient()

    override fun find(id: String): Problem? {
        val request = Request.Builder()
                .url("${problemsServiceUrl()}/problems/$id")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemAsJson = response.body().string()

        if (problemAsJson.isEmpty() || problemAsJson.contains("com.netflix.zuul.exception.ZuulException")) {
            LOG.error("There is error in querying for $id problem. Response: $problemAsJson")
            return null
        }

        return objectMapper.readValue(problemAsJson, Problem::class.java)
    }

    override fun findAll(): Array<Problem> {
        val request = Request.Builder()
                .url("${problemsServiceUrl()}/problems")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemsAsJsonArray = response.body().string()

        return objectMapper.readValue(problemsAsJsonArray, Array<Problem>::class.java)
    }
}
