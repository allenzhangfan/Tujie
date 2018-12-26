package com.netposa.common.spi;

import androidx.annotation.Keep;

/**
 * Created by yexiaokang on 2018/9/28.
 */
@Keep
public interface UrlParser {

    String onGetImageUrl(String originalUrl);

    String onGetLocationImageUrlSuffix();

    String onGetPlayUrl(String originalUrl);

    String onGetMqttUrl(String originalUrl);
}
