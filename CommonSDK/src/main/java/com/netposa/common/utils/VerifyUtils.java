package com.netposa.common.utils;

import android.text.TextUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：安兴亚
 * 创建日期：2017/06/20
 * 邮箱：anxingya@lingdanet.com
 * 描述：TODO
 */

public class VerifyUtils {

    /**
     * 判断是否为手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        /*
            移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
            联通：130、131、132、152、155、156、185、186
            电信：133、153、180、189、（1349卫通）
            总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
            有虚拟运营商，所以中间的数字不再判断，只判断1开头，11位就OK了
        */
        String telRegex = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 判断是否为邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmailNO(String email) {
        /*
            移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
            联通：130、131、132、152、155、156、185、186
            电信：133、153、180、189、（1349卫通）
            总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
            有虚拟运营商，所以中间的数字不再判断，只判断1开头，11位就OK了
        */
        String telRegex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return Pattern.matches(telRegex, email);
        }
    }



    /**
     * @param password 密码
     * @return true：密码有中文，false密码无中文
     */
    public static boolean passMatch(String password) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(password);
        while (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * @param cs
     * @return true 是纯数字
     */
    public static boolean isNumeric(CharSequence cs) {
        if (TextUtils.isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static <E> boolean isNullOrEmpty(Collection<E> coll) {
        if (null == coll)
            return true;
        if (coll.size() == 0)
            return true;

        return false;
    }

    public static <K, V> boolean isNullOrEmpty(Map<K, V> map) {
        if (null == map)
            return true;
        if (map.size() == 0)
            return true;

        return false;
    }

    public static <T> boolean isNullOrEmpty(T[] array) {
        if (null == array)
            return true;
        if (array.length == 0)
            return true;

        return false;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}
