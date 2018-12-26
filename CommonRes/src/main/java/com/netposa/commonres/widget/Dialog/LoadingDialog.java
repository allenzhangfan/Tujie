/*
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.commonres.widget.Dialog;

import android.content.Context;
import android.widget.TextView;

import com.netposa.commonres.R;

import androidx.annotation.StringRes;


/**
 * 作者：陈新明
 * 创建日期：2016/9/29
 * 邮箱：herewinner@163.com
 * 描述：全局的Loading图
 */

public class LoadingDialog extends BaseDialog {
    private TextView mTvShowText;

    public LoadingDialog(Context context) {
        super(context, R.layout.loading_dialog, R.style.Base_Dialog_Loading);
    }

    @Override
    protected void initView() {
        mTvShowText = findViewById(R.id.show_msg_text);
    }

    @Override
    protected void initData() {
        if (null != mTvShowText) {
            mTvShowText.setText(R.string.loading);
        }
    }

    @Override
    protected void setListener() {

    }

    public void setShowText(@StringRes int resId) {
        mTvShowText.setText(resId);
    }

    public void setShowText(String showText) {
        mTvShowText.setText(showText);
    }
}
