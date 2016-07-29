package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.algohub.engine.type.TypeNode;

class DoubleNodeDeserializer implements NodeDeserializer {
    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final TypeNode elementType = type.getElementType();

        final double[] javaArray = new double[elements.size()];
        for (int i = 0; i < elements.size(); ++i) {
            javaArray[i] = (Double) Deserializer.fromJson(elementType, elements.get(i));
        }
        return javaArray;
    }
}
