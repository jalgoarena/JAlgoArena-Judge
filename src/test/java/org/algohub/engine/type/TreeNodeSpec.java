package org.algohub.engine.type;

import org.algohub.engine.ObjectMapperInstance;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TreeNodeSpec {

    @Test
    public void it_should_serialize_and_deserialize_single_tree_node() throws Exception {

        TreeNode treeNode = new TreeNode(3);
        assertSerializationFor(treeNode);
    }

    @Test
    public void it_should_serialize_and_deserialize_whole_tree() throws Exception {
        TreeNode root = new TreeNode(
                1,
                new TreeNode(
                        2,
                        new TreeNode(4),
                        new TreeNode(5)
                ),
                new TreeNode(
                        3,
                        null,
                        new TreeNode(7)
                )
        );
        assertSerializationFor(root);
    }

    @Test
    public void it_should_generate_string_with_whole_tree() throws Exception {
        TreeNode root = new TreeNode(
                1,
                new TreeNode(
                        2,
                        new TreeNode(4),
                        new TreeNode(5)
                ),
                new TreeNode(
                        3,
                        new TreeNode(6),
                        new TreeNode(7)
                )
        );
        String treeNodeAsString = ObjectMapperInstance.INSTANCE.writeValueAsString(root);

        assertThat(treeNodeAsString).isEqualTo("{\"data\":1,\"left\":{\"data\":2,\"left\":{\"data\":4},\"right\":{\"data\":5}},\"right\":{\"data\":3,\"left\":{\"data\":6},\"right\":{\"data\":7}}}");
    }

    @Test
    public void two_trees_with_same_nodes_in_same_order_are_equal() throws Exception {
        TreeNode root1 = new TreeNode(
                1,
                new TreeNode(
                        2,
                        new TreeNode(4),
                        new TreeNode(5)
                ),
                new TreeNode(
                        3,
                        new TreeNode(6),
                        new TreeNode(7)
                )
        );

        TreeNode root2 = new TreeNode(
                1,
                new TreeNode(
                        2,
                        new TreeNode(4),
                        new TreeNode(5)
                ),
                new TreeNode(
                        3,
                        new TreeNode(6),
                        new TreeNode(7)
                )
        );

        assertThat(root1).isEqualTo(root2);
    }

    private void assertSerializationFor(TreeNode root) throws java.io.IOException {
        String treeNodeAsString = ObjectMapperInstance.INSTANCE.writeValueAsString(root);
        TreeNode deserializedTreeNode = ObjectMapperInstance.INSTANCE.readValue(treeNodeAsString, TreeNode.class);

        assertThat(deserializedTreeNode).isEqualTo(root);
    }
}