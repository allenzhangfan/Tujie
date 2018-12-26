package com.netposa.component.jq.mvp.presenter;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.netposa.common.constant.UrlConstant;
import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.jq.R;
import com.netposa.component.jq.mvp.contract.FaceDeployContract;
import com.netposa.component.jq.mvp.model.entity.AlarmListRequestEntity;
import com.netposa.component.jq.mvp.model.entity.AlarmListResponseEntity;
import com.netposa.common.entity.push.AlarmPeopleDetailResponseForExt;
import com.netposa.common.entity.push.JqItemEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;

import static com.netposa.common.constant.GlobalConstants.TYPE_FAMALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_MALE;
import static com.netposa.common.constant.GlobalConstants.TYPE_FACE_DEPLOY;

@FragmentScope
public class FaceDeployPresenter extends BasePresenter<FaceDeployContract.Model, FaceDeployContract.View> {
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
    Gson mGson;
    @Inject
    AlarmListRequestEntity mRequest;

    @Inject
    public FaceDeployPresenter(FaceDeployContract.Model model, FaceDeployContract.View rootView) {
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

    public void getlistAlarmInfo() {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getlistAlarmInfo(mRequest)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<AlarmListResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(AlarmListResponseEntity reponse) {
                        List<AlarmListResponseEntity.ListEntity> dataList = reponse.getList();
                        Log.i(TAG, "dataList size:" + dataList.size());
                        List<JqItemEntity> newDataList = transformJqItemEntities(dataList);
                        mRootView.getlistAlarmInfoSuccess(newDataList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.getlistAlarmInfoFail();
                    }
                });
    }

    private List<JqItemEntity> transformJqItemEntities(List<AlarmListResponseEntity.ListEntity> dataList) {
        List<JqItemEntity> newDataList = new ArrayList<>(dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            AlarmListResponseEntity.ListEntity listEntity = dataList.get(i);
            JqItemEntity itemEntity = new JqItemEntity();
            String ext = listEntity.getExt();
            String taskName = listEntity.getTaskName();
            String alarmAddress = listEntity.getAlarmAddress();
            String note = listEntity.getNote();
            String id = listEntity.getId();
            String imageUrl = listEntity.getImageUrl();
            String scenneImg =listEntity.getSceneImg();
            String taskReason = listEntity.getTaskReason();
            String alarmLevel = listEntity.getAlarmLevel();
            String alarmStatus = listEntity.getAlarmStatus();
            String target = listEntity.getTarget();

            if(!TextUtils.isEmpty(ext)){
                AlarmPeopleDetailResponseForExt peopleTemp = parseUrl(ext);
                itemEntity.setDeployImgUrl(UrlConstant.parseImageUrl(peopleTemp.getTargetImage()));
                if (TextUtils.isEmpty(peopleTemp.getSex())) {
                    itemEntity.setCaptureGender(mContext.getString(R.string.other));
                } else if (TYPE_MALE.equals(peopleTemp.getSex())) {
                    itemEntity.setCaptureGender(mContext.getString(R.string.male));
                } else if (TYPE_FAMALE.equals(peopleTemp.getSex())) {
                    itemEntity.setCaptureGender(mContext.getString(R.string.famale));
                }
                itemEntity.setCaptureIdCard(peopleTemp.getIdcard());
                itemEntity.setCaptureAdress(peopleTemp.getCensus());//户籍地址
                itemEntity.setCaptureLib(peopleTemp.getLibName());//库名称
                itemEntity.setCaptureDetailInfo(peopleTemp.getRemark()); //详细信息
                itemEntity.setCaptureName(peopleTemp.getName());
            }

            itemEntity.setItemType(TYPE_FACE_DEPLOY);
            itemEntity.setSimilarity(listEntity.getScore());

            if (!TextUtils.isEmpty(taskName)) {
                itemEntity.setCaptureLibName(taskName); //布控名称
            }

            if (!TextUtils.isEmpty(alarmAddress)) {
                itemEntity.setCameraLocation(alarmAddress);
            }
            if (!TextUtils.isEmpty(note)) {
                itemEntity.setCapturetTipMsg(note);//备注
            }

            if (!TextUtils.isEmpty(id)) {
                itemEntity.setId(id);
            }
            if (!TextUtils.isEmpty(imageUrl)) {
                itemEntity.setCaptureImgUrl(UrlConstant.parseImageUrl(imageUrl));
            }
            if(!TextUtils.isEmpty(scenneImg)){
                itemEntity.setScenneImg(UrlConstant.parseImageUrl(scenneImg));
            }
            if (!TextUtils.isEmpty(taskReason)) {
                itemEntity.setTaskReason(taskReason);
            }

            if (!TextUtils.isEmpty(alarmLevel)) {
                itemEntity.setLevel(alarmLevel);
            }

            if (!TextUtils.isEmpty(alarmStatus)) {
                itemEntity.setItemHandleType(
                        Integer.valueOf(alarmStatus));
            }
            if(!TextUtils.isEmpty(target)){
                itemEntity.setTarget(target); //目标
            }
            itemEntity.setLatitude(listEntity.getLatitudeX());
            itemEntity.setLongitude(listEntity.getLongitudeX());
            itemEntity.setStartTime(TimeUtils.millis2String(listEntity.getStartTime()));
            itemEntity.setEndTime(TimeUtils.millis2String(listEntity.getEndTime()));
            itemEntity.setAlarmTime(TimeUtils.millis2String(listEntity.getAlarmTime()));
            newDataList.add(itemEntity);
        }
        return newDataList;
    }

    private AlarmPeopleDetailResponseForExt parseUrl(String ext) {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(ext).getAsJsonArray();//获取JsonArray对象
        ArrayList<AlarmPeopleDetailResponseForExt> beanList = new ArrayList<>(jsonElements.size());
        for (JsonElement bean : jsonElements) {
            AlarmPeopleDetailResponseForExt temp = mGson.fromJson(bean, AlarmPeopleDetailResponseForExt.class);//解析
            beanList.add(temp);
        }
        return beanList.get(0);
    }
}
