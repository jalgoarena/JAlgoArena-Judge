package org.algohub.engine.type;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TypeNodeDeserializer
    extends JsonDeserializer<TypeNode> {

  public TypeNode deserialize(final JsonParser p, final DeserializationContext ctxt)
      throws IOException {
    final JsonNode jsonNode = p.readValueAsTree();
    return TypeNode.fromString(jsonNode.asText());
  }
}
