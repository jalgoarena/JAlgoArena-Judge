package org.algohub.engine.judge

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class Function
/**
 * Since this class is immutable, need to provide a method for Jackson.
 */
@JsonCreator
constructor(@JsonProperty("name") val name: String,
            /**
             * Return metadata.
             */
            @JsonProperty("return")
            val returnStatement: Function.Return,
            /**
             * Parameters' metadata.
             */
            @JsonProperty("parameters") val parameters: Array<Function.Parameter>) {

    /**
     * Return type.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Return
    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator
    constructor(
            /**
             * Return data type.
             */
            @JsonProperty("type") val type: String,
            /**
             * Comment of returned value.
             */
            @JsonProperty("comment") val comment: String)

    /**
     * Function parameters' metadata.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Parameter
    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator
    constructor(
            /**
             * Parameter name.
             */
            @JsonProperty("name") val name: String,
            /**
             * Parameter type.
             */
            @JsonProperty("type") val type: String,
            /**
             * Parameter comment.
             */
            @JsonProperty("comment") val comment: String)
}
