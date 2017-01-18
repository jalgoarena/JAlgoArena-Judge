package com.jalgoarena.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.type.Interval
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.boot.test.json.JacksonTester

class IntervalJsonSerializationTest {

    private lateinit var json: JacksonTester<Interval>

    @Before
    fun setup() {
        val objectMapper = jacksonObjectMapper()
        JacksonTester.initFields(this, objectMapper)
    }

    @Test
    fun serializes_problem() {
        assertThat(json.write(INTERVAL))
                .isEqualToJson("interval.json")
    }

    @Test
    fun deserialize_problem() {
        assertThat(json.parse(INTERVAL_JSON))
                .isEqualTo(INTERVAL)
    }

    private val INTERVAL = Interval(
            start = 1,
            end = 2
    )

    //language=JSON
    private val INTERVAL_JSON = """{ "start": 1, "end": 2 }"""
}
