package org.algohub.engine.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.HashSet;
import java.util.Set;

class SetDeserializer implements NodeDeserializer {

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;

        final Set<Object> javaSet = new HashSet<>();
        for (final JsonNode e : elements) {
            javaSet.add(Deserializer.fromJson(type.getElementType(), e));
        }
        return javaSet;
    }
}
