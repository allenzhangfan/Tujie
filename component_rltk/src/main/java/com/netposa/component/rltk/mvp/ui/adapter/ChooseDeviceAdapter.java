package com.netposa.component.rltk.mvp.ui.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.rltk.R;
import com.netposa.component.rltk.mvp.model.entity.OrgChoseEntity;
import com.netposa.component.rltk.mvp.model.entity.SearchDeviceResponseEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import static com.netposa.common.constant.GlobalConstants.CAMERA_QIANG_JI;
import static com.netposa.common.constant.GlobalConstants.CAMERA_QIU_JI;
import static com.netposa.component.rltk.app.RltkConstants.CAMERA;
import static com.netposa.component.rltk.app.RltkConstants.CHILD_COUNT;
import static com.netposa.component.rltk.app.RltkConstants.ORG;

public class ChooseDeviceAdapter extends BaseQuickAdapter<OrgChoseEntity, BaseViewHolder> {


    @Inject
    public ChooseDeviceAdapter(@Nullable List<OrgChoseEntity> data) {
        super(R.layout.item_chose_single, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrgChoseEntity item) {
        boolean choose = item.isChoose();
        helper.setText(R.id.tv_name, item.getOrganizationDesc());
        String type = item.getOrganizationType();
        int childcount = item.getChildcount();
        if (TextUtils.isEmpty(type)) {
            helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_box_camera_online_list);//默认
            helper.setChecked(R.id.rb_single, choose);
            helper.addOnClickListener(R.id.ly_single);
            return;
        } else {
            if (type.equals(ORG)) {
                helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_camera_group);
                if (childcount > CHILD_COUNT) {
                    helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go).setVisible(R.id.iv_menu, true);
                    helper.addOnClickListener(R.id.ly_detail);
                } else {
                    helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go).setVisible(R.id.iv_menu, false);
                }
            } else if (type.equals(CAMERA)) {
                int type_camera = Integer.parseInt(item.getCameraType());
                if (type_camera == CAMERA_QIANG_JI) {
                    helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_box_camera_online_list);
                } else if (type_camera == CAMERA_QIU_JI) {
                    helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_dome_camera_online_list);
                }
                helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go).setVisible(R.id.iv_menu, false);
            }
            helper.setChecked(R.id.rb_single, choose);
            helper.addOnClickListener(R.id.ly_single);
        }
    }
}