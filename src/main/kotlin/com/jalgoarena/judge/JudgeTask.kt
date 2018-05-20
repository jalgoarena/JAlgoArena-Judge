package com.jalgoarena.judge

import com.jalgoarena.judge.PerformanceSnapshot.Companion.takePerformanceSnapshot
import org.apache.commons.lang.exception.ExceptionUtils.getCause
import org.jruby.RubyArray
import java.lang.reflect.Method
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.Callable

internal class JudgeTask(
        private val clazz: Any,
        private val method: Method,
        private val testCases: Array<InternalTestCase>
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
        val cause = getCause(e)

        if (cause is NullPointerException) {
            false
        } else {
            throw InterruptedException(cause.javaClass.name)
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
            a is IntArray && b is RubyArray -> equalRubyArray(a, b)
            a is Double && b is Double -> equalDouble(a, b)
            a == b -> return true
            else -> return a.toString() == b.toString()
        }
    }

    private fun equalDouble(a: Double, b: Double): Boolean {
        val df = DecimalFormat("#.######")
        df.roundingMode = RoundingMode.CEILING

        return df.format(a) == df.format(b)
    }

    private fun equalRubyArray(a: IntArray, b: RubyArray): Boolean {
        return equalForObjectsOrArrays(
                a.map { it.toString() },
                b.map { it.toString() }
        )
    }
}
