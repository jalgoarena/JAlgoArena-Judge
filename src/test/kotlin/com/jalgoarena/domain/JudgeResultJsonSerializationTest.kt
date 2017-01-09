package com.jalgoarena.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.boot.test.json.JacksonTester

class JudgeResultJsonSerializationTest {

    private lateinit var json: JacksonTester<JudgeResult>

    @Before
    fun setup() {
        val objectMapper = jacksonObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }

    @Test
    fun serializes_accepted_judge_result() {
        assertThat(json.write(ACCEPTED_JUDGE_RESULT))
                .isEqualToJson("judge-result.json")
    }

    @Test
    fun serializes_error_judge_result() {
        assertThat(json.write(COMPILATION_ERROR_JUDGE_RESULT))
                .extractingJsonPathStringValue("@.errorMessage")
                .isEqualTo("Sample Error")
    }

    @Test
    fun accepted_judge_results_has_filled_elapsed_time() {
        assertThat(ACCEPTED_JUDGE_RESULT.elapsedTime).isEqualTo(0.24)
    }

    @Test
    fun accepted_judge_results_has_filled_consumer_memory() {
        assertThat(ACCEPTED_JUDGE_RESULT.consumedMemory).isEqualTo(0)
    }

    @Test
    fun accepted_judge_results_has_filled_test_case_results() {
        assertThat(ACCEPTED_JUDGE_RESULT.testcaseResults).isEqualTo(
                listOf(true, true, true, true, true)
        )
    }

    private val ACCEPTED_JUDGE_RESULT = JudgeResult.Accepted(5, 0.24, 0)
    private val COMPILATION_ERROR_JUDGE_RESULT = JudgeResult.CompileError("Sample Error")
}
