package org.algohub.engine.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;

import java.lang.reflect.Array;
import java.util.*;

class Deserializer {
    private static final ImmutableMap<IntermediateType, Class> JAVA_CLASS_MAP =
            ImmutableMap.<IntermediateType, Class>builder().put(IntermediateType.BOOL, boolean.class)
                    .put(IntermediateType.STRING, String.class).put(IntermediateType.DOUBLE, double.class)
                    .put(IntermediateType.INT, int.class).put(IntermediateType.LONG, long.class)
                    .put(IntermediateType.LIST, ArrayList.class).put(IntermediateType.SET, HashSet.class)
                    .put(IntermediateType.MAP, HashMap.class)
                    .put(IntermediateType.LINKED_LIST_NODE, LinkedListNode.class).build();

    private static final ImmutableMap<IntermediateType, NodeDeserializer> CONTAINER_DESERIALIZERS =
            ImmutableMap.<IntermediateType, NodeDeserializer>builder()
                    .put(IntermediateType.ARRAY, new ArrayDeserializer())
                    .put(IntermediateType.LIST, new ListDeserializer())
                    .put(IntermediateType.SET, new SetDeserializer())
                    .put(IntermediateType.MAP, new MapDeserializer())
                    .put(IntermediateType.LINKED_LIST_NODE, new LinkedListNodeDeserializer())
                    .build();


    /**
     * Convert primitive values to JsonNode.
     */
    private static Object jsonToJavaPrimitiveNew(final TypeNode type, final JsonNode jsonNode) {
        final Object object;

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

    @FunctionalInterface
    interface NodeDeserializer {
        Object deserialize(final TypeNode type, final JsonNode jsonNode);
    }

    private static class ArrayDeserializer implements NodeDeserializer {

        private static final ImmutableMap<IntermediateType, NodeDeserializer> ARRAY_ITEM_DESERIALIZERS =
                ImmutableMap.<IntermediateType, NodeDeserializer>builder()
                        .put(IntermediateType.BOOL, new BoolNodeDeserializer())
                        .put(IntermediateType.INT, new IntNodeDeserializer())
                        .put(IntermediateType.LONG, new LongNodeDeserializer())
                        .put(IntermediateType.DOUBLE, new DoubleNodeDeserializer())
                        .build();

        private static Class getArrayElementType(final TypeNode typeNode) {
            TypeNode node = typeNode;
            while (node.getValue() == IntermediateType.ARRAY) {
                node = node.getElementType();
            }
            return JAVA_CLASS_MAP.get(node.getValue());
        }

        private static int[] getAllDimensions(final ArrayNode arrayNode, final TypeNode typeNode) {
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

        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final ArrayNode elements = (ArrayNode) jsonNode;
            final TypeNode elementType = type.getElementType();

            if (ARRAY_ITEM_DESERIALIZERS.containsKey(elementType.getValue())) {
                NodeDeserializer deserializer = ARRAY_ITEM_DESERIALIZERS.get(elementType.getValue());
                return deserializer.deserialize(type, jsonNode);
            }

            final Class innerestType = getArrayElementType(type);
            final int[] dimension = getAllDimensions(elements, type);
            final Object javaArray = Array.newInstance(innerestType, dimension);

            for (int i = 0; i < elements.size(); ++i) {
                Array.set(javaArray, i, fromJson(elementType, elements.get(i)));
            }

            return javaArray;
        }
    }

    private static class SetDeserializer implements NodeDeserializer {

        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final ArrayNode elements = (ArrayNode) jsonNode;

            final Set<Object> javaSet = new HashSet<>();
            for (final JsonNode e : elements) {
                javaSet.add(fromJson(type.getElementType(), e));
            }
            return javaSet;
        }
    }

    private static class ListDeserializer implements NodeDeserializer {

        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final ArrayNode elements = (ArrayNode) jsonNode;

            final List<Object> javaList = new ArrayList<>();
            for (final JsonNode e : elements) {
                javaList.add(fromJson(type.getElementType(), e));
            }
            return javaList;
        }
    }

    private static class BoolNodeDeserializer implements NodeDeserializer {

        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final ArrayNode elements = (ArrayNode) jsonNode;
            final TypeNode elementType = type.getElementType();

            final boolean[] javaArray = new boolean[elements.size()];
            for (int i = 0; i < elements.size(); ++i) {
                javaArray[i] = (Boolean) fromJson(elementType, elements.get(i));
            }
            return javaArray;
        }
    }

    private static class DoubleNodeDeserializer implements NodeDeserializer {
        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final ArrayNode elements = (ArrayNode) jsonNode;
            final TypeNode elementType = type.getElementType();

            final double[] javaArray = new double[elements.size()];
            for (int i = 0; i < elements.size(); ++i) {
                javaArray[i] = (Double) fromJson(elementType, elements.get(i));
            }
            return javaArray;
        }
    }

    private static class IntNodeDeserializer implements NodeDeserializer {
        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final ArrayNode elements = (ArrayNode) jsonNode;
            final TypeNode elementType = type.getElementType();

            final int[] javaArray = new int[elements.size()];
            for (int i = 0; i < elements.size(); ++i) {
                javaArray[i] = (Integer) fromJson(elementType, elements.get(i));
            }
            return javaArray;
        }
    }

    private static class LinkedListNodeDeserializer implements NodeDeserializer {

        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final ArrayNode elements = (ArrayNode) jsonNode;
            final LinkedListNode<Object> javaLinkedList = new LinkedListNode<>();

            for (final JsonNode e : elements) {
                javaLinkedList.add(fromJson(type.getElementType(), e));
            }

            return javaLinkedList;
        }
    }

    private static class LongNodeDeserializer implements NodeDeserializer {

        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final ArrayNode elements = (ArrayNode) jsonNode;
            final TypeNode elementType = type.getElementType();

            final long[] javaArray = new long[elements.size()];
            for (int i = 0; i < elements.size(); ++i) {
                javaArray[i] = (Long) fromJson(elementType, elements.get(i));
            }
            return javaArray;
        }
    }

    private static class MapDeserializer implements NodeDeserializer {

        @Override
        public Object deserialize(TypeNode type, JsonNode jsonNode) {
            final Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();
            final Map<Object, Object> javaMap = new HashMap<>();

            while (iterator.hasNext()) {
                processEntry(type, iterator, javaMap);
            }
            return javaMap;
        }

        private void processEntry(TypeNode type, Iterator<Map.Entry<String, JsonNode>> iterator, Map<Object, Object> javaMap) {
            final Map.Entry<String, JsonNode> entry = iterator.next();
            //NOTE: Since JSON only allows string as key, so all hashmap's key has a single level
            final String keyStr = entry.getKey();
            final Object key;
            switch (type.getKeyType().getValue()) {
                case BOOL:
                    key = Boolean.valueOf(keyStr);
                    break;
                case STRING:
                    key = keyStr;
                    break;
                case DOUBLE:
                    key = Double.valueOf(keyStr);
                    break;
                case INT:
                    key = Integer.valueOf(keyStr);
                    break;
                case LONG:
                    key = Long.valueOf(keyStr);
                    break;
                default:
                    throw new IllegalArgumentException("map keys can only be primitive type: " + type);
            }
            final Object value = fromJson(type.getElementType(), entry.getValue());
            javaMap.put(key, value);
        }
    }
}
