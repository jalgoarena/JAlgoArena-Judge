package com.jalgoarena.type

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class TreeNode(@JvmField @JsonProperty("data") var data: Int,
               @JvmField @JsonProperty("left") var left: TreeNode?,
               @JvmField @JsonProperty("right") var right: TreeNode?) {

    constructor(data: Int) : this(data, null, null)

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null || javaClass != other.javaClass)
            return false

        val treeNode = other as TreeNode?

        if (data != treeNode!!.data)
            return false
        if (if (left != null) left != treeNode.left else treeNode.left != null)
            return false

        return if (right != null) right == treeNode.right else treeNode.right == null

    }

    override fun hashCode(): Int {
        var result = data
        result = 31 * result + if (left != null) left!!.hashCode() else 0
        result = 31 * result + if (right != null) right!!.hashCode() else 0
        return result
    }
}
