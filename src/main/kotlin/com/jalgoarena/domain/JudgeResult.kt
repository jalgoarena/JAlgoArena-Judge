package com.jalgoarena.domain

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
sealed class JudgeResult(val statusCode: String,
                         val errorMessage: String?,
                         val elapsedTime: Double, // milliseconds
                         val consumedMemory: Long, // bytes
                         val testcaseResults: List<Boolean>) {

    class Accepted(testCasesCount: Int, usedTimeInMs: Double, usedMemoryInBytes: Long) : JudgeResult(
            StatusCode.ACCEPTED.toString(),
            null,
            usedTimeInMs,
            usedMemoryInBytes,
            BooleanArray(testCasesCount) { true }.toList()
    )

    class CompileError(compileErrorMsg: String) : JudgeResult(
            StatusCode.COMPILE_ERROR.toString(),
            compileErrorMsg,
            -1.0,
            -1,
            emptyList()
    )

    class TimeLimitExceeded : JudgeResult(
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
