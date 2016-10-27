package org.algohub.engine.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeNode {
    public int data;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(final int data) {
        this(data, null, null);
    }

    @JsonCreator
    public TreeNode(
            @JsonProperty("data") final int data,
            @JsonProperty("left") final TreeNode left,
            @JsonProperty("right") final TreeNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TreeNode treeNode = (TreeNode) o;

        if (data != treeNode.data)
            return false;
        if (left != null ? !left.equals(treeNode.left) : treeNode.left != null)
            return false;

        return right != null ? right.equals(treeNode.right) : treeNode.right == null;

    }

    @Override
    public int hashCode() {
        int result = data;
        result = 31 * result + (left != null ? left.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
