package com.netposa.component.jq.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.core.RouterHub;
import com.netposa.common.utils.BarViewUtils;
import com.netposa.component.jq.di.component.DaggerCapturePictureComponent;
import com.netposa.component.jq.di.module.CapturePictureModule;
import com.netposa.component.jq.mvp.contract.CapturePictureContract;
import com.netposa.component.jq.mvp.presenter.CapturePicturePresenter;
import com.netposa.component.jq.R;
import com.netposa.component.pic.R2;
import static com.jess.arms.utils.Preconditions.checkNotNull;

@Route(path = RouterHub.PIC_CAPTURE_PICTURE_ACTIVITY)
public class CapturePictureActivity extends BaseActivity<CapturePicturePresenter> implements CapturePictureContract.View {

    @BindView(com.netposa.component.jq.R2.id.title_tv)
    TextView mTVtTitle;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCapturePictureComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .capturePictureModule(new CapturePictureModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_capture_picture; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTVtTitle.setText(R.string.capture_image);
        BarViewUtils.hideNavigationBar(this);
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
    public void onClickView(View v) {
        int id = v.getId();
        if (id == R.id.head_left_iv) {
            super.onBackPressed();
            killMyself();
        }
    }
}
