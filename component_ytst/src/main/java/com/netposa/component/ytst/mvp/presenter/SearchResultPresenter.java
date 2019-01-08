package com.netposa.component.ytst.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.netposa.common.log.Log;
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.entity.YtstCarAndPeopleEntity;
import com.netposa.component.ytst.mvp.contract.SearchResultContract;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


@ActivityScope
public class SearchResultPresenter extends BasePresenter<SearchResultContract.Model, SearchResultContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private final DbHelper mDbHelper;

    @Inject
    public SearchResultPresenter(SearchResultContract.Model model, SearchResultContract.View rootView) {
        super(model, rootView);
        mDbHelper = DbHelper.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 图片上传
     *
     * @param
     */
    public void imgSearch(ImgSearchRequestEntity entity) {
        mModel.imgSearch(entity)
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
                .subscribe(new ErrorHandleSubscriber<List<ImgSearchResponseEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<ImgSearchResponseEntity> entity) {
                        Log.i(TAG, "imgSearch :" + entity);
                        mRootView.imageSearchSuccess(entity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.i(TAG, "imgSearch Failed");
                        mRootView.imageSearchuploadImageFail();
                    }
                });
    }

    public void comparatorDate(List<ImgSearchResponseEntity.DataBean> mBeanList, boolean flag) {
        Collections.sort(mBeanList, new Comparator<ImgSearchResponseEntity.DataBean>() {
            /**
             *
             * @param parm1
             * @param parm2
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            public int compare(ImgSearchResponseEntity.DataBean parm1, ImgSearchResponseEntity.DataBean parm2) {
                Date date1 = TimeUtils.millis2Date(parm1.get_absTime());
                Date date2 = TimeUtils.millis2Date(parm2.get_absTime());
                // 对日期字段进行升序before，如果欲降序可采用after方法
                if (flag) { // 降序
                    if (date1.after(date2)) {
                        return 1;
                    }
                    return -1;
                } else {// 升序
                    if (date1.before(date2)) {
                        return 1;
                    }
                    return -1;
                }
            }
        });
    }

    public void comparatorScore(List<ImgSearchResponseEntity.DataBean> mBeanList, boolean flag) {
        Collections.sort(mBeanList, new Comparator<ImgSearchResponseEntity.DataBean>() {
            /**
             *
             * @param parm1
             * @param parm2
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            public int compare(ImgSearchResponseEntity.DataBean parm1, ImgSearchResponseEntity.DataBean parm2) {
                if (flag) {//倒序
                    return parm2.getScore() - parm1.getScore();
                } else {// 正序排列
                    return parm1.getScore() - parm2.getScore();
                }
            }
        });
    }

    /**
     * 先删除数据 再插入数据
     *
     * @param entities
     */
    @SuppressLint("CheckResult")
    public void instert(List<YtstCarAndPeopleEntity> entities) {
        mDbHelper.deleteAllYtstData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(result -> {
                    instertData(entities);
                }, throwable -> {
                    Log.e("instert Fail");
                });
    }

    /**
     * 插入搜车搜人的数据
     *
     * @param deviceEntiry
     */
    public void instertData(List<YtstCarAndPeopleEntity> deviceEntiry) {
        mDbHelper.insertAllYtstData(deviceEntiry)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }

    // 数据添加到数据库里
    public void addInsterData(ArrayList<ImgSearchResponseEntity.DataBean> list) {
        List<YtstCarAndPeopleEntity> dataBean = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            YtstCarAndPeopleEntity entity = new YtstCarAndPeopleEntity();
            entity.setGender(list.get(i).getGender());
            entity.setTraitImg(list.get(i).getTraitImg());
            entity.setLatitude(list.get(i).getLatitude());
            entity.setSource(list.get(i).getSource());
            entity.setDeviceId(list.get(i).getDeviceId());
            entity.setDeviceName(list.get(i).getDeviceName());
            entity.setTraitLocation(list.get(i).getTraitLocation());
            entity.setSmile(list.get(i).getSmile());
            entity.setRecordId(list.get(i).getRecordId());
            entity.setSceneImg(list.get(i).getSceneImg());
            entity.setEyeGlass(list.get(i).getEyeGlass());
            entity.setStartTime(list.get(i).getStartTime());
            entity.setSaveTime(list.get(i).getSaveTime());
            entity.setPushTime(list.get(i).getPushTime());
            entity.setMask(list.get(i).getMask());
            entity.setLongitude(list.get(i).getLongitude());
            entity.setSunGlass(list.get(i).getSunGlass());
            entity.setAttractive(list.get(i).getAttractive());
            entity.setExpression(list.get(i).getExpression());
            entity.setConfidence(list.get(i).getConfidence());
            entity.setNodeType(list.get(i).getNodeType());
            entity.setAbsTime(list.get(i).getAbsTime());
            entity.setCameraType(list.get(i).getCameraType());
            entity.setLocation(list.get(i).getLocation());
            entity.setEndTime(list.get(i).getEndTime());
            entity.setAge(list.get(i).getAge());
            entity.setType1(list.get(i).get_type());
            entity.setRelation_record(list.get(i).getRelation_record());
            entity.setRecordId1(list.get(i).get_recordId());
            entity.setAbsTime1(list.get(i).get_absTime());
            entity.setScore(list.get(i).getScore());
            entity.setPlateNumber(list.get(i).getPlateNumber());
            entity.setSelect(false);
            dataBean.add(entity);
        }
        instert(dataBean);
    }

}
