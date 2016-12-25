package com.jalgoarena.judge

import com.google.common.base.Preconditions
import com.jalgoarena.ObjectMapperInstance
import org.slf4j.LoggerFactory
import java.io.IOException

internal class InternalTestCase(testCase: Problem.TestCase, function: Function) {

    private val LOG = LoggerFactory.getLogger(InternalTestCase::class.java)

    val input: Array<Any?>
    val output: Any?
    val returnsVoid: Boolean

    init {
        val parameters = function.parameters
        Preconditions.checkArgument(parameters.size == testCase.input.size())

        try {
            input = arrayOfNulls(testCase.input.size())
            for (i in input.indices) {
                input[i] = deserialize(
                        testCase.input.get(i).toString(),
                        Class.forName(parameters[i].type)
                )
            }

            returnsVoid = "void" == function.returnStatement.type

            val outputType =
                    if (returnsVoid) Class.forName(parameters[0].type)
                    else Class.forName(function.returnStatement.type)

            this.output = deserialize(
                    testCase.output.toString(),
                    outputType
            )
        } catch (e: ClassNotFoundException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException(e.message)
        } catch (e: IOException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException(e.message)
        }

    }

    private fun deserialize(content: String, type: Class<*>): Any? {
        return ObjectMapperInstance.INSTANCE.readValue(content, type)
    }
}
