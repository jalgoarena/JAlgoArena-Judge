package org.algohub.engine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.algohub.engine.type.ListNode;

public final class ObjectMapperInstance {
    public static final ObjectMapper INSTANCE = new ObjectMapper();

    private ObjectMapperInstance() {
        // static class
    }

    static {
        INSTANCE.registerModule(new Jdk8Module());
        INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        SimpleModule customModule = new SimpleModule();
        customModule.addDeserializer(ListNode.class, new ListNode.Deserializer());
        INSTANCE.registerModule(customModule);
    }
}
