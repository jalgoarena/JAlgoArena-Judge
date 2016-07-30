package org.algohub.engine.judge;

import org.junit.Test;

import static org.algohub.engine.judge.BetterObjects.equalForObjectsOrArrays;
import static org.assertj.core.api.Assertions.assertThat;

public class BetterObjectsSpec {

    @Test
    public void same_object() throws Exception {
        Object item = new Object();
        assertThat(equalForObjectsOrArrays(item, item)).isTrue();
    }

    @Test
    public void different_object() throws Exception {
        Object item1 = new Object();
        Object item2 = new Object();
        assertThat(equalForObjectsOrArrays(item1, item2)).isFalse();
    }

    @Test
    public void if_any_is_null() throws Exception {
        Object item = new Object();
        assertThat(equalForObjectsOrArrays(item, null)).isFalse();
    }

    @Test
    public void if_both_are_nulls() throws Exception {
        assertThat(equalForObjectsOrArrays(null, null)).isTrue();
    }

    @Test
    public void two_equal_multidimensional_arrays() throws Exception {
        int[][] arr1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 0, 1, 2},
                {3, 4, 5, 6}
        };

        int[][] arr2 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 0, 1, 2},
                {3, 4, 5, 6}
        };

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isTrue();
    }

    @Test
    public void two_different_multidimensional_arrays() throws Exception {
        int[][] arr1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 1},
                {9, 0, 1, 2},
                {3, 4, 5, 6}
        };

        int[][] arr2 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 0, 1, 2},
                {3, 4, 5, 6}
        };

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isFalse();
    }

    @Test
    public void two_equal_byte_arrays() throws Exception {
        byte[] arr1 = { 0, 1, 2, 0, 1, 2};
        byte[] arr2 = { 0, 1, 2, 0, 1, 2};

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isTrue();
    }

    @Test
    public void two_different_byte_arrays() throws Exception {
        byte[] arr1 = { 0, 0, 2, 0, 1, 2};
        byte[] arr2 = { 0, 1, 2, 0, 1, 2};

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isFalse();
    }

    @Test
    public void two_equal_short_arrays() throws Exception {
        short[] arr1 = { 0, 11, 2, 0, 1, 2};
        short[] arr2 = { 0, 11, 2, 0, 1, 2};

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isTrue();
    }

    @Test
    public void two_different_short_arrays() throws Exception {
        short[] arr1 = { 0, 0, 2, 0, 1, 2};
        short[] arr2 = { 0, 1, 12, 0, 1, 2};

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isFalse();
    }

    @Test
    public void two_equal_int_arrays() throws Exception {
        int[] arr1 = { 0, 11, 2, 0, 1, 2222};
        int[] arr2 = { 0, 11, 2, 0, 1, 2222};

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isTrue();
    }

    @Test
    public void two_different_int_arrays() throws Exception {
        int[] arr1 = { 0, 0, 2, 0, 11111, 2};
        int[] arr2 = { 0, 1, 12, 0, 1, 2};

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isFalse();
    }

    @Test
    public void two_equal_long_arrays() throws Exception {
        long[] arr1 = { 0, 11123123, 2, 0, 1, 2222};
        long[] arr2 = { 0, 11123123, 2, 0, 1, 2222};

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isTrue();
    }

    @Test
    public void two_different_long_arrays() throws Exception {
        long[] arr1 = { 0, 0, 2, 0, 11111, 2};
        long[] arr2 = { 0, 1, 12, 0, 11123123, 2};

        assertThat(equalForObjectsOrArrays(arr1, arr2)).isFalse();
    }
}