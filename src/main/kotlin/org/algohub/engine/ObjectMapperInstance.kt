package org.algohub.engine

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import org.algohub.engine.type.ListNode

object ObjectMapperInstance {
    val INSTANCE: ObjectMapper = ObjectMapper()

    init {
        INSTANCE.registerModule(Jdk8Module())
        INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        val customModule = SimpleModule()
        customModule.addDeserializer(ListNode::class.java, ListNode.Deserializer())
        INSTANCE.registerModule(customModule)
    }
}
