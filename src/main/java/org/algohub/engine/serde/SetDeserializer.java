package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.algohub.engine.type.TypeNode;

import java.util.HashSet;
import java.util.Set;

class SetDeserializer implements NodeDeserializer {

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;

        final Set javaSet = new HashSet<>();
        for (final JsonNode e : elements) {
            javaSet.add(Deserializer.fromJson(type.getElementType(), e));
        }
        return javaSet;
    }
}
