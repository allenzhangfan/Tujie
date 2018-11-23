package com.netposa.component.jq.mvp.ui.activity;

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
import com.netposa.component.jq.R2;
import com.netposa.component.jq.di.component.DaggerControlTaskDetailComponent;
import com.netposa.component.jq.di.module.ControlTaskDetailModule;
import com.netposa.component.jq.mvp.contract.ControlTaskDetailContract;
import com.netposa.component.jq.mvp.presenter.ControlTaskDetailPresenter;
import com.netposa.component.jq.R;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class ControlTaskDetailActivity extends BaseActivity<ControlTaskDetailPresenter> implements ControlTaskDetailContract.View {


    @BindView(R2.id.title_tv)
    TextView mTitle_txt;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerControlTaskDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .controlTaskDetailModule(new ControlTaskDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_control_task_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mTitle_txt.setText(this.getString(R.string.task_details));
    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.head_left_iv})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.head_left_iv) {
            killMyself();
        }
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
