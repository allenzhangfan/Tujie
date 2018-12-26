package com.netposa.component.clcx.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.log.Log;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.utils.TimeUtils;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.model.entity.QueryCarSearchResponseEntity;
import com.netposa.component.clcx.mvp.model.entity.QueryResultEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import static com.netposa.common.utils.TimeUtils.FORMAT_ONE;


public class QueryResultAdapter extends BaseQuickAdapter<QueryCarSearchResponseEntity, BaseViewHolder> {
    private final Context mContext;
    private final ImageLoader mImageLoader;

    @Inject
    public QueryResultAdapter(Context context, @Nullable List<QueryCarSearchResponseEntity> list) {
        super(R.layout.item_query_result, list);
        mContext = context;
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader();
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryCarSearchResponseEntity item) {
        helper.setText(R.id.tv_car_num, item.getPlateNumber());
        helper.setText(R.id.tv_sxt, item.getDeviceName());
        helper.setText(R.id.tv_date, TimeUtils.millis2String(item.getAbsTime(), FORMAT_ONE));
        ImageView carView = helper.getView(R.id.iv_car);
        mImageLoader.loadImage(mContext, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(item.getSceneImg()))
                .imageView(carView)
                .build());
    }
}
