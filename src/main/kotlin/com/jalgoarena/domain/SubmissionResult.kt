package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SubmissionResult(
        val sourceCode: String,
        val userId: String,
        val problemId: String,
        val submissionTime: String,
        val elapsedTime: Double,
        val statusCode: String,
        val language: String,
        var id: String? = null,
        val submissionId: String,
        val consumedMemory: Long,
        val errorMessage: String?,
        val passedTestCases: Int,
        val failedTestCases: Int,
        val token: String?
)
