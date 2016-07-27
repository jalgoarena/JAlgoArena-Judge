package org.algohub.engine.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.algohub.engine.judge.StatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JudgeResult {
    @JsonProperty("status_code")
    private String statusCode;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("testcase_passed_count")
    private int testcasePassedCount;
    @JsonProperty("testcase_total_count")
    private int testcaseTotalCount;
    @JsonProperty("elapsed_time")
    private double elapsedTime; // milliseconds
    @JsonProperty("consumed_memory_kb")
    private long consumedMemory;  // kilobytes

    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator
    public JudgeResult(@JsonProperty("status_code") final String statusCode,
                       @JsonProperty("error_message") final String errorMessage,
                       @JsonProperty("testcase_passed_count") final int testcasePassedCount,
                       @JsonProperty("testcase_total_count") final int testcaseTotalCount,
                       @JsonProperty("elapsed_time") final double elapsedTime,
                       @JsonProperty("consumed_memory_kb") final long consumedMemory) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.testcasePassedCount = testcasePassedCount;
        this.testcaseTotalCount = testcaseTotalCount;
        this.elapsedTime = elapsedTime;
        this.consumedMemory = consumedMemory;
    }

    /**
     * Constructor.
     */
    public JudgeResult(final String compileErrorMsg) {
        this.statusCode = StatusCode.COMPILE_ERROR.toString();
        this.errorMessage = compileErrorMsg;
        this.testcasePassedCount = 0;
        this.testcaseTotalCount = 0;
        this.elapsedTime = 0;
        this.consumedMemory = 0;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getTestcasePassedCount() {
        return testcasePassedCount;
    }

    public int getTestcaseTotalCount() {
        return testcaseTotalCount;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getConsumedMemory() {
        return consumedMemory;
    }

    @Override
    public String toString() {
        return "JudgeResult{" +
                "statusCode=" + statusCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", testcasePassedCount=" + testcasePassedCount +
                ", testcaseTotalCount=" + testcaseTotalCount +
                ", elapsedTime=" + elapsedTime +
                ", consumedMemory(kb)=" + consumedMemory +
                '}';
    }

    public static JudgeResult accepted(int testCasesCount, double usedTimeInMs, long usedMemoryInKb) {
        return new JudgeResult(
                StatusCode.ACCEPTED.toString(),
                null,
                testCasesCount,
                testCasesCount,
                usedTimeInMs,
                usedMemoryInKb
        );
    }

    public static JudgeResult timeLimitExceeded(int testCasesCount, double usedTimeInMs, long usedMemoryInKb) {
        return new JudgeResult(
                StatusCode.TIME_LIMIT_EXCEEDED.toString(),
                null,
                testCasesCount,
                testCasesCount,
                usedTimeInMs,
                usedMemoryInKb
        );
    }

    public static JudgeResult memoryLimitExceeded(int testCasesCount, double usedTimeInMs, long usedMemoryInKb) {
        return new JudgeResult(
                StatusCode.MEMORY_LIMIT_EXCEEDED.toString(),
                null,
                testCasesCount,
                testCasesCount,
                usedTimeInMs,
                usedMemoryInKb
        );
    }

    public static JudgeResult wrongAnswer(int failedTestCases, int testCasesCount) {
        return new JudgeResult(
                StatusCode.WRONG_ANSWER.toString(),
                null,
                failedTestCases,
                testCasesCount,
                -1,
                -1
        );
    }
}
