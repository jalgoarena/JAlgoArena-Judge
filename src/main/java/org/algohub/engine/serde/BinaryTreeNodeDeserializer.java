package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.algohub.engine.type.BinaryTreeNode;
import org.algohub.engine.type.TypeNode;

class BinaryTreeNodeDeserializer implements NodeDeserializer {

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final BinaryTreeNode javaBinaryTree = new BinaryTreeNode<>();

        for (final JsonNode e : elements) {
            javaBinaryTree.add(Deserializer.fromJson(type.getElementType().get(), e));
        }

        return javaBinaryTree;
    }
}
