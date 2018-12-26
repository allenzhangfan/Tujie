package com.netposa.component.ytst.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.utils.TimeUtils;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.ytst.R;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;

import java.util.ArrayList;
import javax.inject.Inject;
import androidx.annotation.Nullable;


public class SearchResultAdapter extends BaseQuickAdapter<ImgSearchResponseEntity.DataBean, BaseViewHolder> {

    private final Context mContext;
    private final ImageLoader mImageLoader;

    @Inject
    public SearchResultAdapter(Context context,@Nullable ArrayList<ImgSearchResponseEntity.DataBean> list) {
        super(R.layout.item_result_list, list);
        mContext=context;
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader();
    }

    @Override
    protected void convert(BaseViewHolder helper, ImgSearchResponseEntity.DataBean item) {
        helper.setText(R.id.tv_similarity_num, item.getScore()+"%");
        helper.setText(R.id.tv_ku, TimeUtils.millis2String(item.getAbsTime()));
        helper.setText(R.id.tv_name, item.getDeviceName());
        RoundImageView picView = helper.getView(R.id.iv_avatar);
        String imgUrl=item.getTraitImg();
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