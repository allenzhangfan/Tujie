package com.netposa.common.net;

/**
 * author: yeguoqiang
 * created on: 2018/8/17 13:55
 * description:
 */
public class HttpConstant {
    /**
     * 是否成功
     */
    public static final int IS_SUCCESS = 200;

    /**
     * 是否有权限
     */
    public static final int IS_PERMISSION_DENIED = 403;

    /**
     * token过期
     */
    public static final int IS_OUT_OF_DATE = 40002;

    /**
     * 账号在另一端登录被踢下线
     */
    public static final int IS_KICK_OFF = 40004;

    /**
     * 无效的返回结果
     */
    public static final int IS_INVALID = 500;
}
