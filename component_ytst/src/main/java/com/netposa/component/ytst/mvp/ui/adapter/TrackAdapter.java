package com.netposa.component.ytst.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.room.entity.YtstCarAndPeopleEntity;
import com.netposa.component.ytst.R;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class TrackAdapter extends BaseQuickAdapter<YtstCarAndPeopleEntity, BaseViewHolder> {
    private final Context mContext;
    private final ImageLoader mImageLoader;

    @Inject
    public TrackAdapter(Context context, @Nullable List<YtstCarAndPeopleEntity> list) {
        super(R.layout.item_result_list, list);
        mContext = context;
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader();
    }

    @Override
    protected void convert(BaseViewHolder helper, YtstCarAndPeopleEntity item) {
        helper.setText(R.id.tv_similarity_num, item.getScore() + "%");
        helper.setText(R.id.tv_ku, TimeUtils.millis2String(item.getAbsTime()));
        helper.setText(R.id.tv_name, item.getDeviceName());
        RoundImageView picView = helper.getView(R.id.iv_avatar);
        ImageView chooseImg = helper.getView(R.id.iv_chosed);
        chooseImg.setVisibility(View.VISIBLE);
        if (item.isSelect()) {
            helper.setBackgroundRes(R.id.tv_similarity_num, R.drawable.tv_percentage_blue_bg);
            helper.setBackgroundRes(R.id.iv_avatar, R.drawable.image_bound_blue_shape);
            helper.setImageDrawable(R.id.iv_chosed, ContextCompat.getDrawable(mContext, R.drawable.ic_choosed));
        } else {
            helper.setBackgroundRes(R.id.tv_similarity_num, R.drawable.tv_percentage_gray_bg);
            helper.setBackgroundRes(R.id.iv_avatar, R.drawable.image_bound_transparent_shape);
            helper.setImageDrawable(R.id.iv_chosed, ContextCompat.getDrawable(mContext, R.drawable.ic_img_unchoosed));
        }
        String imgUrl = item.getTraitImg();
        imgUrl = UrlConstant.parseImageUrl(imgUrl);
        if (!TextUtils.isEmpty(imgUrl)) {
            mImageLoader.loadImage(mContext,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.ic_image_default)
                            .errorPic(R.drawable.ic_image_load_failed)
                            .url(imgUrl)
                            .imageView(picView)
                            .build());
        } else {
            picView.setImageResource(R.drawable.ic_image_default);
        }
    }
}
