package com.netposa.component.ytst.mvp.presenter;

import android.app.Application;
import android.graphics.Bitmap;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MultipartBody;

import javax.inject.Inject;

import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.log.Log;
import com.netposa.common.utils.ImageUtils;
import com.netposa.common.utils.RequestUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.component.ytst.mvp.contract.ShowTakePictureContract;
import com.netposa.component.ytst.mvp.model.entity.UploadPicResponseEntity;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;

import static com.netposa.common.constant.GlobalConstants.MAX_UPLOAD_IMAGE_SIZE;
import static com.netposa.common.constant.GlobalConstants.PART_NAME_IMAGE;


@ActivityScope
public class ShowTakePicturePresenter extends BasePresenter<ShowTakePictureContract.Model, ShowTakePictureContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public ShowTakePicturePresenter(ShowTakePictureContract.Model model, ShowTakePictureContract.View rootView) {
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

    public void compressImage(Bitmap source) {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        String path;
                        if (ImageUtils.getBitmapSize(source) > MAX_UPLOAD_IMAGE_SIZE) {//压缩完了保存成jpg的图片
                            Bitmap dealBitMap = ImageUtils.compressScale(source);
                            path = ImageUtils.saveBitmap(dealBitMap);
                        } else {
                            path = ImageUtils.saveBitmap(source);
                        }
                        emitter.onNext(path);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String destPath) {
                        Log.i(TAG, "压缩成功:" + destPath);
                        MultipartBody.Part part = RequestUtils.prepareFilePart(PART_NAME_IMAGE, destPath);
                        uploadImage(part);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showShort("压缩文件失败");
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete :");
                    }
                });
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
//                    mRootView.showLoading("");//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(AndroidLifecycle.createLifecycleProvider((LifecycleOwner) mRootView).bindToLifecycle())
                .subscribe(new ErrorHandleSubscriber<HttpResponseEntity<UploadPicResponseEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(HttpResponseEntity<UploadPicResponseEntity> entity) {
                        Log.i(TAG, "UploadResponseEntity :" + entity);
                        mRootView.uploadImageSuccess(entity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        Log.e(TAG, "UploadResponseEntity Failed");
                        mRootView.uploadImageFail();
                    }
                });
    }
}
