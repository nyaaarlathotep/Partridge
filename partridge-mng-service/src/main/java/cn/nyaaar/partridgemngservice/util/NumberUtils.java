package cn.nyaaar.partridgemngservice.util;

/**
 * @author yuegenhua
 * @Version $Id: NumberUtils.java, v 0.1 2022-28 16:55 yuegenhua Exp $$
 */

public final class NumberUtils {
    private NumberUtils() {}

    /**
     * 0 for false, Non 0 for true
     *
     * @param integer the int
     * @return the boolean
     */
    public static boolean int2boolean(int integer) {
        return integer != 0;
    }

    /**
     * false for 0, true for 1
     *
     * @param bool the boolean
     * @return the int
     */
    public static int boolean2int(boolean bool) {
        return bool ? 1 : 0;
    }

    /**
     * Do not throw NumberFormatException, use default value
     *
     * @param str the string to be parsed
     * @param defaultValue the value to return when get error
     * @return the value of the string
     */
    public static int parseIntSafely(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * Do not throw NumberFormatException, use default value
     *
     * @param str the string to be parsed
     * @param defaultValue the value to return when get error
     * @return the value of the string
     */
    public static long parseLongSafely(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * Do not throw NumberFormatException, use default value
     *
     * @param str the string to be parsed
     * @param defaultValue the value to return when get error
     * @return the value of the string
     */
    public static float parseFloatSafely(String str, float defaultValue) {
        try {
            return Float.parseFloat(str);
        } catch (Throwable e) {
            return defaultValue;
        }
    }
}
