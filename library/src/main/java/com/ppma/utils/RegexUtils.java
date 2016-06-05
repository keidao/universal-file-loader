package com.ppma.utils;

/**
 * Created by keidao on 9/20/15.
 */
public class RegexUtils {

    public static String removeNonDigits(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        return str.replaceAll("[^0-9]+", "");
    }
}
