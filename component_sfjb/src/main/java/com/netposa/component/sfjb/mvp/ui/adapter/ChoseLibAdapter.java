package com.netposa.component.sfjb.mvp.ui.adapter;

import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.common.log.Log;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import static com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity.TYPE_CAMERA;
import static com.netposa.component.sfjb.mvp.model.entity.OrgChoseEntity.TYPE_GROUP;

public class ChoseLibAdapter extends BaseQuickAdapter<OrgChoseEntity, BaseViewHolder> {


    @Inject
    public ChoseLibAdapter(@Nullable List<OrgChoseEntity> data) {
        super(R.layout.item_chose_lib, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrgChoseEntity item) {
        boolean isChoose = item.isChoose();
        helper.setText(R.id.tv_name, item.getOrganizationDesc());
        int type = item.getOrganizationType();
        helper.setBackgroundRes(R.id.iv_icon, R.drawable.ic_personnel_library);
        if (type == TYPE_GROUP) {
            helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go)
                    .setVisible(R.id.iv_menu, true);
            helper.addOnClickListener(R.id.ly_detail);
        } else if (type == TYPE_CAMERA) {
            helper.setBackgroundRes(R.id.iv_menu, R.drawable.ic_list_go)
                    .setVisible(R.id.iv_menu, false);
        }
        helper.setChecked(R.id.rb_single, isChoose);
        helper.addOnClickListener(R.id.ly_single);
    }
}