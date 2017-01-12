package com.jalgoarena.judge

import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Callable

internal class JudgeTask(
        private val clazz: Any,
        private val method: Method,
        private val testCases: Array<InternalTestCase>
) : Callable<List<Boolean>> {

    override fun call() =
            testCases.map { judge(clazz, method, it) }

    private fun judge(clazz: Any, method: Method, testCase: InternalTestCase) = try {
        invokeAndCheck(clazz, method, testCase)
    } catch(e: Throwable) {
        val cause = getCause(e)

        if (cause is NullPointerException) {
            false
        } else {
            throw InterruptedException("${cause.javaClass.name}: ${cause.message}")
        }
    }

    private fun invokeAndCheck(clazz: Any, method: Method, testCase: InternalTestCase): Boolean {
        val input = testCase.input
        val output = method.invoke(clazz, *input)

        return equalForObjectsOrArrays(
                testCase.output,
                if (testCase.returnsVoid) input[0] else output
        )
    }

    private fun equalForObjectsOrArrays(a: Any?, b: Any?): Boolean {
        return when {
            a === b -> return true
            a == null || b == null -> return false
            a is Array<*> && b is Array<*> -> return Arrays.deepEquals(a, b)
            a is IntArray && b is IntArray -> Arrays.equals(a, b)
            else -> a == b
        }
    }

    private fun getCause(e: Throwable): Throwable {
        var cause: Throwable? = e.cause
        var result = e

        while (null != cause && result !== cause) {
            result = cause
            cause = result.cause
        }
        return result
    }
}
