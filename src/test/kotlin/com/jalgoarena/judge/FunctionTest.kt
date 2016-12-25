package com.jalgoarena.judge

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import javax.inject.Inject

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(ApplicationConfiguration::class))
class FunctionTest {

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Test
    fun can_be_serialized_and_deserialized_from_json() {
        val functionAsString = objectMapper.writeValueAsString(TWO_SUM_FUNCTION)
        val deserializedFunction = objectMapper.readValue(functionAsString, Function::class.java)

        assertThat(deserializedFunction.name).isEqualTo(TWO_SUM_FUNCTION.name)
    }

    private val TWO_SUM_FUNCTION = Function("twoSum",
            Function.Return("[I",
                    "[index1 + 1, index2 + 1] (index1 < index2)"),
            arrayOf(Function.Parameter("numbers", "[I", "An array of Integers"),
                    Function.Parameter("target", "java.lang.Integer",
                            "target = numbers[index1] + numbers[index2]")
            )
    )
}
