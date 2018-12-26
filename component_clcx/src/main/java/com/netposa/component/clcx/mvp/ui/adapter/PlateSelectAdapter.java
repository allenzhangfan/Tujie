package com.netposa.component.clcx.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.model.entity.PlateSelectEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Author：yeguoqiang
 * Created time：2018/11/22 15:44
 */
public class PlateSelectAdapter extends BaseQuickAdapter<PlateSelectEntity, BaseViewHolder> {

    public PlateSelectAdapter(@Nullable List<PlateSelectEntity> data) {
        super(R.layout.item_plate_select, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateSelectEntity item) {
        helper.setText(R.id.bt_plate_name, item.getName());
    }
}
