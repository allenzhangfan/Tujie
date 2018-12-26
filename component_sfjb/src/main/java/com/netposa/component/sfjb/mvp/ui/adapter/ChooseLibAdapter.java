package com.netposa.component.sfjb.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import static com.netposa.component.sfjb.app.SfjbConstants.IS_CHILD;
import static com.netposa.component.sfjb.app.SfjbConstants.NO_CHILD;

public class ChooseLibAdapter extends BaseQuickAdapter<OrgChoseEntity, BaseViewHolder> {


    @Inject
    public ChooseLibAdapter(@Nullable List<OrgChoseEntity> data) {
        super(R.layout.item_chose_single, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrgChoseEntity item) {
        boolean isChoose = item.isChoose();
        helper.setText(R.id.tv_name, item.getOrganizationDesc());
        int type = item.getOrganizationType();
        if (type == IS_CHILD) {
            helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_camera_group);
            helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go)
                    .setVisible(R.id.iv_menu, true);
            helper.addOnClickListener(R.id.ly_detail);
        } else if (type == NO_CHILD) {
            helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_personnel_library);
            helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go)
                    .setVisible(R.id.iv_menu, false);
        }
        helper.setChecked(R.id.rb_single, isChoose);
        helper.addOnClickListener(R.id.ly_single);
    }
}