package com.netposa.component.ytst.mvp.presenter;

import android.app.Application;

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
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.entity.YtstCarAndPeopleEntity;
import com.netposa.component.ytst.mvp.contract.SearchResultContract;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchRequestEntity;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


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
    public void  comparatorDate(List<ImgSearchResponseEntity.DataBean> mBeanList,boolean flag){
        Collections.sort(mBeanList, new Comparator<ImgSearchResponseEntity.DataBean>() {
            /**
             *
             * @param parm1
             * @param parm2
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            public int compare(ImgSearchResponseEntity.DataBean parm1, ImgSearchResponseEntity.DataBean parm2) {
                Date date1 =  TimeUtils.millis2Date(parm1.get_absTime());
                Date date2 = TimeUtils.millis2Date(parm2.get_absTime());
                // 对日期字段进行升序before，如果欲降序可采用after方法
                if (flag){ // 降序
                    if (date1.after(date2)) {
                        return 1;
                    }
                    return -1;
                }else {// 升序
                    if (date1.before(date2)) {
                        return 1;
                    }
                    return -1;
                }
            }
        });
    }

    public void  comparatorScore(List<ImgSearchResponseEntity.DataBean> mBeanList,boolean flag){
        Collections.sort(mBeanList, new Comparator<ImgSearchResponseEntity.DataBean>() {
            /**
             *
             * @param parm1
             * @param parm2
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            public int compare(ImgSearchResponseEntity.DataBean parm1, ImgSearchResponseEntity.DataBean parm2) {
                if (flag){//倒序
                    return parm2.getScore()-parm1.getScore();
                }else {// 正序排列
                    return parm1.getScore()-parm2.getScore();
                }
            }
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

    /**
     * 先删除数据 再插入数据
     * @param entities
     */
    public void instert(List<YtstCarAndPeopleEntity> entities) {
        mDbHelper.deleteAllYtstData().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(result -> {
                    instertData(entities);
                }, throwable -> {
                    Log.e("instert Fail");
                });
    }

}
