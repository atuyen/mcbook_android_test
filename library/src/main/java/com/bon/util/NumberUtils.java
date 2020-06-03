package com.bon.util;

import com.bon.logger.Logger;
import com.bon.network.NetworkUtils;

import java.text.DecimalFormat;
import java.util.Locale;

@SuppressWarnings("ALL")
public class NumberUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // const
    private static final String DEFAULT_PATTER = "%.2f";

    /*Format NUMBER*/

    /**
     * customize format with float value
     *
     * @param pattern
     * @param value
     * @return
     */
    public static String formatNumber(float value, String pattern) {
        try {
            DecimalFormat myFormatter = new DecimalFormat(pattern);
            return myFormatter.format(value);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return "";
    }

    /**
     * customize format with double value
     *
     * @param value
     * @param pattern
     * @return
     */
    public static String formatNumber(double value, String pattern) {
        try {
            DecimalFormat myFormatter = new DecimalFormat(pattern);
            return myFormatter.format(value);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return "";
    }

    /**
     * format number(double) to string
     *
     * @param number
     * @return number has format with default patter "%.2f"
     */
    public static String formatNumberToString(double number) {
        return formatNumberToString(number, null);
    }

    /**
     * format number(double) to string
     *
     * @param number
     * @param express
     * @return number has format with pattern "express"
     */
    public static String formatNumberToString(double number, String express) {
        try {
            Locale locale = new Locale("en", "US");
            String pattern;

            if (StringUtils.isEmpty(express)) {
                pattern = DEFAULT_PATTER;
            } else {
                pattern = express;
            }

            return String.format(locale, pattern, number);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * format number(float) to String
     *
     * @param number
     * @return number has format with default patter "%.2f"
     */
    public static String formatNumberToString(float number) {
        return formatNumberToString(number, null);
    }

    /**
     * format number(float) to String
     *
     * @param number
     * @param express
     * @return number has format with pattern "express"
     */
    public static String formatNumberToString(float number, String express) {
        try {
            Locale locale = new Locale("en", "US");
            String pattern;

            if (StringUtils.isEmpty(express)) {
                pattern = DEFAULT_PATTER;
            } else {
                pattern = express;
            }

            return String.format(locale, pattern, number);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

	/* CONVERT STRING TO NUMBER */

    /**
     * convertObjectToMapString string to int
     *
     * @param number
     * @return Int
     */
    public static int convertStringToInteger(String number) {
        try {
            if (StringUtils.isEmpty(number)) return 0;

            // remove character ","
            number = number.replace(",", "").trim();
            return Integer.parseInt(number);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0;
    }

    /**
     * convertObjectToMapString string to long
     *
     * @param number
     * @return
     */
    public static long convertStringToLong(String number) {
        try {
            if (StringUtils.isEmpty(number)) return 0L;

            // remove character ","
            number = number.replace(",", "").trim();
            return Long.parseLong(number);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0L;
    }

    /**
     * convertObjectToMapString string to float
     *
     * @param number
     * @return Float
     */
    public static float convertStringToFloat(String number) {
        try {
            if (StringUtils.isEmpty(number)) return 0f;

            // remove character ","
            number = number.replace(",", "").trim();
            return Float.parseFloat(number);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0f;
    }

    /**
     * convertObjectToMapString string to double
     *
     * @param number
     * @return Double
     */
    public static double convertStringToDouble(String number) {
        try {
            if (StringUtils.isEmpty(number)) return 0;

            // remove character ","
            number = number.replace(",", "").trim();
            return Double.parseDouble(number);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return 0d;
    }

	/* CHECK NUMBER */

    /**
     * check number with default pattern "[+-]?\d*(\.\d+)?"
     *
     * @param number
     * @return true or false
     */
    public static boolean isNumeric(String number) {
        try {
            return number.matches("[+-]?\\d*(\\.\\d+)?");
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return false;
    }

    /**
     * check number with "pattern"
     *
     * @param number
     * @return true or false
     */
    public static boolean isNumeric(String number, String pattern) {
        try {
            return number.matches(pattern);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return false;
    }
}
