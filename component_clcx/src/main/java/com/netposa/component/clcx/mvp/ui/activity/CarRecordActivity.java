package com.netposa.component.clcx.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.commonres.modle.LoadingDialog;
import com.netposa.commonres.widget.Dialog.SweetDialog;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.R2;
import com.netposa.component.clcx.di.component.DaggerCarRecordComponent;
import com.netposa.component.clcx.di.module.CarRecordModule;
import com.netposa.component.clcx.mvp.contract.CarRecordContract;
import com.netposa.component.clcx.mvp.model.entity.CarDividerEntity;
import com.netposa.component.clcx.mvp.model.entity.CarInfoEntity;
import com.netposa.component.clcx.mvp.model.entity.CarNameEntity;
import com.netposa.component.clcx.mvp.presenter.CarRecordPresenter;
import com.netposa.component.clcx.mvp.ui.adapter.CarRecordAdapter;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class CarRecordActivity extends BaseActivity<CarRecordPresenter> implements CarRecordContract.View {

    @BindView(R2.id.head_left_iv)
    ImageButton mHeadLeftIv;
    @BindView(R2.id.title_tv)
    TextView mTitleTv;
    @BindView(R2.id.head_right_tv)
    TextView mHeadRightTv;
    @BindView(R2.id.rv_car_info)
    RecyclerView mRvCarInfo;

    @Inject
    LoadingDialog mLoadingDialog;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.ItemAnimator mItemAnimator;
    @Inject
    List<MultiItemEntity> mBeanList;
    @Inject
    CarRecordAdapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCarRecordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .carRecordModule(new CarRecordModule(this, this))
                .build()
                .inject(this);
    }

    @Override
    public int initContentLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_car_record; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mTitleTv.setText(R.string.clcx_record);
        mHeadRightTv.setVisibility(View.VISIBLE);
        mHeadRightTv.setTextColor(this.getResources().getColor(R.color.app_theme_color));
        mHeadRightTv.setText(getString(R.string.wheel_path));
        mRvCarInfo.setLayoutManager(mLayoutManager);
        mRvCarInfo.setItemAnimator(mItemAnimator);
        mRvCarInfo.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> showMessage("跳转定位界面"));
        //todo
        CarDividerEntity entity1=new CarDividerEntity();
        mBeanList.add(entity1);

        CarNameEntity entity2=new CarNameEntity();
        entity2.setName(getString(R.string.clcx_car_info));
        mBeanList.add(entity2);

        CarInfoEntity entity3=new CarInfoEntity();
        entity3.setTitle(getString(R.string.clcx_car_num));
        entity3.setDividerVisable(true);
        entity3.setGpsVisable(false);
        entity3.setDescription("京A 888888");
        mBeanList.add(entity3);

        CarInfoEntity entity4=new CarInfoEntity();
        entity4.setTitle(getString(R.string.clcx_car_speed));
        entity4.setDividerVisable(true);
        entity4.setGpsVisable(false);
        entity4.setDescription("10KM/h");
        mBeanList.add(entity4);

        CarInfoEntity entity5=new CarInfoEntity();
        entity5.setTitle(getString(R.string.clxc_cross_device));
        entity5.setDividerVisable(true);
        entity5.setGpsVisable(true);
        entity5.setDescription("西安基地研发南门");
        mBeanList.add(entity5);

        CarInfoEntity entity6=new CarInfoEntity();
        entity6.setTitle(getString(R.string.clxc_device_type));
        entity6.setDividerVisable(true);
        entity6.setGpsVisable(false);
        entity6.setDescription("球机");
        mBeanList.add(entity6);

        CarInfoEntity entity7=new CarInfoEntity();
        entity7.setTitle(getString(R.string.violation_information));
        entity7.setDividerVisable(true);
        entity7.setGpsVisable(false);
        entity7.setDescription("未识别");
        mBeanList.add(entity7);

        CarInfoEntity entity8=new CarInfoEntity();
        entity8.setTitle(getString(R.string.clcx_capture_time));
        entity8.setDividerVisable(false);
        entity8.setGpsVisable(false);
        entity8.setDescription("2018-02-22 11:38:20");
        mBeanList.add(entity8);

        CarDividerEntity entity9=new CarDividerEntity();
        mBeanList.add(entity9);

        CarNameEntity entity10=new CarNameEntity();
        entity10.setName(getString(R.string.clcx_second_car_info));
        mBeanList.add(entity10);

        CarInfoEntity entity11=new CarInfoEntity();
        entity11.setTitle(getString(R.string.sport_speed));
        entity11.setDividerVisable(true);
        entity11.setGpsVisable(false);
        entity11.setDescription("快速");
        mBeanList.add(entity11);

        CarInfoEntity entity12=new CarInfoEntity();
        entity12.setTitle(getString(R.string.sport_direction));
        entity12.setDividerVisable(true);
        entity12.setGpsVisable(false);
        entity12.setDescription("向下");
        mBeanList.add(entity12);

        CarInfoEntity entity13=new CarInfoEntity();
        entity13.setTitle(getString(R.string.plate_number_color));
        entity13.setDividerVisable(true);
        entity13.setDescription("蓝色");
        entity13.setGpsVisable(false);
        mBeanList.add(entity13);

        CarInfoEntity entity14=new CarInfoEntity();
        entity14.setTitle(getString(R.string.car_type));
        entity14.setDividerVisable(true);
        entity14.setDescription("轿车");
        entity14.setGpsVisable(false);
        mBeanList.add(entity14);

        CarInfoEntity entity15=new CarInfoEntity();
        entity15.setTitle(getString(R.string.car_grand));
        entity15.setDividerVisable(true);
        entity15.setDescription("日产");
        entity15.setGpsVisable(false);
        mBeanList.add(entity15);

        CarInfoEntity entity16=new CarInfoEntity();
        entity16.setTitle(getString(R.string.car_child_type));
        entity16.setDividerVisable(true);
        entity16.setDescription("天籁");
        entity16.setGpsVisable(false);
        mBeanList.add(entity16);

        CarInfoEntity entity17=new CarInfoEntity();
        entity17.setTitle(getString(R.string.car_year_type));
        entity17.setDividerVisable(true);
        entity17.setDescription("天籁");
        entity17.setGpsVisable(false);
        mBeanList.add(entity17);

        CarInfoEntity entity18=new CarInfoEntity();
        entity18.setTitle(getString(R.string.is_special_car));
        entity18.setDividerVisable(true);
        entity18.setDescription("未识别");
        entity18.setGpsVisable(false);
        mBeanList.add(entity18);

        CarInfoEntity entity19=new CarInfoEntity();
        entity19.setTitle(getString(R.string.safety_belt));
        entity19.setDividerVisable(true);
        entity19.setDescription("未识别");
        entity19.setGpsVisable(false);
        mBeanList.add(entity19);

        CarInfoEntity entity20=new CarInfoEntity();
        entity20.setTitle(getString(R.string.annual_survey));
        entity20.setDividerVisable(true);
        entity20.setDescription("未识别");
        entity20.setGpsVisable(false);
        mBeanList.add(entity20);

        CarInfoEntity entity21=new CarInfoEntity();
        entity21.setTitle(getString(R.string.is_telephone));
        entity21.setDividerVisable(true);
        entity21.setDescription("是");
        entity21.setGpsVisable(false);
        mBeanList.add(entity21);

        CarInfoEntity entity22=new CarInfoEntity();
        entity22.setTitle(getString(R.string.sun_visor));
        entity22.setDividerVisable(true);
        entity22.setDescription("白天未开启");
        entity22.setGpsVisable(false);
        mBeanList.add(entity22);

        CarInfoEntity entity23=new CarInfoEntity();
        entity23.setTitle(getString(R.string.is_visor_face));
        entity23.setDividerVisable(true);
        entity23.setDescription("是");
        mBeanList.add(entity23);

        CarInfoEntity entity24=new CarInfoEntity();
        entity24.setTitle(getString(R.string.other_marker));
        entity24.setDividerVisable(false);
        entity24.setGpsVisable(false);
        entity24.setDescription("无");
        mBeanList.add(entity24);

    }

    @Override
    public void showLoading(String message) {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.dismiss();
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

    @OnClick({R2.id.head_left_iv, R2.id.head_right_tv})
    public void onViewClicked(View view) {
        int id=view.getId();
        if(id==R.id.head_left_iv){
            killMyself();
        }else if(id==R.id.head_right_tv){
            showMessage("跳转行车轨迹");
        }

    }
    @Override
    public void killMyself() {
        finish();
    }

}
