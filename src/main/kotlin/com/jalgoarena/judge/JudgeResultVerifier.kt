package com.jalgoarena.judge

import com.jalgoarena.type.Pair
import java.util.*

class JudgeResultVerifier {
    fun isValidAnswer(expected: Any?, toCheck: Any?): Boolean {
        return equalForObjectsOrArrays(expected, toCheck)
    }

    private fun equalForObjectsOrArrays(expected: Any?, toCheck: Any?): Boolean {
        return when {
            expected === toCheck -> return true
            expected == null || toCheck == null -> return false
            expected is Pair && toCheck is Pair -> return comparePairs(expected, toCheck)
            expected is Array<*> && toCheck is Array<*> -> return Arrays.deepEquals(expected, toCheck)
            expected is IntArray && toCheck is IntArray -> Arrays.equals(expected, toCheck)
            expected is Double && toCheck is Double -> equalDouble(expected, toCheck)
            expected is Int && toCheck is Double -> equalDouble(expected.toDouble(), toCheck)
            expected is Double && toCheck is Int -> equalDouble(expected, toCheck.toDouble())
            expected == toCheck -> return true
            else -> return expected.toString() == toCheck.toString()
        }
    }

    private fun comparePairs(expected: Pair, toCheck: Pair): Boolean {
        return equalForObjectsOrArrays(expected.first, toCheck.first) &&
                equalForObjectsOrArrays(expected.second, toCheck.second)
    }

    private fun equalDouble(expected: Double, toCheck: Double): Boolean {
        return Math.abs(toCheck - expected) / Math.max(1.0, expected) < 1e-5
    }
}
