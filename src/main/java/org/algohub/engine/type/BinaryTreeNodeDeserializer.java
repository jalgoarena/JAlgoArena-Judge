package org.algohub.engine.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

class BinaryTreeNodeDeserializer implements NodeDeserializer {

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final BinaryTreeNode<Object> javaBinaryTree = new BinaryTreeNode<>();

        for (final JsonNode e : elements) {
            javaBinaryTree.add(Deserializer.fromJson(type.getElementType(), e));
        }

        return javaBinaryTree;
    }
}
