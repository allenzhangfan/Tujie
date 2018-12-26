package com.netposa.component.spjk.mvp.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.room.entity.SpjkCollectionDeviceEntity;
import com.netposa.component.spjk.R;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import static com.netposa.common.constant.GlobalConstants.CAMERA_QIANG_JI;
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIU_JI;

/**
 * Author：yeguoqiang
 * Created time：2018/11/8 13:37
 */
public class FollowDevicesAdapter extends BaseQuickAdapter<SpjkCollectionDeviceEntity, BaseViewHolder> {

    @Inject
    Context mContext;

    @Inject
    public FollowDevicesAdapter(@Nullable List<SpjkCollectionDeviceEntity> data) {
        super(R.layout.item_follow_devices, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpjkCollectionDeviceEntity item) {
        helper.setText(R.id.tv_camera_address, item.getCamername());
        if (item.getCamertype() == CAMERA_QIU_JI) {
            helper.setImageDrawable(
                    R.id.iv_camera,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_dome_camera_online));
        } else if (item.getCamertype() == CAMERA_QIANG_JI) {
            helper.setImageDrawable(
                    R.id.iv_camera,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_box_camera_online));
        }
    }
}