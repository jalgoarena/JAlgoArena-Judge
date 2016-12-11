package org.algohub.engine.judge

import com.google.common.base.Preconditions
import org.algohub.engine.ObjectMapperInstance
import org.slf4j.LoggerFactory
import java.io.IOException

internal class InternalTestCase(testCase: Problem.TestCase, function: Function) {

    val input: Array<Any?>
    val output: Any?
    private val returnsVoid: Boolean

    init {
        val parameters = function.parameters
        Preconditions.checkArgument(parameters.size == testCase.input.size())

        try {
            input = arrayOfNulls<Any>(testCase.input.size())
            for (i in input.indices) {
                input[i] = deserialize(
                        testCase.input.get(i).toString(),
                        Class.forName(parameters[i].type)
                )
            }

            returnsVoid = "void" == function.returnStatement.type

            if (returnsVoid) {
                this.output = deserialize(
                        testCase.output.toString(),
                        Class.forName(parameters[0].type)
                )
            } else {
                this.output = deserialize(
                        testCase.output.toString(),
                        Class.forName(function.returnStatement.type)
                )
            }
        } catch (e: ClassNotFoundException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException(e.message)
        } catch (e: IOException) {
            LOG.error(e.message, e)
            throw IllegalArgumentException(e.message)
        }

    }

    @Throws(IOException::class)
    private fun deserialize(content: String, type: Class<*>): Any? {
        return ObjectMapperInstance.INSTANCE.readValue(content, type)
    }

    fun returnsVoid() = returnsVoid

    companion object {
        private val LOG = LoggerFactory.getLogger(InternalTestCase::class.java)
    }
}
