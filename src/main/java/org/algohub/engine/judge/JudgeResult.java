package org.algohub.engine.judge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JudgeResult {
    @JsonProperty("status_code")
    private final String statusCode;
    @JsonProperty("error_message")
    private final String errorMessage;
    @JsonProperty("elapsed_time")
    private final double elapsedTime; // milliseconds
    @JsonProperty("consumed_memory")
    private final long consumedMemory;  // bytes
    @JsonProperty("testcase_results")
    private final List<Boolean> testCaseResults;

    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator
    public JudgeResult(@JsonProperty("status_code") final String statusCode,
                       @JsonProperty("error_message") final String errorMessage,
                       @JsonProperty("elapsed_time") final double elapsedTime,
                       @JsonProperty("consumed_memory") final long consumedMemory,
                       @JsonProperty("testcase_results") final List<Boolean> testCaseResults) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.elapsedTime = elapsedTime;
        this.consumedMemory = consumedMemory;
        this.testCaseResults = testCaseResults;
    }

    public JudgeResult(final String compileErrorMsg) {
        this.statusCode = StatusCode.COMPILE_ERROR.toString();
        this.errorMessage = compileErrorMsg;
        this.elapsedTime = 0;
        this.consumedMemory = 0;
        this.testCaseResults = Collections.emptyList();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public long getConsumedMemory() {
        return consumedMemory;
    }

    public List<Boolean> getTestCaseResults() {
        return testCaseResults;
    }

    @Override
    public String toString() {
        return "JudgeResult{" +
                "statusCode=" + statusCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", elapsedTime=" + elapsedTime +
                ", consumedMemory=" + consumedMemory +
                '}';
    }

    static JudgeResult accepted(int testCasesCount, double usedTimeInMs, long usedMemoryInKb) {

        return new JudgeResult(
                StatusCode.ACCEPTED.toString(),
                null,
                usedTimeInMs,
                usedMemoryInKb,
                allTestsPass(testCasesCount)
        );
    }

    static JudgeResult timeLimitExceeded() {
        return new JudgeResult(
                StatusCode.TIME_LIMIT_EXCEEDED.toString(),
                null,
                -1,
                -1,
                Collections.emptyList()
        );
    }

    static JudgeResult memoryLimitExceeded(int testCasesCount, long usedMemory) {
        return new JudgeResult(
                StatusCode.MEMORY_LIMIT_EXCEEDED.toString(),
                null,
                -1,
                usedMemory,
                allTestsPass(testCasesCount)
        );
    }

    static JudgeResult wrongAnswer(List<Boolean> results) {
        return new JudgeResult(
                StatusCode.WRONG_ANSWER.toString(),
                null,
                -1,
                -1,
                results
        );
    }

    static JudgeResult runtimeError(String errorMessage) {
        return new JudgeResult(
                StatusCode.RUNTIME_ERROR.toString(),
                errorMessage,
                -1,
                -1,
                Collections.emptyList()
        );
    }

    private static List<Boolean> allTestsPass(int testCasesCount) {
        Boolean[] results = new Boolean[testCasesCount];
        Arrays.fill(results, true);
        return Arrays.asList(results);
    }
}
