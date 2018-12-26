package com.netposa.component.spjk.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

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
import com.netposa.component.room.entity.SpjkSearchHistoryEntity;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.contract.SpjkSearchContract;
import com.netposa.component.spjk.mvp.model.entity.SpjkItemEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchRequestEntity;
import com.netposa.component.spjk.mvp.model.entity.SpjkSearchResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.netposa.common.constant.GlobalConstants.PAGE_SIZE_DEFAULT;

@ActivityScope
public class SpjkSearchPresenter extends BasePresenter<SpjkSearchContract.Model, SpjkSearchContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<SpjkSearchHistoryEntity> mBeanList;
    @Inject
    List<SpjkItemEntity> mSearchResultBeanList;
    @Inject
    SpjkSearchRequestEntity mRequestEntity;
    @Inject
    Context mContext;
    private final DbHelper mDbHelper;
    private SpjkSearchRequestEntity.PageInfoBean mentity;

    @Inject
    public SpjkSearchPresenter(SpjkSearchContract.Model model, SpjkSearchContract.View rootView) {
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
                mDbHelper.deleteSpjkSearchHistory(mBeanList.get(i))
                        .subscribeOn(Schedulers.io())
                        .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                        .subscribe();
                mBeanList.remove(i);
            }
        }
        mRootView.refreshData();
    }

    @SuppressLint("CheckResult")
    public List<SpjkSearchHistoryEntity> getAll() {
        mDbHelper.getAllSpjkSearchHistories()
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
                .subscribe(new ErrorHandleSubscriber<List<SpjkSearchHistoryEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<SpjkSearchHistoryEntity> response) {
                        Log.e(TAG, "getAll :" + response.toString());
                        mBeanList.addAll(removeDuplicateSearchHistory(response));
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

    private static ArrayList<SpjkSearchHistoryEntity> removeDuplicateSearchHistory(List<SpjkSearchHistoryEntity> SearchHistoryList) {
        Set<SpjkSearchHistoryEntity> set = new TreeSet<>((o1, o2) -> {
            //字符串,根据搜索结果来去重
            return o1.getName().compareTo(o2.getName());
        });
        set.addAll(SearchHistoryList);
        return new ArrayList<>(set);
    }

    public void deleteAll() {
        mDbHelper.deleteSpjkAllSearchHistories()
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }

    /**
     * 获取ID；
     */
    public void getOrganizeId() {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getOrganizeId(new SpjkListDeviceRequestEntity())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<SpjkListDeviceResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(SpjkListDeviceResponseEntity responseEntity) {
                        Log.d("getOrganizeId :", responseEntity.toString());
                        String id = responseEntity.getDeviceTreeList().get(0).getId();
                        if (!TextUtils.isEmpty(id)) {
                            mRootView.getOrganizeId(id);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        mRootView.getListFailed();
                    }
                });
    }

    /**
     * 搜索结果获取的数据
     */
    public void getMatchData(String result, int page, String id) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mRequestEntity.setKey(result);
        mRequestEntity.setId(id);
        mentity = new SpjkSearchRequestEntity.PageInfoBean();
        mentity.setCurrentPage(page);
        mentity.setPageSize(PAGE_SIZE_DEFAULT);
        mRequestEntity.setPageInfo(mentity);
        mModel.getDevice(mRequestEntity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<SpjkSearchResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(SpjkSearchResponseEntity responseEntity) {
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
