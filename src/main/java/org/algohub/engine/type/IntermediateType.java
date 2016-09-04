package org.algohub.engine.type;

import java.util.Arrays;
import java.util.Optional;

public enum IntermediateType {
    VOID("void"),
    BOOL("bool"),
    STRING("string"),
    DOUBLE("double"),
    INT("int"),
    LONG("long"),
    ARRAY("array"),
    LIST("list"),
    SET("set"),
    MAP("map"),
    LINKED_LIST_NODE("LinkedListNode");

    private final String text;

    IntermediateType(final String text) {
        this.text = text;
    }

    public static IntermediateType fromString(final String text) {
        Optional<IntermediateType> intermediateType = Arrays.stream(values()).filter(
                x -> text.equalsIgnoreCase(x.text)
        ).findFirst();

        if (intermediateType.isPresent()) {
            return intermediateType.get();
        } else {
            throw new IllegalArgumentException("Unrecognized type: " + text);
        }
    }

    @Override
    public String toString() {
        return text;
    }
}
