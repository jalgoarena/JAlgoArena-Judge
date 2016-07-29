package org.algohub.engine.type;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 *  TypeNode (internal representation of type) Jackson deserializer
 */
public class TypeNodeDeserializer
        extends JsonDeserializer<TypeNode> {

    /**
     * Deserialize JsonNode to TypeNode
     * @param jsonParser JsonParser
     * @param deserializationContext Deserialization Context
     * @return deserialized TypeNode
     * @throws IOException Exception can be raised during Json deserialization process
     */
    @Override
    public TypeNode deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
            throws IOException {
        final JsonNode jsonNode = jsonParser.readValueAsTree();
        return TypeNode.fromString(jsonNode.asText());
    }
}
