package com.netposa.common.constant;

import com.netposa.common.BuildConfig;
import com.netposa.common.spi.ImageUrlParser;

import java.util.ServiceLoader;

/**
 * 作者：安兴亚
 * 创建日期：2017/08/09
 * 邮箱：anxingya@lingdanet.com
 * 描述：Url地址常量
 */

public final class UrlConstant {

    private static final ServiceLoader<ImageUrlParser> LOADER = ServiceLoader.load(ImageUrlParser.class);

    /**
     * 测试地址
     */
    private static String BASE_URL = "http://192.168.152.206:24000/";
    // public static String BASE_URL = "http://lm-dev.lingda.com/";
    // public static String BASE_URL = "http://192.168.100.4:9091/";
    // public static String BASE_URL = "http://218.95.36.58/";

    /**
     * baseurl
     */
    public static String APP_DOMAIN = BuildConfig.BASE_URL;

    public static String parseImageUrl(String encryptUrl) {
        ImageUrlParser imageUrlParser = LOADER.iterator().next();
        return imageUrlParser.onGetImageUrl(encryptUrl);
    }

}
