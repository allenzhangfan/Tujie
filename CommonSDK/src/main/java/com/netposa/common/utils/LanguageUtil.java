/*
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.common.utils;

import com.netposa.common.constant.GlobalConstants;

import java.util.Locale;

/**
 * 作者：陈新明
 * 创建日期：2016/11/8
 * 邮箱：chenxm0902
 * 描述：//TODO
 */

public class LanguageUtil {
    /**
     * 判断是否是英文
     *
     * @return
     */
    public static String language() {
        String language = Locale.getDefault().getLanguage();
        return language;
    }

    /**
     * 获取语言环境
     *
     * @return
     */
    public static String getLanguage() {
        String language = language();
        if (GlobalConstants.INTERNATIONAL) {
            if (language.equals("en")) {
                return "en";
            } else if (language.equals("ko")) {
                return "en";
            } else if (language.equals("ja")) {
                return "ja";
            } else {
                return "en";
            }
        } else {
            return "zh";
        }
    }

    public static boolean isLanguageZh() {
        String language = getLanguage();
        if (language.equals("zh"))
            return true;
        else
            return false;
    }
}
