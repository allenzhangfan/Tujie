package com.netposa.common.spi;

import androidx.annotation.Keep;

/**
 * Created by yexiaokang on 2018/9/28.
 */
@Keep
public interface ImageUrlParser {

    String onGetImageUrl(String original);
}
