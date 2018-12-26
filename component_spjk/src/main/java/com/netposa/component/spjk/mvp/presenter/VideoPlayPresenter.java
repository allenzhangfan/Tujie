package com.netposa.component.spjk.mvp.presenter;

import android.app.Application;
import android.content.Context;
import android.text.format.DateFormat;

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

import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.utils.DeviceUtil;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.contract.VideoPlayContract;
import com.netposa.component.spjk.mvp.model.entity.DeviceInfoResponseEntity;
import com.netposa.component.spjk.mvp.model.entity.PtzDirectionRequestEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;

import static com.netposa.component.spjk.app.SpjkConstants.DATA_STRING;
import static com.netposa.component.spjk.app.SpjkConstants.JPG;
import static com.netposa.component.spjk.app.SpjkConstants.MIN_CLICK_DELAY_TIME;


@ActivityScope
public class VideoPlayPresenter extends BasePresenter<VideoPlayContract.Model, VideoPlayContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    ArrayBlockingQueue<String> mCaptureblockqueue;//截图任务队列
    @Inject
    Context mContext;
    private long mLastClickTime = 0;
    private final DbHelper mDbHelper;

    @Inject
    public VideoPlayPresenter(VideoPlayContract.Model model, VideoPlayContract.View rootView) {
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
     * 截图
     */
    public void onScreenCapture() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - mLastClickTime > MIN_CLICK_DELAY_TIME) {
            mLastClickTime = currentTime;
            Calendar calendar = Calendar.getInstance();
            String format = DATA_STRING;
            File dir = new File(GlobalConstants.PICTURE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String picname = DateFormat.format(format, calendar) + JPG;
            String filepath = dir.getAbsolutePath() + "/" + picname;
            mCaptureblockqueue.add(filepath);
            //通知图库更新
            DeviceUtil.galleryAddMedia(filepath);
            ToastUtils.showShort(mContext.getResources().getString(R.string.img_has_saved));
        }
    }

    /**
     * 获取视频详情信息
     *
     * @param cameraId
     */
    public void getDeviceInfo(String cameraId) {
        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.getDeviceInfo(cameraId)
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
                .subscribe(new ErrorHandleSubscriber<DeviceInfoResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(DeviceInfoResponseEntity response) {
                        Log.d(TAG, response.toString());
                        mRootView.showDeviceInfoSuccess(response);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.showDeviceInfoFailed();
                    }
                });
    }

    /**
     * 设置云台控制
     *
     * @param ptzDirection
     */
    public void setPtzDirection(PtzDirectionRequestEntity ptzDirection) {

        if (!NetworkUtils.isConnected()) {
            mRootView.showMessage(mContext.getString(R.string.network_disconnect));
            mRootView.hideLoading();
            return;
        }
        mModel.setPtzDirection(ptzDirection)
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
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object response) {
                        Log.e("setPtzDirection", response);
                        mRootView.setDirectionSuccess(response);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
    }


    /**
     * 根据id 删除存在数据库的一条数据 即取消关注
     *
     * @param cameraId
     */
    public void deleteDevice(String cameraId) {
        mDbHelper.deleteDevice(cameraId)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }

    /**
     * 插入一条关注数据
     *
     * @param deviceEntiry
     */
    public void insterDevice(SpjkCollectionDeviceEntity deviceEntiry) {
        mDbHelper.insterOneDevice(deviceEntiry)
                .subscribeOn(Schedulers.io())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe();
    }

    /**
     * 查看当前的设备是否被关注
     *
     * @param cameraId
     */
    public void checkDevice(String cameraId) {
        mDbHelper.ckeckDevice(cameraId)
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
                .subscribe(new ErrorHandleSubscriber<Integer>(mErrorHandler) {
                    @Override
                    public void onNext(Integer count) {
                        Log.i(TAG, "check collect device count:" + count);
                        mRootView.checkDeviceSuccess(count);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.i(TAG, "ckeck follow device count fail");
                        mRootView.checkDeviceFailed();
                    }
                });
    }

}
