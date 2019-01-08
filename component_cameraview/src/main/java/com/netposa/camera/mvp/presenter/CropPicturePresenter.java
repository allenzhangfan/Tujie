package com.netposa.camera.mvp.presenter;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;

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
import okhttp3.MultipartBody;

import javax.inject.Inject;

import com.netposa.camera.mvp.contract.CropPictureContract;
import com.netposa.camera.mvp.model.entity.UploadResponseEntity;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.core.RouterHub;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.entity.login.LoginResponseEntity;
import com.netposa.common.log.Log;
import com.netposa.common.service.mqtt.PushService;
import com.netposa.common.utils.ImageUtils;
import com.netposa.common.utils.RequestUtils;
import com.netposa.common.utils.SPUtils;
import com.netposa.component.room.DbHelper;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import static com.netposa.common.constant.GlobalConstants.MAX_UPLOAD_IMAGE_SIZE;
import static com.netposa.common.constant.GlobalConstants.PART_NAME_IMAGE;

@ActivityScope
public class CropPicturePresenter extends BasePresenter<CropPictureContract.Model, CropPictureContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public CropPicturePresenter(CropPictureContract.Model model, CropPictureContract.View rootView) {
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

    public void faceLoginOrCropImage(Bitmap cropBitMap, String typeStr) {
        if (ImageUtils.getBitmapSize(cropBitMap) > MAX_UPLOAD_IMAGE_SIZE) {//压缩完了保存成jpg的图片
            cropBitMap = ImageUtils.compressScale(cropBitMap);
        }
        MultipartBody.Part part = RequestUtils.prepareFilePart(PART_NAME_IMAGE, ImageUtils.saveBitmap(cropBitMap));
        if (!TextUtils.isEmpty(typeStr)) {
            faceLogin(part, typeStr);
        } else {// 身份鉴别的上传图片
            uploadImage(part);
        }
    }

    /**
     * 图片上传
     *
     * @param part
     */
    public void uploadImage(MultipartBody.Part part) {
        mModel.uploadImage(part)
                .subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<HttpResponseEntity<UploadResponseEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(HttpResponseEntity<UploadResponseEntity> entity) {
                        Log.i(TAG, "UploadResponseEntity :" + entity);
                        mRootView.uploadImageSuccess(entity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.i(TAG, "UploadResponseEntity Failed");
                        mRootView.uploadImageFail();
                    }
                });
    }

    /**
     * 人脸登录接口
     *
     * @param part
     */
    public void faceLogin(MultipartBody.Part part, String name) {
        mModel.faceLogin(part, name)
                .subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading("");//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<LoginResponseEntity>(mErrorHandler) {
                    @Override
                    public void onNext(LoginResponseEntity entity) {
                        Log.i(TAG, "faceLogin :" + entity);
                        SPUtils.getInstance().put(GlobalConstants.CONFIG_LAST_USER_LOGIN_NAME, entity.getUser().getLoginName());
                        // 登录的时候删除上次缓存的推送消息
                        DbHelper
                                .getInstance()
                                .deleteAllAlarmMessages()
                                .subscribeOn(Schedulers.io())
                                .subscribe();
                        PushService pushService = (PushService) ARouter.getInstance().build(RouterHub.MQTT_PUSH_SERVICE).navigation();
                        pushService.activateMQTT();
                        mRootView.goToHomeActivity();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(TAG, "faceLogin Failed");
                    }
                });
    }
}
