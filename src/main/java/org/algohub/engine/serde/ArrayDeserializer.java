package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.TypeNode;

import java.lang.reflect.Array;

class ArrayDeserializer implements NodeDeserializer {

    private ImmutableMap<IntermediateType, NodeDeserializer> ARRAY_ITEM_DESERIALIZERS =
            ImmutableMap.<IntermediateType, NodeDeserializer>builder()
                    .put(IntermediateType.BOOL, new BoolNodeDeserializer())
                    .put(IntermediateType.INT, new IntNodeDeserializer())
                    .put(IntermediateType.LONG, new LongNodeDeserializer())
                    .put(IntermediateType.DOUBLE, new DoubleNodeDeserializer())
                    .build();

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final TypeNode elementType = type.getElementType().get();

        if (ARRAY_ITEM_DESERIALIZERS.containsKey(elementType.getValue())) {
            NodeDeserializer deserializer = ARRAY_ITEM_DESERIALIZERS.get(elementType.getValue());
            return deserializer.deserialize(type, jsonNode);
        }

        final Class innerestType = Deserializer.getArrayElementType(type);
        final int[] dimension = Deserializer.getAllDimensions(elements, type);
        final Object javaArray = Array.newInstance(innerestType, dimension);

        for (int i = 0; i < elements.size(); ++i) {
            Array.set(javaArray, i, Deserializer.fromJson(elementType, elements.get(i)));
        }

        return javaArray;
    }
}
