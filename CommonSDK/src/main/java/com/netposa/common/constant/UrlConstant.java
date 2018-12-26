package com.netposa.common.constant;

import com.netposa.common.BuildConfig;
import com.netposa.common.spi.UrlParser;

import java.util.ServiceLoader;

public final class UrlConstant {

    private static final UrlParser URL_PARSER = ServiceLoader.load(UrlParser.class).iterator().next();

    /**
     * baseurl
     */
    public static String sBaseUrl = BuildConfig.BASE_URL;

    /**
     * 图片服务器url
     * @param encryptUrl
     * @return
     */
    public static String parseImageUrl(String encryptUrl) {
        return URL_PARSER.onGetImageUrl(encryptUrl);
    }

    public static String parseLocationImageUrlSuffix() {
        return URL_PARSER.onGetLocationImageUrlSuffix();
    }

    /**
     * 视频播放url
     * @param encryptUrl
     * @return
     */
    public static String parsePlayUrl(String encryptUrl) {
        return URL_PARSER.onGetPlayUrl(encryptUrl);
    }

    /**
     * mqtt url
     * @param originUrl
     * @return
     */
    public static String parseMqttUrl(String originUrl) {
        return URL_PARSER.onGetMqttUrl(originUrl);
    }

}
