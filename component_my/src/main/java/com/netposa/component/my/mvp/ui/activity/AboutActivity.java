package com.netposa.component.my.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;
import com.netposa.component.my.R2;
import com.netposa.component.my.di.component.DaggerAboutComponent;
import com.netposa.component.my.di.module.AboutModule;
import com.netposa.component.my.mvp.contract.AboutContract;
import com.netposa.component.my.mvp.presenter.AboutPresenter;
import com.netposa.component.my.R;
import static com.jess.arms.utils.Preconditions.checkNotNull;

public class AboutActivity extends BaseActivity<AboutPresenter> implements AboutContract.View {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.app_version)
    TextView mTvVersion;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAboutComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .aboutModule(new AboutModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_about; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTVtTitle.setText(R.string.about);
        String versionName = DeviceUtils.getVersionName(this);
        String appVersion = String.format(getResources().getString(R.string.app_version), versionName);
        mTvVersion.setText(appVersion);
    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

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

    @OnClick({R2.id.head_left_iv})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        }
    }
}
