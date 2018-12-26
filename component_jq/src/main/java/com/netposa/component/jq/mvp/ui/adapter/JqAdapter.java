package com.netposa.component.jq.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.constant.UrlConstant;
import com.netposa.commonres.widget.CircleProgressView;
import com.netposa.component.jq.R;
import com.netposa.common.entity.push.JqItemEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import static com.netposa.common.constant.GlobalConstants.TYPE_INVALID;
import static com.netposa.common.constant.GlobalConstants.TYPE_FACE_DEPLOY;
import static com.netposa.common.constant.GlobalConstants.TYPE_SUSPENDING;
import static com.netposa.common.constant.GlobalConstants.TYPE_VALID;

/**
 * Author：yeguoqiang
 * Created time：2018/10/29 14:31
 */
public class JqAdapter extends BaseQuickAdapter<JqItemEntity, BaseViewHolder> {

    private final Context mContext;
    private final ImageLoader mImageLoader;

    @Inject
    public JqAdapter(Context context, @Nullable List<JqItemEntity> data) {
        super(R.layout.item_jq, data);
        mContext = context;
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    protected void convert(BaseViewHolder helper, JqItemEntity item) {
        CircleProgressView cpvSimilarity = helper.getView(R.id.cpv_similarity);
        TextView tvCarNumber = helper.getView(R.id.tv_car_number);
        int itemHandleTAG = item.getItemHandleType();
        if (itemHandleTAG == TYPE_VALID) {
            helper.setText(R.id.tv_jq_tag, R.string.valid);
            helper.setTextColor(R.id.tv_jq_tag, mContext.getResources().getColor(R.color.color_2CCE9A));
        } else if (itemHandleTAG == TYPE_INVALID) {
            helper.setText(R.id.tv_jq_tag, R.string.invalid);
            helper.setTextColor(R.id.tv_jq_tag, mContext.getResources().getColor(R.color.color_C0C5D1));
        } else if (itemHandleTAG == TYPE_SUSPENDING) {
            helper.setText(R.id.tv_jq_tag, R.string.suspending);
            helper.setTextColor(R.id.tv_jq_tag, mContext.getResources().getColor(R.color.color_FF503A));
        }
        helper.setText(R.id.tv_camera_location, item.getCameraLocation());
        helper.setText(R.id.tv_capture_info, item.getCaptureLibName());
        helper.setText(R.id.tv_alarm_time, item.getAlarmTime());
        ImageView ivCapture = helper.getView(R.id.iv_capture);
        ImageView ivDeploy = helper.getView(R.id.iv_deploy);
        mImageLoader.loadImage(mContext, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(item.getCaptureImgUrl()))
                .imageView(ivCapture)
                .build());
        mImageLoader.loadImage(mContext, ImageConfigImpl
                .builder()
                .cacheStrategy(0)
                .placeholder(R.drawable.ic_image_default)
                .errorPic(R.drawable.ic_image_load_failed)
                .url(UrlConstant.parseImageUrl(item.getDeployImgUrl()))
                .imageView(ivDeploy)
                .build());

        if (item.getItemType() == TYPE_FACE_DEPLOY) {//人
            cpvSimilarity.setVisibility(View.VISIBLE);
            tvCarNumber.setVisibility(View.GONE);
            helper.setImageDrawable(R.id.iv_jq, ContextCompat.getDrawable(mContext, R.drawable.ic_people));
            helper.setText(R.id.tv_detail, R.string.similarity);
            cpvSimilarity.setScore((float) item.getSimilarity(), false, "%");
        } else {//车
            cpvSimilarity.setVisibility(View.GONE);
            tvCarNumber.setVisibility(View.VISIBLE);
            tvCarNumber.setText(item.getCarNumber());
            helper.setImageDrawable(R.id.iv_jq, ContextCompat.getDrawable(mContext, R.drawable.ic_car));
            helper.setText(R.id.tv_detail, R.string.deploy_car_number);
        }
    }
}
