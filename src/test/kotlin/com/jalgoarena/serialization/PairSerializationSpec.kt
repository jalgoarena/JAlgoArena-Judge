package com.jalgoarena.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jalgoarena.ApplicationConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test

class PairSerializationSpec {

    private lateinit var objectMapper: ObjectMapper

    @Before
    fun setUp() {
        objectMapper = ApplicationConfiguration().objectMapper()
    }

    @Test
    fun deserialize_simple_graph() {
        val pair = objectMapper.readValue<Pair<Double, Double>>(PAIR_JSON)

       assertThat(pair.first).isEqualTo(30.12345)
       assertThat(pair.second).isEqualTo(0.00001)
    }

    companion object {
        @Language("JSON")
        private const val PAIR_JSON = "{\"first\":30.12345,\"second\":0.00001}"
    }
}