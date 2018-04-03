package com.jalgoarena.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.data.ProblemsRepository
import com.jalgoarena.domain.Problem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Repository

@Repository
open class ProblemsClientForTests : ProblemsRepository {

    private val objectMapper = jacksonObjectMapper()

    private fun problemsServiceUrl() = "https://jalgoarena-api.herokuapp.com/problems/api"
    private val CLIENT = OkHttpClient()

    override fun find(id: String): Problem {
        val request = Request.Builder()
                .url("${problemsServiceUrl()}/problems/$id")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemAsJson = response.body().string()

        return objectMapper.readValue(problemAsJson, Problem::class.java)
    }

    override fun findAll(): List<Problem> {
        val request = Request.Builder()
                .url("${problemsServiceUrl()}/problems")
                .build()

        val response = CLIENT.newCall(request).execute()
        val problemsAsJsonArray = response.body().string()

        return objectMapper.readValue(problemsAsJsonArray, Array<Problem>::class.java).asList()
    }
}
