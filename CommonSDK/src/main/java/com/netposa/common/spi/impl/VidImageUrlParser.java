package com.netposa.common.spi.impl;

import android.text.TextUtils;

import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.spi.ImageUrlParser;
import com.netposa.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Keep;

/**
 * Created by yexiaokang on 2018/9/28.
 */
@Keep
public class VidImageUrlParser implements ImageUrlParser {

    private static final String FORMAT = "%1$sDownLoadFile?filename=";
    private static final String DEFAULT_IMAGE_URL = "http://86.87.0.54:6551/";

    private List<Filter> mFilters = new ArrayList<>();
    private Map<String, String> mPrefixCache = new HashMap<>();

    public VidImageUrlParser() {
        mFilters.add(new Filter("a", "aLOC", FORMAT));
        mFilters.add(new Filter("b", "bLOC", FORMAT));
        mFilters.add(new Filter("bimg", "bimgLOC", FORMAT));
        mFilters.add(new Filter("", "LOC", FORMAT));
    }

    @Override
    public String onGetImageUrl(String original) {
        if (TextUtils.isEmpty(original)) {
            return "";
        }
        String imageUrl = SPUtils.getInstance().getString(GlobalConstants.IMAGE_URL, DEFAULT_IMAGE_URL);
        if (!imageUrl.endsWith("/")) {
            imageUrl = imageUrl.concat("/");
        }

        for (Filter filter : mFilters) {
            if (original.startsWith(filter.sign)) {
                String baseUrl = mPrefixCache.get(filter.sign);
                if (TextUtils.isEmpty(baseUrl)) {
                    baseUrl = String.format(filter.mark, imageUrl);
                    mPrefixCache.put(filter.sign, baseUrl);
                }
                if (TextUtils.isEmpty(filter.replace)) {
                    return baseUrl.concat(original);
                } else {
                    return original.replaceFirst(filter.replace, baseUrl);
                }
            }
        }
        return "";
    }


    private static class Filter {

        private final String replace;
        private final String sign;
        private final String mark;

        Filter(String replace, String sign, String mark) {
            this.replace = replace;
            this.sign = sign;
            this.mark = mark;
        }
    }
}
