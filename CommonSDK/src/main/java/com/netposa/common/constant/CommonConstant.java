/**
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.common.constant;


import android.os.Environment;

import com.netposa.common.BuildConfig;

import java.io.File;

/**
 * 作者：谢小明
 * 创建日期：2016/10/13
 * 邮箱：xiexm0902@lingdanet.com
 * 描述：//常量类
 */

public class CommonConstant {

    /**
     * 外部存储区默认主目录
     */
    public static final String DEFAULT_EXTERNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    /**
     * 应用程序主目录（外部存储区）
     */
    public static final String MAIN_PATH = "Tujie/";

    /**
     * 应用程序的默认根目录
     */
    public static final String ROOT_PATH = DEFAULT_EXTERNAL_PATH + MAIN_PATH;

    /**
     * 下载目录
     */
    public static final String DOWNLOAD_PATH = ROOT_PATH + "download/";

    /**
     * 缓存目录
     */
    public static final String CACHE_PATH = ROOT_PATH + "cache/";

    /**
     * 头像目录
     */
    public static final String AVATAR_PATH = ROOT_PATH + "avatar/";

    /**
     * 拍照目录
     */
    public static final String PICTURE_PATH = ROOT_PATH + "picture/";

    /**
     * apk 更新下载包目录
     */
    public static final String APK_PATH = ROOT_PATH + "apk/";

    /**
     * video 暂存目录
     */
    public static final String VIDEO_PATH = ROOT_PATH + "video/";

    /**
     * 日志目录
     */
    public static final String LOG_PATH = ROOT_PATH + "log/";

    /**
     * db目录
     */
    public static final String DB_PATH = ROOT_PATH + "db/";

    /**
     * 图片最大边界
     */
    public static final int[] MAX_IMAGE_BOUNDS = new int[]{720, 1280};

    /**
     * 图片压缩后的最大文件大小，单位为KB
     */
    public static final int MAX_IMAGE_SIZE = 300;

    /**
     * Log输出开关,true为输出log，false为关闭。上线时候需要改为false
     */
    public static final boolean DEBUG = BuildConfig.DEBUG;

    /**
     * 国际版为true ｜ 国内版为false
     */
    public static final boolean INTERNATIONAL = false;

    public static final String PLATFORM_TYPE = "android";

    /**
     * 常量token
     */
    public static final String TOKEN = "token";

    /**
     * 常量uid
     */
    public static final String UID = "uid";

    /**
     * 服务器时间
     */
    public static final String SERVER_DATE = "server_date";

    /**
     * 极光推送成功的标示字符
     */
    public static final String JPUSH_SET_SUCCESS = "jpush_alias_success";

    /**
     * 再次运行程序
     */
    public static final String RUN_AGAIN = "isAgainRun";

    /**
     * 网络连接超时时间，单位为毫秒（15s）
     */
    public static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000;

    /**
     * 网络读写超时时间，单位为毫秒(15s)
     */
    public static final int READ_TIMEOUT_MILLIS = 15 * 1000;


    /**
     * session
     */
    public static final String SESSION = "SESSION";

    /**
     * 日志名字
     */
    public static final String LOG_FILE_NAME = "netposa_tujie.log";
}
