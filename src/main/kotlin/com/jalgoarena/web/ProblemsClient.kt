package com.jalgoarena.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.data.DataRepository
import com.jalgoarena.domain.Problem
import com.netflix.discovery.EurekaClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class ProblemsClient(
        @Inject val objectMapper: ObjectMapper,
        @Inject val discoveryClient: EurekaClient
) : DataRepository<Problem> {

    private val LOG = LoggerFactory.getLogger(this.javaClass)

    private fun problemsServiceUrl(): String =
            discoveryClient.getNextServerFromEureka("jalgoarena-problems", false).homePageUrl

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
