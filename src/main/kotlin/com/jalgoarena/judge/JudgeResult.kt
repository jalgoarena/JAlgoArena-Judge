package com.jalgoarena.judge

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class JudgeResult(@JsonProperty("status_code") val statusCode: String,
                       @JsonProperty("error_message") val errorMessage: String?, // milliseconds
                       @JsonProperty("elapsed_time") val elapsedTime: Double, // bytes
                       @JsonProperty("consumed_memory") val consumedMemory: Long,
                       @JsonProperty("testcase_results") val testCaseResults: List<Boolean>) {

    companion object {

        @JvmStatic
        fun compileError(compileErrorMsg: String): JudgeResult {
            return JudgeResult(
                    StatusCode.COMPILE_ERROR.toString(),
                    compileErrorMsg,
                    0.0,
                    0,
                    emptyList()
            )
        }

        @JvmStatic
        fun accepted(testCasesCount: Int, usedTimeInMs: Double, usedMemoryInKb: Long): JudgeResult {

            return JudgeResult(
                    StatusCode.ACCEPTED.toString(),
                    null,
                    usedTimeInMs,
                    usedMemoryInKb,
                    allTestsPass(testCasesCount)
            )
        }

        @JvmStatic
        fun timeLimitExceeded(): JudgeResult {
            return JudgeResult(
                    StatusCode.TIME_LIMIT_EXCEEDED.toString(),
                    null,
                    -1.0,
                    -1,
                    emptyList<Boolean>()
            )
        }

        @JvmStatic
        fun memoryLimitExceeded(testCasesCount: Int, usedMemory: Long): JudgeResult {
            return JudgeResult(
                    StatusCode.MEMORY_LIMIT_EXCEEDED.toString(),
                    null,
                    -1.0,
                    usedMemory,
                    allTestsPass(testCasesCount)
            )
        }

        @JvmStatic
        fun wrongAnswer(results: List<Boolean>): JudgeResult {
            return JudgeResult(
                    StatusCode.WRONG_ANSWER.toString(),
                    null,
                    -1.0,
                    -1,
                    results
            )
        }

        @JvmStatic
        fun runtimeError(errorMessage: String?): JudgeResult {
            return JudgeResult(
                    StatusCode.RUNTIME_ERROR.toString(),
                    errorMessage,
                    -1.0,
                    -1,
                    emptyList<Boolean>()
            )
        }

        private fun allTestsPass(testCasesCount: Int): List<Boolean> {
            val results = arrayOfNulls<Boolean>(testCasesCount)
            Arrays.fill(results, true)
            return Arrays.asList<Boolean>(*results)
        }
    }
}
