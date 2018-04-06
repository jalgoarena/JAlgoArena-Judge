package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Submission(
        val sourceCode: String,
        val userId: String,
        val language: String,
        val submissionId: String,
        val problemId: String,
        val submissionTime: String,
        val token: String? = null
)
