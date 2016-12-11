package org.algohub.engine.judge

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class JudgeResult {
    @JsonProperty("status_code")
    val statusCode: String
    @JsonProperty("error_message")
    val errorMessage: String?
    @JsonProperty("elapsed_time")
    val elapsedTime: Double // milliseconds
    @JsonProperty("consumed_memory")
    val consumedMemory: Long  // bytes
    @JsonProperty("testcase_results")
    val testCaseResults: List<Boolean>

    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator
    constructor(@JsonProperty("status_code") statusCode: String,
                @JsonProperty("error_message") errorMessage: String?,
                @JsonProperty("elapsed_time") elapsedTime: Double,
                @JsonProperty("consumed_memory") consumedMemory: Long,
                @JsonProperty("testcase_results") testCaseResults: List<Boolean>) {
        this.statusCode = statusCode
        this.errorMessage = errorMessage
        this.elapsedTime = elapsedTime
        this.consumedMemory = consumedMemory
        this.testCaseResults = testCaseResults
    }

    constructor(compileErrorMsg: String) {
        this.statusCode = StatusCode.COMPILE_ERROR.toString()
        this.errorMessage = compileErrorMsg
        this.elapsedTime = 0.0
        this.consumedMemory = 0
        this.testCaseResults = emptyList<Boolean>()
    }

    override fun toString() =
            "JudgeResult{statusCode=$statusCode, errorMessage='$errorMessage', elapsedTime=$elapsedTime, consumedMemory=$consumedMemory}"

    companion object {

        @JvmStatic
        internal fun accepted(testCasesCount: Int, usedTimeInMs: Double, usedMemoryInKb: Long): JudgeResult {

            return JudgeResult(
                    StatusCode.ACCEPTED.toString(),
                    null,
                    usedTimeInMs,
                    usedMemoryInKb,
                    allTestsPass(testCasesCount)
            )
        }

        @JvmStatic
        internal fun timeLimitExceeded(): JudgeResult {
            return JudgeResult(
                    StatusCode.TIME_LIMIT_EXCEEDED.toString(),
                    null,
                    -1.0,
                    -1,
                    emptyList<Boolean>()
            )
        }

        @JvmStatic
        internal fun memoryLimitExceeded(testCasesCount: Int, usedMemory: Long): JudgeResult {
            return JudgeResult(
                    StatusCode.MEMORY_LIMIT_EXCEEDED.toString(),
                    null,
                    -1.0,
                    usedMemory,
                    allTestsPass(testCasesCount)
            )
        }

        @JvmStatic
        internal fun wrongAnswer(results: List<Boolean>): JudgeResult {
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
