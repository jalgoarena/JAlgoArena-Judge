package com.jalgoarena.judge

import com.jalgoarena.utils.BetterEquals
import com.jalgoarena.utils.ThrowableEnhancements
import java.lang.reflect.Method
import java.util.concurrent.Callable

internal class JudgeTask(
        private val clazz: Any,
        private val method: Method,
        private val testCases: Array<InternalTestCase>
) : Callable<List<Boolean>>, ThrowableEnhancements, BetterEquals {

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
}
