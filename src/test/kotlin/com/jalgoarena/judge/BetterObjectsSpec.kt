package com.jalgoarena.judge

import com.jalgoarena.utils.BetterObjects
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat

class BetterObjectsSpec {

    @Test
    @Throws(Exception::class)
    fun same_object() {
        val item = Any()
        assertThat(BetterObjects.equalForObjectsOrArrays(item, item)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun different_object() {
        val item1 = Any()
        val item2 = Any()
        assertThat(BetterObjects.equalForObjectsOrArrays(item1, item2)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun if_any_is_null() {
        val item = Any()
        assertThat(BetterObjects.equalForObjectsOrArrays(item, null)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun if_both_are_nulls() {
        assertThat(BetterObjects.equalForObjectsOrArrays(null, null)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_equal_multidimensional_arrays() {
        val arr1 = arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 0, 1, 2), intArrayOf(3, 4, 5, 6))

        val arr2 = arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 0, 1, 2), intArrayOf(3, 4, 5, 6))

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_different_multidimensional_arrays() {
        val arr1 = arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 1), intArrayOf(9, 0, 1, 2), intArrayOf(3, 4, 5, 6))

        val arr2 = arrayOf(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 0, 1, 2), intArrayOf(3, 4, 5, 6))

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun two_equal_byte_arrays() {
        val arr1 = byteArrayOf(0, 1, 2, 0, 1, 2)
        val arr2 = byteArrayOf(0, 1, 2, 0, 1, 2)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_different_byte_arrays() {
        val arr1 = byteArrayOf(0, 0, 2, 0, 1, 2)
        val arr2 = byteArrayOf(0, 1, 2, 0, 1, 2)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun two_equal_short_arrays() {
        val arr1 = shortArrayOf(0, 11, 2, 0, 1, 2)
        val arr2 = shortArrayOf(0, 11, 2, 0, 1, 2)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_different_short_arrays() {
        val arr1 = shortArrayOf(0, 0, 2, 0, 1, 2)
        val arr2 = shortArrayOf(0, 1, 12, 0, 1, 2)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun two_equal_int_arrays() {
        val arr1 = intArrayOf(0, 11, 2, 0, 1, 2222)
        val arr2 = intArrayOf(0, 11, 2, 0, 1, 2222)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_different_int_arrays() {
        val arr1 = intArrayOf(0, 0, 2, 0, 11111, 2)
        val arr2 = intArrayOf(0, 1, 12, 0, 1, 2)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun two_equal_long_arrays() {
        val arr1 = longArrayOf(0, 11123123, 2, 0, 1, 2222)
        val arr2 = longArrayOf(0, 11123123, 2, 0, 1, 2222)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_different_long_arrays() {
        val arr1 = longArrayOf(0, 0, 2, 0, 11111, 2)
        val arr2 = longArrayOf(0, 1, 12, 0, 11123123, 2)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun two_equal_char_arrays() {
        val arr1 = charArrayOf('a', 'b', 'c', 'd', 'e')
        val arr2 = charArrayOf('a', 'b', 'c', 'd', 'e')

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_different_char_arrays() {
        val arr1 = charArrayOf('a', 'b', 'c', 'd', 'e')
        val arr2 = charArrayOf('a', 'b', 'c', 'd', 'z')

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun two_equal_float_arrays() {
        val arr1 = floatArrayOf(0.0f, 1.3f, 2.4f, 0.1f, 1.01f, 2222.43f)
        val arr2 = floatArrayOf(0.0f, 1.3f, 2.4f, 0.1f, 1.01f, 2222.43f)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_different_float_arrays() {
        val arr1 = floatArrayOf(0.0f, 1.3f, 2.4f, 0.1f, 1.01f, 2222.43f)
        val arr2 = floatArrayOf(0.0f, 1.3f, 2.4f, 0.1f, 1.01f, 22231.43f)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isFalse()
    }

    @Test
    @Throws(Exception::class)
    fun two_equal_boolean_arrays() {
        val arr1 = booleanArrayOf(true, false, false, true, true, false)
        val arr2 = booleanArrayOf(true, false, false, true, true, false)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun two_different_boolean_arrays() {
        val arr1 = booleanArrayOf(true, false, false, true, true, false)
        val arr2 = booleanArrayOf(true, false, false, true, false, false)

        assertThat(BetterObjects.equalForObjectsOrArrays(arr1, arr2)).isFalse()
    }
}
