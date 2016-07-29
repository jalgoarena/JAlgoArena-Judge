package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import org.algohub.engine.type.TypeNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class MapDeserializer implements NodeDeserializer {

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
        switch (type.getKeyType().get().getValue()) {
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
        final Object value = Deserializer.fromJson(type.getElementType().get(), entry.getValue());
        javaMap.put(key, value);
    }
}
