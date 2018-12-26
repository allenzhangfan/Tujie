package com.netposa.component.ytst.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.common.utils.SizeUtils;
import com.netposa.commonres.widget.RoundImageView;
import com.netposa.component.ytst.R;
import com.netposa.component.ytst.mvp.model.entity.PonitEntity;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/12/7 17:35
 */
public class SelectTargetAdapter extends BaseQuickAdapter<PonitEntity, BaseViewHolder> {

    private ImageLoader mImageLoader;
    private Context mContext;
    private final int mSeletedPadding;
    private final int mDefaultPadding;

    @Inject
    public SelectTargetAdapter(Context context, @Nullable List<PonitEntity> data) {
        super(R.layout.item_select_target, data);
        this.mContext = context;
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(context).imageLoader();
        mSeletedPadding = SizeUtils.dp2px(2f);
        mDefaultPadding = SizeUtils.dp2px(0.5f);
    }

    @Override
    protected void convert(BaseViewHolder helper, PonitEntity item) {
        helper.setVisible(R.id.iv_chosed, item.isSelected());
        RoundImageView rIvTarget = helper.getView(R.id.riv_target);
        if (item.isSelected()) {
            rIvTarget.setPadding(
                    mSeletedPadding,
                    mSeletedPadding,
                    mSeletedPadding,
                    mSeletedPadding);
            helper.setBackgroundRes(R.id.riv_target, R.drawable.image_bound_blue_shape);
        } else {
            rIvTarget.setPadding(
                    mDefaultPadding,
                    mDefaultPadding,
                    mDefaultPadding,
                    mDefaultPadding);
            helper.setBackgroundRes(R.id.riv_target, R.drawable.image_view_bound_shape);
        }
        String path=UrlConstant.parseImageUrl(item.getImgPath());
            if (!TextUtils.isEmpty(path)) {
                mImageLoader.loadImage(mContext,
                        ImageConfigImpl
                                .builder()
                                .placeholder(R.drawable.ic_image_default)
                                .errorPic(R.drawable.ic_image_load_failed)
                                .url(path)
                                .imageView(rIvTarget)
                                .build());
            } else {
                rIvTarget.setImageResource(R.drawable.ic_image_default);
            }
    }
}