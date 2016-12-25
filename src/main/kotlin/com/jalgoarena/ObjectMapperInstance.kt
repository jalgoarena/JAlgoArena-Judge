package com.jalgoarena

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jalgoarena.type.ListNode

object ObjectMapperInstance {
    val INSTANCE: ObjectMapper = ObjectMapper()

    init {
        INSTANCE.registerKotlinModule()
        INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        val customModule = SimpleModule()
        customModule.addDeserializer(ListNode::class.java, ListNode.Deserializer())
        INSTANCE.registerModule(customModule)
    }
}
