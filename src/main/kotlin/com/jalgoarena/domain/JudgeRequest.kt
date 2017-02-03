package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class JudgeRequest(
        val sourceCode: String,
        val userId: String,
        val language: String)
