package org.algohub.engine.judge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {

    private final String id;
    private final String title;
    private final String description;
    @JsonProperty("time_limit")
    private final long timeLimit;
    @JsonProperty("memory_limit")
    private final int memoryLimit;
    private final Function function;
    @JsonProperty("test_cases")
    private final TestCase[] testCases;
    private final Example example;
    @JsonProperty("skeleton_code")
    private final String skeletonCode;

    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator
    public Problem(@JsonProperty("id") final String id,
                   @JsonProperty("title") final String title,
                   @JsonProperty("description") final String description,
                   @JsonProperty("time_limit") final long timeLimit,
                   @JsonProperty("memory_limit") final int memoryLimit,
                   @JsonProperty("function") final Function function,
                   @JsonProperty("test_cases") final TestCase[] testCases,
                   @JsonProperty("example") Example example,
                   @JsonProperty("source_code") String skeletonCode) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.function = function;
        this.testCases = testCases;
        this.example = example;
        this.skeletonCode = skeletonCode;
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

    public Example getExample() {
        return example;
    }

    public String getSkeletonCode() {
        return skeletonCode;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Example {
        private final String input;
        private final String output;

        @JsonCreator
        public Example(@JsonProperty("input") final String input,
                       @JsonProperty("output") final String output) {
            this.input = input;
            this.output = output;
        }

        public String getInput() {
            return input;
        }

        public String getOutput() {
            return output;
        }
    }

    public Problem problemWithoutFunctionAndTestCases(String skeletonCode) {
        return new Problem(id, title, description, timeLimit, memoryLimit, null, null, example, skeletonCode);
    }
}
