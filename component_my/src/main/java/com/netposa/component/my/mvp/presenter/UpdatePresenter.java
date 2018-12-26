package com.netposa.component.my.mvp.presenter;

import android.content.Context;
import com.jess.arms.mvp.BasePresenter;
import com.netposa.common.log.Log;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.component.my.R;
import com.netposa.component.my.mvp.contract.UpdateContract;
import com.netposa.component.my.mvp.model.entity.UpdateInfoEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

/**
 * Author：yeguoqiang
 * Created time：2018/12/15 10:21
 */
public class UpdatePresenter extends BasePresenter<UpdateContract.Model, UpdateContract.View> {

    public UpdatePresenter(UpdateContract.Model model, UpdateContract.View rootView) {
        super(model, rootView);
    }

    public void getUpdateJson(Context context, RxErrorHandler errorHandler) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(context.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getUpdateJson()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> mRootView.showLoading(""))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (mRootView != null) mRootView.hideLoading();
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<UpdateInfoEntity>(errorHandler) {
                    @Override
                    public void onNext(UpdateInfoEntity responseEntity) {
                        Log.d("getUpdateJson：", responseEntity.toString());
                        mRootView.getUpdateJsonSuc(responseEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("getUpdateJson: ", t.toString());
                        mRootView.getUpdateJsonFail();
                    }
                });
    }
}
