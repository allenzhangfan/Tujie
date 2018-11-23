package com.netposa.component.imageupload;

import android.graphics.Bitmap;

import com.netposa.common.constant.CommonConstant;
import com.netposa.common.log.Log;
import com.netposa.common.utils.Configuration;
import com.netposa.common.utils.EncryptUtils;
import com.netposa.common.utils.ImageUtils;
import com.netposa.common.utils.NetworkUtils;
import com.netposa.common.utils.ToastUtils;
import com.netposa.common.utils.Utils;
import com.netposa.component.pic.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * Author：yeguoqiang
 * Created time：2018/9/27 13:41
 */
public class ImageUploadUtils {

    private static final String TAG = ImageUploadUtils.class.getSimpleName();

    private ImageUploadUtils mInstance = new ImageUploadUtils();

    private ArrayList<MultipartBody.Part> mFiles;

    public ImageUploadUtils getInstance() {
        return mInstance;
    }

    public void upoadImages(List<String> imagePathList) {
        mFiles = new ArrayList<>();
        Observable.fromIterable(imagePathList)
                .map(new Function<String, String>() {

                    @Override
                    public String apply(String srcImagePath) throws Exception {
                        String destImagePath = compressImage(srcImagePath);
                        return destImagePath;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String destImagePath) {
                        File imageFile = new File(destImagePath);
                        String formField = "image_" + EncryptUtils.generateRandomUUID();
                        MultipartBody.Part part = RequestUtils.prepareFilePart(formField, imageFile.getAbsolutePath());
                        mFiles.add(part);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        upoadImages();
                    }
                });
    }

    /**
     * 压缩图片资源,返回压缩后的图片路径
     * @param imagePath
     * @return
     */
    private String compressImage(String imagePath) {
        String newJpgPath = Configuration.getPictureDirectoryPath() + "IMG_" + System.nanoTime() + ".jpeg";
        ImageUtils.save(imagePath, newJpgPath, CommonConstant.MAX_IMAGE_BOUNDS,
                Bitmap.CompressFormat.JPEG, CommonConstant.MAX_IMAGE_SIZE);
        Log.d(TAG, "oldImagePath:" + imagePath + ",newImagePath:" + newJpgPath);
        return newJpgPath;
    }

    /**
     * 开始上传
     */
    private void upoadImages() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(Utils.getContext().getResources().getString(R.string.network_disconnect));
            return;
        }
        //TODO
    }
}
