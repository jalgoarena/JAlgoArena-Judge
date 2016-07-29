package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.JsonNode;
import org.algohub.engine.type.TypeNode;

interface ContainerNodeDeserializer {
    Object deserialize(final TypeNode type, final JsonNode jsonNode);
}
