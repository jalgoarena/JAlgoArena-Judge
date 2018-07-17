package com.jalgoarena.judge

import com.jalgoarena.judge.PerformanceSnapshot.Companion.takePerformanceSnapshot
import java.lang.reflect.Method
import java.util.concurrent.Callable

internal class JudgeTask(
        private val clazz: Any,
        private val method: Method,
        private val testCases: Array<InternalTestCase>,
        private val verifier: JudgeResultVerifier
) : Callable<Pair<List<Boolean>, PerformanceResult>> {

    override fun call(): Pair<List<Boolean>, PerformanceResult> {
        val before = takePerformanceSnapshot()
        val testCasesResults = testCases.map { judge(clazz, method, it) }
        val after = takePerformanceSnapshot()

        return Pair(testCasesResults, PerformanceResult.create(
              before, after
        ))
    }

    private fun judge(clazz: Any, method: Method, testCase: InternalTestCase) = try {
        invokeAndCheck(clazz, method, testCase)
    } catch(e: Throwable) {
        val cause = e.cause!!

        if (cause is NullPointerException) {
            false
        } else {
            throw InterruptedException(cause.javaClass.name)
        }
    }

    private fun invokeAndCheck(clazz: Any, method: Method, testCase: InternalTestCase): Boolean {
        val input = testCase.input
        val output = method.invoke(clazz, *input)

        return verifier.isValidAnswer(
                testCase.output,
                if (testCase.returnsVoid) input[0] else output
        )
    }
}
