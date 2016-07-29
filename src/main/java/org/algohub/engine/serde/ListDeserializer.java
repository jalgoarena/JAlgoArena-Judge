package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.algohub.engine.type.TypeNode;

import java.util.ArrayList;
import java.util.List;

class ListDeserializer implements NodeDeserializer {

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;

        final List javaList = new ArrayList<>();
        for (final JsonNode e : elements) {
            javaList.add(Deserializer.fromJson(type.getElementType().get(), e));
        }
        return javaList;
    }
}
