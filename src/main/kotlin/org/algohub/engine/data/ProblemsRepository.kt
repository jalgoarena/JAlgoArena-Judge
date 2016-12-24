package org.algohub.engine.data

import okhttp3.OkHttpClient
import okhttp3.Request
import org.algohub.engine.ObjectMapperInstance
import org.algohub.engine.judge.Problem
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ProblemsRepository {

    private val LOG = LoggerFactory.getLogger(this.javaClass)
    private val DATA_SERVICE_HOST = "https://jalgoarena-api.herokuapp.com/problems/"
    private val CLIENT = OkHttpClient()

    fun find(problemId: String): Problem? {
        val request = Request.Builder()
                .url("${DATA_SERVICE_HOST}problems/$problemId")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemAsJson = response.body().string()

        if (problemAsJson.isEmpty() || problemAsJson.contains("com.netflix.zuul.exception.ZuulException")) {
            LOG.error("There is error in querying for $problemId problem. Response: $problemAsJson")
            return null
        }

        return ObjectMapperInstance.INSTANCE.readValue(problemAsJson, Problem::class.java)
    }

    fun findAll(): Array<Problem> {
        val request = Request.Builder()
                .url(DATA_SERVICE_HOST + "problems")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemsAsJsonArray = response.body().string()

        return ObjectMapperInstance.INSTANCE.readValue(problemsAsJsonArray, Array<Problem>::class.java)
    }
}
