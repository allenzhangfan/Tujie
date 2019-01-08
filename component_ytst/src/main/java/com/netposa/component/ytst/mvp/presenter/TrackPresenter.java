package com.netposa.component.ytst.mvp.presenter;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.netposa.common.core.RouterHub;
import com.netposa.common.entity.TrackEnity;
import com.netposa.common.log.Log;
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.entity.YtstCarAndPeopleEntity;
import com.netposa.component.ytst.mvp.contract.TrackContract;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.netposa.common.constant.GlobalConstants.KEY_TRACK_ENTITY;
import static com.netposa.component.ytst.app.YtstConstants.TYPE_FACE;


@ActivityScope
public class TrackPresenter extends BasePresenter<TrackContract.Model, TrackContract.View> {
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
    private final DbHelper mDbHelper;

    @Inject
    public TrackPresenter(TrackContract.Model model, TrackContract.View rootView) {
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

    public void getAllData() {
        mDbHelper.getAllYtstData()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<List<YtstCarAndPeopleEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<YtstCarAndPeopleEntity> entities) {
                        Log.i(TAG, "Neighbouring ckeckDevice :" + entities);
                        mRootView.showCarAndPepoleData(entities);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.i(TAG, "Neighbouring ckeckDeviceFail");
                        mRootView.showCarAndPepoleDataFailed();
                    }
                });
    }

    public void comparatorDate(List<YtstCarAndPeopleEntity> mBeanList, boolean flag) {
        Collections.sort(mBeanList, new Comparator<YtstCarAndPeopleEntity>() {
            /**
             *
             * @param parm1
             * @param parm2
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            public int compare(YtstCarAndPeopleEntity parm1, YtstCarAndPeopleEntity parm2) {
                Date date1 = TimeUtils.millis2Date(parm1.getAbsTime());
                Date date2 = TimeUtils.millis2Date(parm2.getAbsTime());
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

    public void comparatorScore(List<YtstCarAndPeopleEntity> mBeanList, boolean flag) {
        Collections.sort(mBeanList, new Comparator<YtstCarAndPeopleEntity>() {
            /**
             *
             * @param parm1
             * @param parm2
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            public int compare(YtstCarAndPeopleEntity parm1, YtstCarAndPeopleEntity parm2) {
                if (flag) {//倒序
                    return parm2.getScore() - parm1.getScore();
                } else {// 正序排列
                    return parm1.getScore() - parm2.getScore();
                }
            }
        });
    }

    public void  intentPathActivity(ArrayList<String> mSelectList,List<YtstCarAndPeopleEntity> mBeanList,String mPicPath){
        TrackEnity trackEnity=new TrackEnity();
        trackEnity.setCropPic(mPicPath);
        trackEnity.setPoint(mSelectList);
        trackEnity.setSceneImg(mBeanList.get(0).getSceneImg());
        trackEnity.setStartTime(mBeanList.get(0).getAbsTime());
        trackEnity.setLocation(mBeanList.get(0).getLocation());
        String type= mBeanList.get(0).getType1();
        if (TYPE_FACE.equals(type)){
            trackEnity.setTitleName(mBeanList.get(0).getDeviceName());
        }else{
            trackEnity.setTitleName(mBeanList.get(0).getPlateNumber());
        }
        ARouter.getInstance().build(RouterHub.CLCX_VIEW_CAR_PATH_ACTIVITY)
                .withParcelable(KEY_TRACK_ENTITY,trackEnity)
                .navigation(mContext);
    }
}
