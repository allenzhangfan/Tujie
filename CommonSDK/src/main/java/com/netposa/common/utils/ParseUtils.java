package com.netposa.common.utils;

/**
 * Created by yexiaokang on 2018/9/25.
 */
public class ParseUtils {

    public static int parseInt(String s, int defValue) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defValue;
        }
    }

    public static float parseFloat(String s, float defValue) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return defValue;
        }
    }
}
