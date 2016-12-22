package org.algohub.engine.data

import okhttp3.OkHttpClient
import okhttp3.Request
import org.algohub.engine.ObjectMapperInstance
import org.algohub.engine.judge.Problem

class ProblemsRepository {
    fun find(problemId: String): Problem? {
        val request = Request.Builder()
                .url("${DATA_SERVICE_HOST}problems/$problemId")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemAsJson = response.body().string()
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

    companion object {
        private val DATA_SERVICE_HOST = "https://jalgoarena-data.herokuapp.com/"
        private val CLIENT = OkHttpClient()
    }
}
