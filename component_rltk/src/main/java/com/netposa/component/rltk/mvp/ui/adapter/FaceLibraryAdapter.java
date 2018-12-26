package com.netposa.component.rltk.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.rltk.R;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryResponseEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/11/29 20:24
 */
public class FaceLibraryAdapter extends BaseQuickAdapter<FaceLibraryResponseEntity.ListEntity, BaseViewHolder> {

    private ImageLoader mImageLoader;
    private Context mContext;

    @Inject
    public FaceLibraryAdapter(Context context, @Nullable List<FaceLibraryResponseEntity.ListEntity> data) {
        super(R.layout.item_face_library, data);
        this.mContext = context;
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    protected void convert(BaseViewHolder helper, FaceLibraryResponseEntity.ListEntity item) {
        helper.setText(R.id.tv_camera_address, item.getDeviceName());
        helper.setText(R.id.tv_date, TimeUtils.millis2String(item.getSaveTime()));
        ImageView ivPerson = helper.getView(R.id.iv_person);
        mImageLoader.loadImage(mContext, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(item.getTraitImg()))
                .imageView(ivPerson)
                .build());
    }
}