package com.netposa.component.my.mvp.ui.activity;

import android.os.Bundle;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.netposa.component.my.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/26 17:11
 */
public class MyActivity extends BaseActivity {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_my;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }
}
