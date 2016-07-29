package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.algohub.engine.type.TypeNode;

import java.lang.reflect.Array;

class ArrayDeserializer implements ContainerNodeDeserializer {

    @Override
    public Object deserialize(TypeNode type, JsonNode jsonNode) {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final TypeNode elementType = type.getElementType().get();
        switch (elementType.getValue()) {
            case BOOL: {
                final boolean[] javaArray = new boolean[elements.size()];
                for (int i = 0; i < elements.size(); ++i) {
                    javaArray[i] = (Boolean) Deserializer.fromJson(elementType, elements.get(i));
                }
                return javaArray;
            }
            case INT: {
                final int[] javaArray = new int[elements.size()];
                for (int i = 0; i < elements.size(); ++i) {
                    javaArray[i] = (Integer) Deserializer.fromJson(elementType, elements.get(i));
                }
                return javaArray;
            }
            case LONG: {
                final long[] javaArray = new long[elements.size()];
                for (int i = 0; i < elements.size(); ++i) {
                    javaArray[i] = (Long) Deserializer.fromJson(elementType, elements.get(i));
                }
                return javaArray;
            }
            case DOUBLE: {
                final double[] javaArray = new double[elements.size()];
                for (int i = 0; i < elements.size(); ++i) {
                    javaArray[i] = (Double) Deserializer.fromJson(elementType, elements.get(i));
                }
                return javaArray;
            }
            default: {
                final Class innerestType = Deserializer.getArrayElementType(type);
                final int[] dimension = Deserializer.getAllDimensions(elements, type);
                final Object javaArray = Array.newInstance(innerestType, dimension);
                for (int i = 0; i < elements.size(); ++i) {
                    Array.set(javaArray, i, Deserializer.fromJson(elementType, elements.get(i)));
                }
                return javaArray;
            }
        }
    }
}
