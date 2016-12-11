package org.algohub.engine.judge

import java.util.Arrays

internal object BetterObjects {

    fun equalForObjectsOrArrays(a: Any?, b: Any?): Boolean {
        return when {
            a === b -> return true
            a == null || b == null -> return false
            a is Array<*> && b is Array<*> -> return Arrays.deepEquals(a, b)
            a is ByteArray && b is ByteArray -> Arrays.equals(a, b)
            a is ShortArray && b is ShortArray -> Arrays.equals(a, b)
            a is IntArray && b is IntArray -> Arrays.equals(a, b)
            a is LongArray && b is LongArray -> Arrays.equals(a, b)
            a is CharArray && b is CharArray -> Arrays.equals(a, b)
            a is FloatArray && b is FloatArray -> Arrays.equals(a, b)
            a is DoubleArray && b is DoubleArray -> Arrays.equals(a, b)
            a is BooleanArray && b is BooleanArray -> Arrays.equals(a, b)
            else -> a == b
        }
    }
}
