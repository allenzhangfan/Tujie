package com.netposa.component.jq.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.commonres.widget.CircleProgressView;
import com.netposa.component.jq.R2;
import com.netposa.component.jq.di.component.DaggerAlarmDetailsComponent;
import com.netposa.component.jq.di.module.AlarmDetailsModule;
import com.netposa.component.jq.mvp.contract.AlarmDetailsContract;
import com.netposa.component.jq.mvp.model.entity.JqItemEntity;
import com.netposa.component.jq.mvp.presenter.AlarmDetailsPresenter;
import com.netposa.component.jq.R;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.netposa.component.jq.app.JqConstants.KEY_JQ_ITEM;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_INVALID;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_PEOPLE;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_SUSPENDING;
import static com.netposa.component.jq.mvp.model.entity.JqItemEntity.TYPE_VALID;

public class AlarmDetailsActivity extends BaseActivity<AlarmDetailsPresenter> implements AlarmDetailsContract.View {

    @BindView(R2.id.title_tv)
    TextView mTitle_txt;
    @BindView(R2.id.ll_confirm)
    LinearLayout mLlConfirm;
    @BindView(R2.id.ll_jq_person_details)
    LinearLayout mLlJqPersonDetails;
    @BindView(R2.id.ll_jq_car_details)
    LinearLayout mLlJqCarDetails;
    @BindView(R2.id.similar_progresview)
    CircleProgressView mCircleProgressView;
    @BindView(R2.id.tv_similarity)
    TextView mTvSimilarity;
    @BindView(R2.id.tv_car_number)
    TextView mTvCarNumber;
    @BindView(R2.id.tv_detail_type_car)
    TextView mTvDetailTypeCar;
    @BindView(R2.id.tv_detail_type_person)
    TextView mTvDetailTypePerson;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAlarmDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .alarmDetailsModule(new AlarmDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_alarm_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitle_txt.setText(this.getString(R.string.alarm_car_details));
        mTitle_txt.getPaint().setFakeBoldText(true);
        JqItemEntity jqItemEntity = getIntent().getParcelableExtra(KEY_JQ_ITEM);
        initLayout(jqItemEntity);

    }

    private void initLayout(JqItemEntity itemEntity) {
        int itemType = itemEntity.getItemType();//判断是车还是人
        int itemHandleType = itemEntity.getItemHandleType();//判断有效、无效、待处理

        if (itemType == TYPE_PEOPLE) {
            mLlJqPersonDetails.setVisibility(View.VISIBLE);
            mLlJqCarDetails.setVisibility(View.GONE);
            mCircleProgressView.setScore(itemEntity.getSimilarity(), true);
            mTvSimilarity.setText(getResources().getString(R.string.similarity_percent,itemEntity.getSimilarity()));
        } else {
            mLlJqPersonDetails.setVisibility(View.GONE);
            mLlJqCarDetails.setVisibility(View.VISIBLE);
            mTvCarNumber.setText(itemEntity.getCarNumber());
        }

        if (itemHandleType == TYPE_VALID) {
            mLlConfirm.setVisibility(View.GONE);
            mTvDetailTypeCar.setText(getString(R.string.valid));
            mTvDetailTypeCar.setTextColor(ContextCompat.getColor(this, R.color.jq_valid));
            mTvDetailTypeCar.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_effective_bg));
            mTvDetailTypePerson.setText(getString(R.string.valid));
            mTvDetailTypePerson.setTextColor(ContextCompat.getColor(this, R.color.jq_valid));
            mTvDetailTypePerson.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_effective_bg));
        } else if (itemHandleType == TYPE_INVALID) {
            mLlConfirm.setVisibility(View.GONE);
            mTvDetailTypeCar.setText(getString(R.string.invalid));
            mTvDetailTypeCar.setTextColor(ContextCompat.getColor(this, R.color.color_hint_text));
            mTvDetailTypeCar.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_uneffect_bg));
            mTvDetailTypePerson.setText(getString(R.string.invalid));
            mTvDetailTypePerson.setTextColor(ContextCompat.getColor(this, R.color.color_hint_text));
            mTvDetailTypePerson.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_uneffect_bg));
        } else if (itemHandleType == TYPE_SUSPENDING) {
            mLlConfirm.setVisibility(View.VISIBLE);
            mTvDetailTypeCar.setText(getString(R.string.suspending));
            mTvDetailTypeCar.setTextColor(ContextCompat.getColor(this, R.color.jq_suspending));
            mTvDetailTypeCar.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_undeal_bg));
            mTvDetailTypePerson.setText(getString(R.string.suspending));
            mTvDetailTypePerson.setTextColor(ContextCompat.getColor(this, R.color.jq_suspending));
            mTvDetailTypePerson.setBackground(ContextCompat.getDrawable(this, R.drawable.alarm_undeal_bg));
        }
    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.head_left_iv, R2.id.control_car_type, R2.id.camera_address})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.head_left_iv) {
            killMyself();
        } else if (i == R.id.control_car_type) {
            // 跳转到布控任务详情
            launchActivity(new Intent(this, ControlTaskDetailActivity.class));
        } else if (i == R.id.camera_address) {
            ToastUtils.showShort("跳转到地图");
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
