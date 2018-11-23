package com.netposa.component.clcx.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.model.entity.CarInfoEntity;
import com.netposa.component.clcx.mvp.model.entity.CarNameEntity;

import java.util.List;

import static com.netposa.component.clcx.mvp.model.entity.CarDividerEntity.TYPE_ITEM;
import static com.netposa.component.clcx.mvp.model.entity.CarInfoEntity.TYPE_ITEM_INFO;
import static com.netposa.component.clcx.mvp.model.entity.CarNameEntity.TYPE_ITEM_NAME;

public class CarRecordAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public CarRecordAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_ITEM, R.layout.item_view_divider);
        addItemType(TYPE_ITEM_NAME, R.layout.item_car_title_name);
        addItemType(TYPE_ITEM_INFO, R.layout.item_car_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemViewType = item.getItemType();
        if (itemViewType == TYPE_ITEM_NAME) {
            CarNameEntity nameEntity = (CarNameEntity) item;
            helper.setText(R.id.tv_title, nameEntity.getName());
        } else if (itemViewType == TYPE_ITEM_INFO) {
            CarInfoEntity infoEntity = (CarInfoEntity) item;
            helper.setVisible(R.id.divider_line,infoEntity.isDividerVisable());
            helper.setGone(R.id.iv_location, infoEntity.isGpsVisable());
            helper.setText(R.id.tv_title, infoEntity.getTitle());
            helper.setText(R.id.tv_description, infoEntity.getDescription());
            helper.addOnClickListener(R.id.iv_location);
        }
    }
}
