package com.jalgoarena.type

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TreeNode(@JvmField var data: Int,
               @JvmField var left: TreeNode?,
               @JvmField var right: TreeNode?) {

    constructor(data: Int) : this(data, null, null)
}
