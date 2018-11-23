package com.netposa.component.login.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.KeyboardUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.component.login.BuildConfig;
import com.netposa.component.login.R2;
import com.netposa.component.login.di.component.DaggerSetUpIpComponent;
import com.netposa.component.login.di.module.SetUpIpModule;
import com.netposa.component.login.mvp.contract.SetUpIpContract;
import com.netposa.component.login.mvp.presenter.SetUpIpPresenter;
import com.netposa.component.login.R;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.login.app.LoginConstants.KEY_LOGIN_IP;
import static com.netposa.component.login.app.LoginConstants.REQUEST_CODE_LOGIN_IP;

public class SetUpIpActivity extends BaseActivity<SetUpIpPresenter> implements SetUpIpContract.View {


    @BindView(R2.id.title_tv)
    TextView mTitle_txt;
    @BindView(R2.id.input_editText)
    EditText mInputEt;
    private String mLoginIp;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSetUpIpComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .setUpIpModule(new SetUpIpModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_set_up_ip; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitle_txt.setText(this.getString(R.string.service_address));
        mTitle_txt.getPaint().setFakeBoldText(true);
        mLoginIp = mInputEt.getText().toString().trim();
        if (BuildConfig.DEBUG) {
            mInputEt.setText(R.string.edit_ip_open);
        } else {
            mInputEt.setText(R.string.edit_ip_local);
        }
    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.head_left_iv, R2.id.btn_login})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.btn_login) {
            mLoginIp = mInputEt.getText().toString().trim();
            if (TextUtils.isEmpty(mLoginIp)) {
                ToastUtils.showShort(R.string.login_plz_input_ip);
                KeyboardUtils.hideSoftInput(this, mInputEt);
                return;
            }
            Intent data = new Intent();
            data.putExtra(KEY_LOGIN_IP, mLoginIp);
            setResult(REQUEST_CODE_LOGIN_IP, data);
            killMyself();
        }
    }

    @Override
    public void onBackPressed() {
        killMyself();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
