package com.netposa.component.my.mvp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.log.Log;
import com.netposa.common.utils.AppUtils;
import com.netposa.common.utils.SPUtils;
import com.netposa.commonres.widget.Dialog.UpdateDialog;
import com.netposa.component.my.R;
import com.netposa.component.my.R2;
import com.netposa.component.my.di.component.DaggerAboutComponent;
import com.netposa.component.my.di.module.AboutModule;
import com.netposa.component.my.mvp.contract.AboutContract;
import com.netposa.component.my.mvp.model.entity.UpdateInfoEntity;
import com.netposa.component.my.mvp.presenter.AboutPresenter;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.common.constant.GlobalConstants.KEY_BASE_URL;

public class AboutActivity extends BaseActivity<AboutPresenter> implements AboutContract.View {

    @BindView(R2.id.title_tv)
    TextView mTVtTitle;
    @BindView(R2.id.app_version)
    TextView mTvVersion;

    private int mCurrentVersion;
    private Dialog mUpdateDialog;
    private String mTruePath;

    @Inject
    RxErrorHandler mErrorHandler;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAboutComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .aboutModule(new AboutModule(this, this))
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
        mCurrentVersion = DeviceUtils.getVersionCode(this);
        String appVersion = String.format(getResources().getString(R.string.app_version), versionName);
        mTvVersion.setText(appVersion);
    }


    @Override
    public void getUpdateJsonSuc(UpdateInfoEntity entity) {
        int updateVersion = entity.getVersionNumber();
        boolean forceUpdate = entity.isForceUpdate();
        int minVersion = entity.getMinVersion();
        String description = entity.getVersionDescription();
        if (!TextUtils.isEmpty(entity.getRelativePath())) {
            String path = entity.getRelativePath();
            String rePath = path.substring(1, path.length());
            mTruePath = UrlConstant.sBaseUrl + rePath;
            Log.i(TAG, mTruePath);
            if (mCurrentVersion < updateVersion) {
                if (mCurrentVersion < minVersion) {
                    showUpdateDialog(true, description, mTruePath);
                } else {
                    showUpdateDialog(forceUpdate, description, mTruePath);
                }
            } else {
                showMessage(getString(R.string.latest_version));
            }
        } else {
            showMessage(getString(R.string.no_new_version));
        }
    }

    private void showUpdateDialog(boolean isForce, String description, String path) {

        mUpdateDialog = UpdateDialog.showUpgradeDialog(isForce, description, this,
                v -> {
                    downloadApk(path, isForce);
                }, v -> {
                    if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
                        mUpdateDialog.dismiss();
                        mUpdateDialog = null;
                    }
                });

    }

    private void downloadApk(String path, boolean isForce) {
        if (!isForce) {
            if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
                mUpdateDialog.dismiss();
                mUpdateDialog = null;
            }
        }
        AppUtils.gotoDownloadApk(path);
    }

    @Override
    public void getUpdateJsonFail() {

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

    @OnClick({R2.id.head_left_iv, R2.id.btn_update})
    public void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.head_left_iv) {
            killMyself();
        } else if (id == R.id.btn_update) {
            mPresenter.getUpdateJson(this, mErrorHandler);
        }
    }
}
