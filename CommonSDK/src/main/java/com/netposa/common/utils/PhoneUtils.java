package com.netposa.common.utils;

import android.text.TextUtils;

/**
 * 作者：陈新明
 * 创建日期：2017/6/26
 * 邮箱：
 * 描述：TODO
 */

public class PhoneUtils {
    /**
     *
     * @param phone 手机号
     * @return 返回隐藏中间4位的手机号
     */
    public static String hidePhoneNum(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }
        if (phone.length() == 11) {
            //11位手机号，则将中间四位变成136****1135
            return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        }
        return phone;
    }

}
