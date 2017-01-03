package com.jalgoarena.judge

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
sealed class JudgeResult(@JsonProperty("status_code") val statusCode: String,
                         @JsonProperty("error_message") val errorMessage: String?, // milliseconds
                         @JsonProperty("elapsed_time") val elapsedTime: Double, // bytes
                         @JsonProperty("consumed_memory") val consumedMemory: Long,
                         @JsonProperty("testcase_results") val testCaseResults: List<Boolean>) {

    class Accepted(testCasesCount: Int, usedTimeInMs: Double, usedMemoryInKb: Long) : JudgeResult(
            StatusCode.ACCEPTED.toString(),
            null,
            usedTimeInMs,
            usedMemoryInKb,
            BooleanArray(testCasesCount) { true }.toList()
    )

    class CompileError(compileErrorMsg: String) : JudgeResult(
            StatusCode.COMPILE_ERROR.toString(),
            compileErrorMsg,
            -1.0,
            -1,
            emptyList()
    )

    class TimeLimitExceeded() : JudgeResult(
            StatusCode.TIME_LIMIT_EXCEEDED.toString(),
            null,
            -1.0,
            -1,
            emptyList<Boolean>()
    )

    class MemoryLimitExceeded(usedMemory: Long) : JudgeResult(
            StatusCode.MEMORY_LIMIT_EXCEEDED.toString(),
            null,
            -1.0,
            usedMemory,
            emptyList<Boolean>()
    )

    class WrongAnswer(results: List<Boolean>) : JudgeResult(
            StatusCode.WRONG_ANSWER.toString(),
            null,
            -1.0,
            -1,
            results
    )

    class RuntimeError(runtimeErrorMessage: String?) : JudgeResult(
            StatusCode.RUNTIME_ERROR.toString(),
            runtimeErrorMessage,
            -1.0,
            -1,
            emptyList<Boolean>()
    )
}
