package com.jalgoarena.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.jalgoarena.ApplicationConfiguration
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Problem
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import javax.inject.Inject

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(ApplicationConfiguration::class))
class ProblemTest {

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Test
    fun can_be_serialized_and_deserialized_from_json() {
        val problem = Problem(
                id = "dummy_id",
                title = "dummy_title",
                description = "dummy description",
                level = 3,
                memoryLimit = 1,
                timeLimit = 1,
                function = TWO_SUM_FUNCTION,
                skeletonCode = "dummy code",
                kotlinSkeletonCode = "kotlin dummy code",
                testCases = emptyArray()
        )

        val problemAsString = objectMapper.writeValueAsString(problem)
        val deserializedProblem = objectMapper.readValue(problemAsString, Problem::class.java)

        assertThat(deserializedProblem.id).isEqualTo(problem.id)
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
