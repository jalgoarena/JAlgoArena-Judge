package org.algohub.engine.judge

import java.util.Arrays

internal object BetterObjects {

    fun equalForObjectsOrArrays(a: Any?, b: Any?): Boolean {
        if (a === b) {
            return true
        } else if (a == null || b == null) {
            return false
        }

        return checkDeepObjects(a, b)
    }

    private fun checkDeepObjects(a: Any, b: Any): Boolean {
        if (a is Array<*> && b is Array<*>) {
            return Arrays.deepEquals(a, b)
        }

        return checkByteArray(a, b)
    }

    private fun checkByteArray(a: Any, b: Any): Boolean {
        if (a is ByteArray && b is ByteArray) {
            return Arrays.equals(a, b)
        }

        return checkShortArray(a, b)
    }

    private fun checkShortArray(a: Any, b: Any): Boolean {
        if (a is ShortArray && b is ShortArray) {
            return Arrays.equals(a, b)
        }

        return checkIntArray(a, b)
    }

    private fun checkIntArray(a: Any, b: Any): Boolean {
        if (a is IntArray && b is IntArray) {
            return Arrays.equals(a, b)
        }

        return checkLongArray(a, b)
    }

    private fun checkLongArray(a: Any, b: Any): Boolean {
        if (a is LongArray && b is LongArray) {
            return Arrays.equals(a, b)
        }

        return checkCharArray(a, b)
    }

    private fun checkCharArray(a: Any, b: Any): Boolean {
        if (a is CharArray && b is CharArray) {
            return Arrays.equals(a, b)
        }

        return checkFloatArray(a, b)
    }

    private fun checkFloatArray(a: Any, b: Any): Boolean {
        if (a is FloatArray && b is FloatArray) {
            return Arrays.equals(a, b)
        }

        return checkDoubleArray(a, b)
    }

    private fun checkDoubleArray(a: Any, b: Any): Boolean {
        if (a is DoubleArray && b is DoubleArray) {
            return Arrays.equals(a, b)
        }

        return checkBoolArray(a, b)
    }

    private fun checkBoolArray(a: Any, b: Any): Boolean {
        if (a is BooleanArray && b is BooleanArray) {
            return Arrays.equals(a, b)
        }

        return checkObjects(a, b)
    }

    private fun checkObjects(a: Any, b: Any): Boolean {
        return a == b
    }
}// static class
