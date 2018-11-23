package com.netposa.component.jq.mvp.presenter;

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
import javax.inject.Inject;
import com.netposa.common.log.Log;
import com.netposa.component.jq.mvp.contract.JqSearchContract;
import com.netposa.component.room.dao.DbHelper;
import com.netposa.component.room.entity.JqSearchHistoryEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import java.util.List;

@ActivityScope
public class JqSearchPresenter extends BasePresenter<JqSearchContract.Model, JqSearchContract.View> {
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
    List<JqSearchHistoryEntity> mBeanList;
    private final DbHelper mDbHelper;

    @Inject
    public JqSearchPresenter(JqSearchContract.Model model, JqSearchContract.View rootView) {
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
                mDbHelper.deleteSearchHistory(mBeanList.get(i))
                        .subscribeOn(Schedulers.io())
                        .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                        .subscribe();
                mBeanList.remove(i);
            }
        }
        mRootView.refreshData();
    }

    @SuppressLint("CheckResult")
    public List<JqSearchHistoryEntity> getAll() {
        mDbHelper.getAllSearchHistories()
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
                .subscribe(jqSearchHistoryEntities -> {
                    Log.e(TAG, "getAll :" + jqSearchHistoryEntities.size());
                    mBeanList.addAll(jqSearchHistoryEntities);
                    mRootView.loadDataForFirstTime();
                });
        return mBeanList;
    }

    public void deleteAll() {
        mDbHelper.deleteAllSearchHistories()
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }
}
