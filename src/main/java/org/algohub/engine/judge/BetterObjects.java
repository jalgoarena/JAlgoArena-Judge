package org.algohub.engine.judge;

import java.util.Arrays;

class BetterObjects {

    private BetterObjects() {
        // static class
    }

    static boolean equalForObjectsOrArrays(Object a, Object b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        }

        return checkDeepObjects(a, b);
    }

    private static boolean checkDeepObjects(Object a, Object b) {
        if (a instanceof Object[] && b instanceof Object[]) {
            return Arrays.deepEquals((Object[]) a, (Object[]) b);
        }

        return checkByteArray(a, b);
    }

    private static boolean checkByteArray(Object a, Object b) {
        if (a instanceof byte[] && b instanceof byte[]) {
            return Arrays.equals((byte[]) a, (byte[]) b);
        }

        return checkShortArray(a, b);
    }

    private static boolean checkShortArray(Object a, Object b) {
        if (a instanceof short[] && b instanceof short[]) {
            return Arrays.equals((short[]) a, (short[]) b);
        }

        return checkIntArray(a, b);
    }

    private static boolean checkIntArray(Object a, Object b) {
        if (a instanceof int[] && b instanceof int[]) {
            return Arrays.equals((int[]) a, (int[]) b);
        }

        return checkLongArray(a, b);
    }

    private static boolean checkLongArray(Object a, Object b) {
        if (a instanceof long[] && b instanceof long[]) {
            return Arrays.equals((long[]) a, (long[]) b);
        }

        return checkCharArray(a, b);
    }

    private static boolean checkCharArray(Object a, Object b) {
        if (a instanceof char[] && b instanceof char[]) {
            return Arrays.equals((char[]) a, (char[]) b);
        }

        return checkFloatArray(a, b);
    }

    private static boolean checkFloatArray(Object a, Object b) {
        if (a instanceof float[] && b instanceof float[]) {
            return Arrays.equals((float[]) a, (float[]) b);
        }

        return checkDoubleArray(a, b);
    }

    private static boolean checkDoubleArray(Object a, Object b) {
        if (a instanceof double[] && b instanceof double[]) {
            return Arrays.equals((double[]) a, (double[]) b);
        }

        return checkBoolArray(a, b);
    }

    private static boolean checkBoolArray(Object a, Object b) {
        if (a instanceof boolean[] && b instanceof boolean[]) {
            return Arrays.equals((boolean[]) a, (boolean[]) b);
        }

        return checkObjects(a, b);
    }

    private static boolean checkObjects(Object a, Object b) {
        return a.equals(b);
    }
}
