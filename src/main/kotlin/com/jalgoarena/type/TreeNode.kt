package com.jalgoarena.type

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TreeNode(@JvmField @JsonProperty("data") var data: Int,
               @JvmField @JsonProperty("left") var left: TreeNode?,
               @JvmField @JsonProperty("right") var right: TreeNode?) {

    constructor(data: Int) : this(data, null, null)
}
