package org.algohub.engine.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

class LinkedListNodeDeserializer implements NodeDeserializer {

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final LinkedListNode<Object> javaLinkedList = new LinkedListNode<>();

        for (final JsonNode e : elements) {
            javaLinkedList.add(Deserializer.fromJson(type.getElementType(), e));
        }

        return javaLinkedList;
    }
}
