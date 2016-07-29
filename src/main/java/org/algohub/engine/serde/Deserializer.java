package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import org.algohub.engine.type.BinaryTreeNode;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.LinkedListNode;
import org.algohub.engine.type.TypeNode;

import java.util.*;

public interface Deserializer {
    ImmutableMap<IntermediateType, Class> JAVA_CLASS_MAP =
            ImmutableMap.<IntermediateType, Class>builder().put(IntermediateType.BOOL, boolean.class)
                    .put(IntermediateType.STRING, String.class).put(IntermediateType.DOUBLE, double.class)
                    .put(IntermediateType.INT, int.class).put(IntermediateType.LONG, long.class)
                    .put(IntermediateType.LIST, ArrayList.class).put(IntermediateType.SET, HashSet.class)
                    .put(IntermediateType.MAP, HashMap.class)
                    .put(IntermediateType.LINKED_LIST_NODE, LinkedListNode.class)
                    .put(IntermediateType.BINARY_TREE_NODE, BinaryTreeNode.class).build();

    ImmutableMap<IntermediateType, NodeDeserializer> CONTAINER_DESERIALIZERS =
            ImmutableMap.<IntermediateType, NodeDeserializer>builder()
                    .put(IntermediateType.ARRAY, new ArrayDeserializer())
                    .put(IntermediateType.LIST, new ListDeserializer())
                    .put(IntermediateType.SET, new SetDeserializer())
                    .put(IntermediateType.MAP, new MapDeserializer())
                    .put(IntermediateType.LINKED_LIST_NODE, new LinkedListNodeDeserializer())
                    .put(IntermediateType.BINARY_TREE_NODE, new BinaryTreeNodeDeserializer())
                    .build();


    static Class getArrayElementType(final TypeNode typeNode) {
        TypeNode node = typeNode;
        while (node.getValue() == IntermediateType.ARRAY) {
            node = node.getElementType();
        }
        return JAVA_CLASS_MAP.get(node.getValue());
    }

    static int[] getAllDimensions(final ArrayNode arrayNode, final TypeNode typeNode) {
        final ArrayList<Integer> list = new ArrayList<>();

        JsonNode cur = arrayNode;
        TypeNode currentType = typeNode;
        while (cur.isArray() && currentType.getValue() == IntermediateType.ARRAY) {
            list.add(cur.size());
            cur = cur.get(0);
            currentType = currentType.getElementType();
        }
        return Ints.toArray(list);
    }


    /**
     * Convert primitive values to JsonNode.
     */
    static Object jsonToJavaPrimitiveNew(final TypeNode type, final JsonNode jsonNode) {
        final Object object;
        // for BinaryTreeNode
        if (jsonNode.isNull()) {
            return null;
        }

        switch (type.getValue()) {
            case BOOL:
                object = jsonNode.asBoolean();
                break;
            case STRING:
                object = jsonNode.asText();
                break;
            case DOUBLE:
                object = jsonNode.asDouble();
                break;
            case INT:
                object = jsonNode.asInt();
                break;
            case LONG:
                object = jsonNode.asLong();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized primitive type: " + type);
        }
        return object;
    }

    // Post order
    static Object fromJson(final TypeNode type, final JsonNode jsonNode) {
        if (!type.isContainer()) {
            return jsonToJavaPrimitiveNew(type, jsonNode);
        }

        if (!CONTAINER_DESERIALIZERS.containsKey(type.getValue())) {
            throw new IllegalArgumentException("Unrecognized collection type: " + type.getValue());
        }

        NodeDeserializer nodeDeserializer = CONTAINER_DESERIALIZERS.get(type.getValue());
        return nodeDeserializer.deserialize(type, jsonNode);
    }
}
