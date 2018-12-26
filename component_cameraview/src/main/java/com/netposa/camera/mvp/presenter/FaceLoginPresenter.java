package com.netposa.camera.mvp.presenter;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.google.android.cameraview.R;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.netposa.camera.mvp.contract.FaceLoginContract;


@ActivityScope
public class FaceLoginPresenter extends BasePresenter<FaceLoginContract.Model, FaceLoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public FaceLoginPresenter(FaceLoginContract.Model model, FaceLoginContract.View rootView) {
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

    public void initLatestAlbum(Context context, ImageView albumBtn) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                ContactsContract.Contacts.Photo._ID + " desc");
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                String mediaPath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.MediaColumns.DATA));
                mImageLoader.loadImage(context, ImageConfigImpl
                        .builder()
                        .cacheStrategy(0)
                        .placeholder(R.drawable.ic_image_default)
                        .errorPic(R.drawable.ic_image_load_failed)
                        .url(mediaPath)
                        .imageView(albumBtn)
                        .build());
            }
        }
        cursor.close();
    }
}
