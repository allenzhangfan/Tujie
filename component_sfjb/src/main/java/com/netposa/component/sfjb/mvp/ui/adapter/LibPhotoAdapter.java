package com.netposa.component.sfjb.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.netposa.component.sfjb.R;
import com.netposa.component.sfjb.mvp.model.entity.DetailInfoEntity;
import com.netposa.component.sfjb.mvp.model.entity.PersonInfoEntity;

import java.util.List;

import static com.netposa.component.sfjb.mvp.model.entity.DetailInfoEntity.TYPE_DETAIL_INFO;
import static com.netposa.component.sfjb.mvp.model.entity.PersonInfoEntity.TYPE_PERSON_INFO;

public class LibPhotoAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public LibPhotoAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_PERSON_INFO, R.layout.item_person_info);
        addItemType(TYPE_DETAIL_INFO, R.layout.item_detail_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemViewType = item.getItemType();
        if (itemViewType == TYPE_DETAIL_INFO) {
            DetailInfoEntity detailEntity = (DetailInfoEntity) item;
            helper.setText(R.id.tv_title, detailEntity.getTitle());
            helper.setText(R.id.tv_description, detailEntity.getDescription());
        } else if (itemViewType == TYPE_PERSON_INFO) {
            PersonInfoEntity infoEntity = (PersonInfoEntity) item;
            helper.setVisible(R.id.divider_line, infoEntity.isDividerVisable());
            helper.setText(R.id.tv_title, infoEntity.getTitle());
            helper.setText(R.id.tv_description, infoEntity.getDescription());
            helper.addOnClickListener(R.id.iv_location);
        }
    }
}
