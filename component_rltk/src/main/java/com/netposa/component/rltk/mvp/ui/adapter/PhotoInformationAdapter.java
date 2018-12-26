package com.netposa.component.rltk.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.netposa.component.rltk.R;
import com.netposa.component.rltk.mvp.model.entity.PersonInfoEntity;
import com.netposa.component.rltk.mvp.model.entity.PersonNameEntity;
import java.util.List;
import static com.netposa.component.rltk.mvp.model.entity.PersonDividerEntity.TYPE_ITEM;
import static com.netposa.component.rltk.mvp.model.entity.PersonInfoEntity.TYPE_ITEM_INFO;
import static com.netposa.component.rltk.mvp.model.entity.PersonNameEntity.TYPE_ITEM_NAME;

public class PhotoInformationAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public PhotoInformationAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_ITEM, R.layout.item_view_divider);
        addItemType(TYPE_ITEM_NAME, R.layout.item_car_title_name);
        addItemType(TYPE_ITEM_INFO, R.layout.item_car_person_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemViewType = item.getItemType();
        if (itemViewType == TYPE_ITEM_NAME) {
            PersonNameEntity nameEntity = (PersonNameEntity) item;
            helper.setText(R.id.tv_title, nameEntity.getName());
        } else if (itemViewType == TYPE_ITEM_INFO) {
            PersonInfoEntity infoEntity = (PersonInfoEntity) item;
            helper.setVisible(R.id.divider_line, infoEntity.isDividerVisable());
            helper.setGone(R.id.iv_location, infoEntity.isGpsVisable());
            helper.setText(R.id.tv_title, infoEntity.getTitle());
            helper.setText(R.id.tv_description, infoEntity.getDescription());
            helper.addOnClickListener(R.id.iv_location);
        }
    }
}
