package com.jalgoarena.judge

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Preconditions
import org.slf4j.LoggerFactory
import java.io.IOException

class InternalTestCase(val input: Array<Any?>,
                       val output: Any?,
                       val returnsVoid: Boolean)

class InternalTestCaseParser(val function: Function,
                             val objectMapper: ObjectMapper) {

    private val LOG = LoggerFactory.getLogger(InternalTestCaseParser::class.java)

    fun parse(testCase: Problem.TestCase): InternalTestCase {

        val parameters = function.parameters
        Preconditions.checkArgument(parameters.size == testCase.input.size())

        try {
            val input: Array<Any?> = arrayOfNulls(testCase.input.size())
            for (i in input.indices) {
                input[i] = deserialize(
                        testCase.input.get(i).toString(),
                        Class.forName(parameters[i].type)
                )
            }

            val returnsVoid = "void" == function.returnStatement.type

            val outputType =
                    if (returnsVoid) Class.forName(parameters[0].type)
                    else Class.forName(function.returnStatement.type)

            val output = deserialize(
                    testCase.output.toString(),
                    outputType
            )

            return InternalTestCase(input, output, returnsVoid)
        } catch (e: ClassNotFoundException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException(e.message)
        } catch (e: IOException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException(e.message)
        }
    }

    private fun deserialize(content: String, type: Class<*>): Any? {
        return objectMapper.readValue(content, type)
    }
}
