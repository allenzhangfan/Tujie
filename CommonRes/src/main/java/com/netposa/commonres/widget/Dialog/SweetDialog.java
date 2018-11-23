package com.netposa.commonres.widget.Dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.netposa.commonres.R;


/**
 * 作者：安兴亚
 * 创建日期：2016/10/30
 * 邮箱：anxy0820@lingdanet.com
 * 描述：提示Dialog，继承自BaseDialog
 */
public class SweetDialog extends BaseDialog {

    protected TextView mPositive;
    private TextView mTitle;
    private TextView mMessage;
    private TextView mNegative;
    private View mNegativeSplitView;

    public SweetDialog(Context context) {
        super(context, R.layout.sweet_dialog, R.style.Base_Dialog);
    }

    @Override
    protected void initView() {
        mTitle = (TextView) findViewById(R.id.sd_title);
        mMessage = (TextView) findViewById(R.id.sd_message);
        mPositive = (TextView) findViewById(R.id.sd_positive);
        mNegative = (TextView) findViewById(R.id.sd_negative);
        mNegativeSplitView = findViewById(R.id.sd_negative_split_view);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    public void setNegativeGone() {
        mNegative.setVisibility(View.GONE);
        mNegativeSplitView.setVisibility(View.GONE);
    }

    public void setPositiveListener(View.OnClickListener listener) {
        mPositive.setOnClickListener(listener);
    }

    public void setNegativeListener(View.OnClickListener listener) {
        if (listener != null) {
            mNegative.setOnClickListener(listener);
        } else {
            mNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setMessage(String message) {
        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);
    }

    public void setNegative(String negative) {
        mNegative.setText(negative);
    }

    public void setPositive(String positive) {
        mPositive.setText(positive);
    }
}
