package com.netposa.component.sfjb.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.utils.StringUtils;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.model.entity.FaceCompareResponseEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;


public class SfjbCompareResultAdapter extends BaseQuickAdapter<FaceCompareResponseEntity.ListBean, BaseViewHolder> {

    private final Context mContext;
    private final ImageLoader mImageLoader;

    @Inject
    public SfjbCompareResultAdapter(Context context,@Nullable List<FaceCompareResponseEntity.ListBean> list) {
        super(R.layout.item_result_list, list);
        mContext=context;
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader();
    }

    @Override
    protected void convert(BaseViewHolder helper, FaceCompareResponseEntity.ListBean item) {
        double similarity=item.getPhotoInfoExts().get(0).getScore();
        helper.setText(R.id.tv_similarity_num, StringUtils.dealSimilarityBackStr(similarity)+"%");
        int postion=helper.getAdapterPosition();
        //库
        String kuName=item.getLibName();
        if (TextUtils.isEmpty(kuName)){
            helper.setText(R.id.tv_ku, item.getLibName());
        }
        helper.setText(R.id.tv_name, item.getName());
        RoundImageView picView = helper.getView(R.id.iv_avatar);
        if (postion==0){//第一个位置
            helper.setBackgroundRes(
                    R.id.tv_similarity_num,
                    R.drawable.tv_percentage_blue_bg);
            helper.setBackgroundRes(R.id.iv_avatar,R.drawable.image_bound_blue_shape);
        }else {
            helper.setBackgroundRes(
                    R.id.tv_similarity_num,
                    R.drawable.tv_percentage_gray_bg);
            helper.setBackgroundRes(R.id.iv_avatar,R.drawable.image_bound_transparent_shape);
        }
        String imgUrl=item.getPhotoInfoExts().get(0).getUrl();
        imgUrl= UrlConstant.parseImageUrl(imgUrl);
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

