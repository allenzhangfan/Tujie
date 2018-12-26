package com.netposa.component.spjk.mvp.ui.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.app.SpjkConstants;
import com.netposa.component.spjk.mvp.model.entity.SpjkListDeviceResponseEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import static com.netposa.common.constant.GlobalConstants.CAMERA_QIANG_JI;
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIU_JI;

public class DeviceParentAdapter extends BaseQuickAdapter<SpjkListDeviceResponseEntity.DeviceTreeListBean, BaseViewHolder> {

    @Inject
    public DeviceParentAdapter(@Nullable List<SpjkListDeviceResponseEntity.DeviceTreeListBean> data) {
        super(R.layout.item_device_city_camera, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpjkListDeviceResponseEntity.DeviceTreeListBean item) {
        helper.setText(R.id.tv_name, item.getName());
        String type = item.getNodeType();
        String type_camera_str = item.getCameraType();
        if (TextUtils.isEmpty(type)) {
            type = SpjkConstants.CAMERA;
        }
        int type_camera;
        if (TextUtils.isEmpty(type_camera_str)) {
            type_camera = CAMERA_QIANG_JI;
        } else {
            type_camera = Integer.valueOf(type_camera_str);
        }
        int childcount = item.getChildrenCount();
        if (type.equals(SpjkConstants.ORG)) {
            helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_camera_group);
            if (childcount > SpjkConstants.CHILD_COUNT) {
                helper.addOnClickListener(R.id.ly_detail);
                helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go).setVisible(R.id.iv_menu, true);
            } else {
                helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go).setVisible(R.id.iv_menu, false);
            }
        } else if (type.equals(SpjkConstants.CAMERA)) {
            helper.addOnClickListener(R.id.ly_detail);
            if (type_camera == CAMERA_QIANG_JI) {
                helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_box_camera_online_list);
                helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_gps).addOnClickListener(R.id.iv_menu);
            } else if (type_camera == CAMERA_QIU_JI) {
                helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_dome_camera_online_list);
                helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_gps).addOnClickListener(R.id.iv_menu);
            }
        }
    }
}