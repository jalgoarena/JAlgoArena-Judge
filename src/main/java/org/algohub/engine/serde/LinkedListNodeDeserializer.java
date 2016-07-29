package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.algohub.engine.type.LinkedListNode;
import org.algohub.engine.type.TypeNode;

class LinkedListNodeDeserializer implements NodeDeserializer {

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final LinkedListNode javaLinkedList = new LinkedListNode<>();

        for (final JsonNode e : elements) {
            javaLinkedList.add(Deserializer.fromJson(type.getElementType(), e));
        }

        return javaLinkedList;
    }
}
