package com.jalgoarena.judge

import org.jruby.RubyArray
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import com.jalgoarena.type.*

class JudgeResultVerifier {
    fun isValidAnswer(a: Any?, b: Any?): Boolean {
        return equalForObjectsOrArrays(a, b)
    }

    private fun equalForObjectsOrArrays(a: Any?, b: Any?): Boolean {
        return when {
            a === b -> return true
            a == null || b == null -> return false
            a is Pair && b is Pair -> return comparePairs(a, b)
            a is Array<*> && b is Array<*> -> return Arrays.deepEquals(a, b)
            a is IntArray && b is IntArray -> Arrays.equals(a, b)
            a is IntArray && b is RubyArray -> equalRubyArray(a, b)
            a is Double && b is Double -> equalDouble(a, b)
            a is Int && b is Double -> equalDouble(a.toDouble(), b)
            a is Double && b is Int -> equalDouble(a, b.toDouble())
            a == b -> return true
            else -> return a.toString() == b.toString()
        }
    }

    private fun comparePairs(a: Pair, b: Pair): Boolean {
        return equalForObjectsOrArrays(a.first, b.first) &&
                equalForObjectsOrArrays(a.second, b.second)
    }

    private fun equalDouble(a: Double, b: Double): Boolean {
        val df = DecimalFormat("#.#####")
        df.roundingMode = RoundingMode.HALF_UP

        return df.format(a) == df.format(b)
    }

    private fun equalRubyArray(a: IntArray, b: RubyArray): Boolean {
        return equalForObjectsOrArrays(
                a.map { it.toString() },
                b.map { it.toString() }
        )
    }

}
