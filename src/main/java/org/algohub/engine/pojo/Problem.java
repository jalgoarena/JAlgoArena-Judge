package org.algohub.engine.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


/**
 * Problem Java Object, corresponds to the problem JSON string.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {
    private final String id;
    private final String title;
    private final String description;
    @JsonProperty("time_limit_ms")
    private final long timeLimit;
    @JsonProperty("memory_limit_kb")
    private final int memoryLimit;
    private final Function function;
    @JsonProperty("test_cases")
    private final TestCase[] testCases;

    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator
    public Problem(@JsonProperty("id") final String id,
                   @JsonProperty("title") final String title,
                   @JsonProperty("description") final String description,
                   @JsonProperty("time_limit_ms") final long timeLimit,
                   @JsonProperty("memory_limit_kb") final int memoryLimit,
                   @JsonProperty("function") final Function function,
                   @JsonProperty("test_cases") final TestCase[] testCases) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.function = function;
        this.testCases = testCases;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public Function getFunction() {
        return function;
    }

    public TestCase[] getTestCases() {
        return testCases;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TestCase {
        private final ArrayNode input;
        private final JsonNode output;

        @JsonCreator
        public TestCase(@JsonProperty("input") final ArrayNode input,
                        @JsonProperty("output") final JsonNode output) {
            this.input = input;
            this.output = output;
        }

        public ArrayNode getInput() {
            return input;
        }

        public JsonNode getOutput() {
            return output;
        }
    }

    public Problem toPublicProblem() {
        return new Problem(id, title, description, timeLimit, memoryLimit, null, null);
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", timeLimit=" + timeLimit +
                ", memoryLimit=" + memoryLimit +
                '}';
    }
}
