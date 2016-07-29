package org.algohub.engine.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

class IntNodeDeserializer implements NodeDeserializer {
    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final TypeNode elementType = type.getElementType();

        final int[] javaArray = new int[elements.size()];
        for (int i = 0; i < elements.size(); ++i) {
            javaArray[i] = (Integer) Deserializer.fromJson(elementType, elements.get(i));
        }
        return javaArray;
    }
}
