/**
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.common.interceptor;

import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.utils.SPUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者：赵炜
 * 创建日期：2016/9/23
 * 邮箱：zhaowei0920@lingdanet.com
 * 描述：Header 拦截器,增加token统一处理
 */

public class HeaderInterceptor implements Interceptor {

    private static final String TAG = "HeaderInterceptor";

    private Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        String token = SPUtils.getInstance().getString(GlobalConstants.TOKEN);
        headers.put("user-agent", "Android");
        if (headers != null) {
            Log.d(TAG, "token:" + token);
            headers.put(GlobalConstants.TOKEN, token);
        }
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey));
            }
        }

        Response response = chain.proceed(builder.build());
        return response;
    }
}
