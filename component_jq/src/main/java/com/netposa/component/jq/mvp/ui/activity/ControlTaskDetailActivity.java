package com.netposa.component.jq.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.jq.R;
import com.netposa.component.jq.R2;
import com.netposa.component.jq.di.component.DaggerControlTaskDetailComponent;
import com.netposa.component.jq.di.module.ControlTaskDetailModule;
import com.netposa.component.jq.mvp.contract.ControlTaskDetailContract;
import com.netposa.common.entity.push.JqItemEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmDetailForIdResponseEntity;
import com.netposa.component.jq.mvp.presenter.ControlTaskDetailPresenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.jq.app.JqConstants.KEY_DETAILS;
import static com.netposa.component.jq.app.JqConstants.LEVAL_1;
import static com.netposa.component.jq.app.JqConstants.LEVAL_2;
import static com.netposa.component.jq.app.JqConstants.LEVAL_3;

public class ControlTaskDetailActivity extends BaseActivity<ControlTaskDetailPresenter> implements ControlTaskDetailContract.View {


    @BindView(R2.id.title_tv)
    TextView mTitle_txt;
    @BindView(R2.id.car_ku_type)
    TextView mCarKuType;
    @BindView(R2.id.capture_leval)
    TextView mCaptureLeval;
    @BindView(R2.id.remark_target)
    TextView mRemarkTarget;
    @BindView(R2.id.remark_reason)
    TextView mRemarkReason;
    @BindView(R2.id.start_time)
    TextView mStartTime;
    @BindView(R2.id.end_time)
    TextView mEndTime;

    private AlarmDetailForIdResponseEntity mEntity;

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
        Intent data = getIntent();
        if (data == null) {
            Log.e(TAG, "intent data is null,please check !");
            return;
        }
        mEntity = getIntent().getParcelableExtra(KEY_DETAILS);
        mCarKuType.setText(mEntity.getLibName());
        mStartTime.setText(TimeUtils.millis2String(mEntity.getStartTime()));
        mEndTime.setText(TimeUtils.millis2String(mEntity.getEndTime()));
        if (LEVAL_1.equals(mEntity.getAlarmLevel())) {
            mCaptureLeval.setText(getString(R.string.leval_1));
        } else if (LEVAL_2.equals(mEntity.getAlarmLevel())) {
            mCaptureLeval.setText(getString(R.string.leval_2));
        } else if (LEVAL_3.equals(mEntity.getAlarmLevel())) {
            mCaptureLeval.setText(getString(R.string.leval_3));
        }
        mRemarkTarget.setText(mEntity.getTarget()); //目标
        mRemarkReason.setText(mEntity.getTaskReason());//原因
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
