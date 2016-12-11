package org.algohub.engine.type

import org.algohub.engine.ObjectMapperInstance
import org.junit.Test

import org.assertj.core.api.Assertions.assertThat

class TreeNodeSpec {

    @Test
    @Throws(Exception::class)
    fun it_should_serialize_and_deserialize_single_tree_node() {

        val treeNode = TreeNode(3)
        assertSerializationFor(treeNode)
    }

    @Test
    @Throws(Exception::class)
    fun it_should_serialize_and_deserialize_whole_tree() {
        val root = TreeNode(
                1,
                TreeNode(
                        2,
                        TreeNode(4),
                        TreeNode(5)
                ),
                TreeNode(
                        3,
                        null,
                        TreeNode(7)
                )
        )
        assertSerializationFor(root)
    }

    @Test
    @Throws(Exception::class)
    fun it_should_generate_string_with_whole_tree() {
        val root = TreeNode(
                1,
                TreeNode(
                        2,
                        TreeNode(4),
                        TreeNode(5)
                ),
                TreeNode(
                        3,
                        TreeNode(6),
                        TreeNode(7)
                )
        )
        val treeNodeAsString = ObjectMapperInstance.INSTANCE.writeValueAsString(root)

        assertThat(treeNodeAsString).isEqualTo("{\"data\":1,\"left\":{\"data\":2,\"left\":{\"data\":4},\"right\":{\"data\":5}},\"right\":{\"data\":3,\"left\":{\"data\":6},\"right\":{\"data\":7}}}")
    }

    @Test
    @Throws(Exception::class)
    fun two_trees_with_same_nodes_in_same_order_are_equal() {
        val root1 = TreeNode(
                1,
                TreeNode(
                        2,
                        TreeNode(4),
                        TreeNode(5)
                ),
                TreeNode(
                        3,
                        TreeNode(6),
                        TreeNode(7)
                )
        )

        val root2 = TreeNode(
                1,
                TreeNode(
                        2,
                        TreeNode(4),
                        TreeNode(5)
                ),
                TreeNode(
                        3,
                        TreeNode(6),
                        TreeNode(7)
                )
        )

        assertThat(root1).isEqualTo(root2)
        assertThat(root1.hashCode()).isEqualTo(root2.hashCode())
    }

    @Test
    @Throws(Exception::class)
    fun two_trees_with_same_nodes_in_different_order_are_not_equal() {
        val root1 = TreeNode(
                1,
                TreeNode(
                        2,
                        TreeNode(4),
                        TreeNode(5)
                ),
                TreeNode(
                        3,
                        TreeNode(6),
                        TreeNode(7)
                )
        )

        val root2 = TreeNode(
                1,
                TreeNode(
                        2,
                        TreeNode(6),
                        TreeNode(5)
                ),
                TreeNode(
                        3,
                        TreeNode(4),
                        TreeNode(7)
                )
        )

        assertThat(root1).isNotEqualTo(root2)
        assertThat(root1.hashCode()).isNotEqualTo(root2.hashCode())
    }

    @Throws(java.io.IOException::class)
    private fun assertSerializationFor(root: TreeNode) {
        val treeNodeAsString = ObjectMapperInstance.INSTANCE.writeValueAsString(root)
        val deserializedTreeNode = ObjectMapperInstance.INSTANCE.readValue(treeNodeAsString, TreeNode::class.java)

        assertThat(deserializedTreeNode).isEqualTo(root)
    }
}