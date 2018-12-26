package com.netposa.component.clcx.mvp.presenter;

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

import com.netposa.common.constant.UrlConstant;
import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.TujieImageUtils;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.contract.QueryResultContract;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchEntity;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.util.ArrayList;
import java.util.List;


@ActivityScope
public class QueryResultPresenter extends BasePresenter<QueryResultContract.Model, QueryResultContract.View> {
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
    public QueryResultPresenter(QueryResultContract.Model model, QueryResultContract.View rootView) {
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

    public void getResultList(QueryCarSearchEntity entity) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }

        mModel.getDevicelist(entity)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<List<QueryCarSearchResponseEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(List<QueryCarSearchResponseEntity> responseEntity) {
                        Log.d("responseEntity", responseEntity.toString());
                        if (responseEntity.size()>0){
                            List<QueryCarSearchResponseEntity> list=new ArrayList<>();
                            for (int i=0;i<responseEntity.size();i++){
                                QueryCarSearchResponseEntity  queryCarSearchResponseEntity=new QueryCarSearchResponseEntity();
                                String playUrl=responseEntity.get(i).getSceneImg();
                                responseEntity.get(i).setSceneImg(TujieImageUtils.getDisplayUrl(playUrl,
                                        responseEntity.get(i).getLocation()));
                                // 切换url成剪切图
                                queryCarSearchResponseEntity.setRecordId(responseEntity.get(i).getRecordId());
                                queryCarSearchResponseEntity.setDeviceId(responseEntity.get(i).getDeviceId());
                                queryCarSearchResponseEntity.setDeviceName(responseEntity.get(i).getDeviceName());
                                queryCarSearchResponseEntity.setNodeType(responseEntity.get(i).getNodeType());
                                queryCarSearchResponseEntity.setPlateNumber(responseEntity.get(i).getPlateNumber());
                                queryCarSearchResponseEntity.setPlateColor(responseEntity.get(i).getPlateColor());
                                queryCarSearchResponseEntity.setSceneImg(playUrl);
                                queryCarSearchResponseEntity.setAbsTime(responseEntity.get(i).getAbsTime());
                                queryCarSearchResponseEntity.setLocation(responseEntity.get(i).getLocation());
                                queryCarSearchResponseEntity.setLongitude(responseEntity.get(i).getLongitude());
                                queryCarSearchResponseEntity.setLatitude(responseEntity.get(i).getLatitude());
                                queryCarSearchResponseEntity.setSource(responseEntity.get(i).getSource());
                                list.add(queryCarSearchResponseEntity);
                            }
                        }
                        mRootView.getListSuccese(responseEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("Throwable", t.toString());
                        mRootView.getListFailed();
                    }
                });
    }
}
