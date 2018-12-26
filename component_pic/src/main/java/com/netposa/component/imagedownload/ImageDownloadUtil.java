package com.netposa.component.imagedownload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.Preconditions;
import com.netposa.common.utils.Configuration;
import com.netposa.common.utils.FileUtils;
import com.netposa.common.utils.ImageUtils;
import com.netposa.component.pic.R;

import java.io.File;

import androidx.annotation.Nullable;

import static com.netposa.common.constant.GlobalConstants.DEFAULT_IMAGE_SUFFIX;

/**
 * Author：yeguoqiang
 * Created time：2018/12/3 09:46
 */
public class ImageDownloadUtil {

    private Context mContext;

    public ImageDownloadUtil(Context context) {
        Preconditions.checkNotNull(context, "Context is required");
        mContext = context;
    }

    public void downloadImage(String downloadUrl, ImageLoadListener listener) {
        Preconditions.checkNotNull(downloadUrl, "downloadUrl is required");
        Preconditions.checkNotNull(listener, "ImageLoadListener is required");
        String downloadPath = Configuration.getDownloadDirectoryPath();
        listener.onLoadingStarted(downloadUrl);
        GlideArms.with(mContext) //配置上下文
                .load(downloadUrl)//设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .placeholder(R.drawable.ic_image_default) //设置占位图片
                .error(R.drawable.ic_image_load_failed)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        File imageFile = FileUtils.createFile(downloadPath, "", DEFAULT_IMAGE_SUFFIX);
                        boolean saveResult = ImageUtils.save(ImageUtils.drawable2Bitmap(resource), imageFile, Bitmap.CompressFormat.JPEG);
                        listener.onLoadingComplete(downloadUrl, downloadPath, saveResult, resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        listener.onLoadingFailed(downloadUrl, new LoadErrorEntity(0, mContext.getString(R.string.download_image_failed)));
                    }
                });
    }

    public interface ImageLoadListener {

        void onLoadingStarted(String downloadUrl);

        void onLoadingFailed(String downloadUrl, LoadErrorEntity errorEntity);

        void onLoadingComplete(String downloadUrl, String downloadPath, boolean saveResult, Drawable drawable);
    }
}
