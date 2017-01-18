package com.jalgoarena.judge

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Preconditions
import com.jalgoarena.domain.Function
import com.jalgoarena.domain.Problem
import com.jalgoarena.type.ListNode
import org.slf4j.LoggerFactory
import java.util.*

class ProblemTestCaseParser(
        private val function: Function,
        private val objectMapper: ObjectMapper
) {

    private val LOG = LoggerFactory.getLogger(ProblemTestCaseParser::class.java)

    fun parse(testCase: Problem.TestCase): InternalTestCase {

        val parameters = function.parameters
        Preconditions.checkArgument(parameters.size == testCase.input.size())

        try {
            val input = input(parameters, testCase)
            val returnsVoid = "void" == function.returnStatement.type
            val output = output(parameters, returnsVoid, testCase)

            return InternalTestCase(input, output, returnsVoid)
        } catch(e: Throwable) {
            LOG.error(e.message, e)
            throw IllegalArgumentException(e.message)
        }
    }

    private fun input(parameters: List<Function.Parameter>, testCase: Problem.TestCase): Array<Any?> {
        val input: Array<Any?> = arrayOfNulls(testCase.input.size())
        for (i in input.indices) {
            val parameter = parameters[i]

            val type = parameter.type
            val content = testCase.input.get(i).toString()

            input[i] = when {
                type == "java.util.ArrayList" && parameter.generic == "ListNode" -> {
                    deserializeGeneric(content, object: TypeReference<ArrayList<ListNode>>() {})
                }
                else -> {
                    deserialize(content, Class.forName(type))
                }
            }
        }
        return input
    }

    private fun output(parameters: List<Function.Parameter>, returnsVoid: Boolean, testCase: Problem.TestCase): Any? {
        val outputType = when {
            returnsVoid -> Class.forName(parameters[0].type)
            else -> Class.forName(function.returnStatement.type)
        }

        return deserialize(
                testCase.output.toString(),
                outputType
        )
    }

    private fun deserialize(content: String, type: Class<*>) =
            objectMapper.readValue(content, type)

    private fun<T> deserializeGeneric(content: String, type: TypeReference<T>) =
            objectMapper.readValue<T>(content, type)
}
