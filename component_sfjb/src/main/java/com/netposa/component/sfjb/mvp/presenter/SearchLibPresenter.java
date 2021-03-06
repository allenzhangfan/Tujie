package com.netposa.component.sfjb.mvp.presenter;

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
import com.netposa.component.room.DbHelper;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.contract.SearchLibContract;
import com.netposa.component.room.entity.SfjbSearchHistoryEntity;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibRequestEntity;
import com.netposa.component.sfjb.mvp.model.entity.SearchFaceLibResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@ActivityScope
public class SearchLibPresenter extends BasePresenter<SearchLibContract.Model, SearchLibContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<SfjbSearchHistoryEntity> mBeanList;
    @Inject
    Context mContext;
    @Inject
    SearchFaceLibRequestEntity mEntity;

    private final DbHelper mDbHelper;

    @Inject
    public SearchLibPresenter(SearchLibContract.Model model, SearchLibContract.View rootView) {
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
     * 单条历史记录删除
     *
     * @param itemPosition
     */
    public void deleteSigleHistory(int itemPosition) {
        for (int i = 0; i < mBeanList.size(); i++) {
            if (i == itemPosition) {
                mDbHelper.deleteSfjbSearchHistory(mBeanList.get(i))
                        .subscribeOn(Schedulers.io())
                        .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                        .subscribe();
                mBeanList.remove(i);
            }
        }
        mRootView.refreshData();
    }

    @SuppressLint("CheckResult")
    public List<SfjbSearchHistoryEntity> getAll() {
        mDbHelper.getAllSfjbSearchHistories()
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
                .subscribe(new ErrorHandleSubscriber<List<SfjbSearchHistoryEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<SfjbSearchHistoryEntity> response) {
                        Log.e(TAG, "getAll :" + response.toString());
                        mBeanList.addAll(removeDuplicateHistoryEntity(response));
                        mRootView.loadDataForFirstTimeSuccess();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(TAG, "loadDataForFirstTimeFail");
                        mRootView.loadDataForFirstTimeFail();
                    }

                });
        return mBeanList;
    }

    private static ArrayList<SfjbSearchHistoryEntity> removeDuplicateHistoryEntity(List<SfjbSearchHistoryEntity> SfjbSearchHistoryList) {
        Set<SfjbSearchHistoryEntity> set = new TreeSet<>((o1, o2) -> {
            //字符串,根据搜索结果来去重
            return o1.getName().compareTo(o2.getName());
        });
        set.addAll(SfjbSearchHistoryList);
        return new ArrayList<>(set);
    }

    public void deleteAll() {
        mDbHelper.deleteSfjbAllSearchHistories()
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }

    /**
     * 搜索结果获取的数据
     */
    public void getMatchData(String result, String id) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mEntity.setOrgId(id);
        mEntity.setName(result);
        mModel.getSearch(mEntity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<SearchFaceLibResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(SearchFaceLibResponseEntity responseEntity) {
                        Log.i("getMatchData:", responseEntity.toString());
                        mRootView.getListSuccess(responseEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("getMatchData:", t.toString());
                        mRootView.getListFailed();
                    }
                });
    }
}
