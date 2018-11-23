/**
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.common.entity;

/**
 * 作者：赵炜
 * 创建日期：2016/10/10
 * 邮箱：zhaowei0920@lingdanet.com
 * 描述：PopWindow 实体类
 */

public class PopupWindowEntity {

    private String mText;
    private int mResId;
    private String mData;

    public PopupWindowEntity(String mText, int mResId, String mData) {
        this.mText = mText;
        this.mResId = mResId;
        this.mData = mData;
    }

    public PopupWindowEntity(String mText, String mData) {
        this.mText = mText;
        this.mData = mData;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        this.mResId = resId;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        this.mData = data;
    }

}
