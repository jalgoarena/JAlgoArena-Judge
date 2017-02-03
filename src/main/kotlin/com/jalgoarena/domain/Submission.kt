package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Submission(
        val problemId: String,
        val elapsedTime: Double,
        val sourceCode: String,
        val statusCode: String,
        val userId: String,
        val language: String,
        var id: String? = null
)
