package com.netposa.component.clcx.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.contract.CarRecordContract;
import com.netposa.component.clcx.mvp.model.entity.CarDetailRequestEntity;
import com.netposa.component.clcx.mvp.model.entity.CarDetailResponseEntity;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.entity.DetectionInfoEntity;
import com.netposa.component.room.entity.IsCallPhoneEntity;
import com.netposa.component.room.entity.MarkerTypeEntity;
import com.netposa.component.room.entity.MoveDirectionEntity;
import com.netposa.component.room.entity.MoveSpeedEntity;
import com.netposa.component.room.entity.PeccancyTypeEntity;
import com.netposa.component.room.entity.PlateColorEntity;
import com.netposa.component.room.entity.SafetyBeltNumEntity;
import com.netposa.component.room.entity.SpecialCarEntity;
import com.netposa.component.room.entity.VehicleBrandEntity;
import com.netposa.component.room.entity.VehicleNKEntity;
import com.netposa.component.room.entity.VehicleNKVMEntity;
import com.netposa.component.room.entity.VehicleSubBrandEntity;
import com.netposa.component.room.entity.VehicleSunVisorNumEntity;
import com.netposa.component.room.entity.VehicleTypeEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class CarRecordPresenter extends BasePresenter<CarRecordContract.Model, CarRecordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    Context mContext;
    @Inject
    CarDetailRequestEntity entity;

    @Inject
    public CarRecordPresenter(CarRecordContract.Model model, CarRecordContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void getDetailInfo(String recordId) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        List<String> mData = new ArrayList<>();
        mData.add(recordId);
        entity.setRecordId(mData);
        mModel.getCarDetail(entity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<List<CarDetailResponseEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<CarDetailResponseEntity> responseEntity) {
                        Log.d("getDetailInfo：", responseEntity.toString());
                        mRootView.getSuceese(responseEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("Throwable", t.toString());
                        mRootView.getFailed();
                    }
                });
    }

//    //获取标志物
//    @SuppressLint("CheckResult")
//    public List<MarkerTypeEntity> getMakerList() {
//        mDbHelper.getMarkerType()
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading("");//显示下拉刷新的进度条
//                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally(() -> {
//                    mRootView.hideLoading();//隐藏下拉刷新的进度条
//                })
//                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
//                .subscribe((List<MarkerTypeEntity> entities) -> {
//                    Log.e(TAG, "getMakerList :" + entities);
//                    mMakerList.addAll(entities);
//                });
//        return mMakerList;
//    }
}
