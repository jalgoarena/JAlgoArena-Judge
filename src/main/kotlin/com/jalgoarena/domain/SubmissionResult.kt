package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class SubmissionResult(
        val sourceCode: String,
        val userId: String,
        val problemId: String,
        val submissionTime: LocalDateTime,
        val elapsedTime: Double,
        val statusCode: String,
        var id: Int,
        val submissionId: String,
        val consumedMemory: Long,
        val errorMessage: String?,
        val passedTestCases: Int,
        val failedTestCases: Int,
        val token: String
)
