package com.netposa.component.my.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.netposa.component.my.R;
import com.netposa.component.my.mvp.model.entity.PersonInfoEntity;

import java.util.List;

import static com.netposa.component.my.mvp.model.entity.PersonInfoEntity.TYPE_ITEM;
import static com.netposa.component.my.mvp.model.entity.PersonInfoDividerEntity.TYPE_DIVIDER;

/**
 * Author：yeguoqiang
 * Created time：2018/10/27 13:36
 */
public class PersonInfoAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public PersonInfoAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_DIVIDER, R.layout.item_view_divider);
        addItemType(TYPE_ITEM, R.layout.item_person_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int itemViewType = helper.getItemViewType();
        if (itemViewType == TYPE_ITEM) {
            PersonInfoEntity infoEntity = (PersonInfoEntity) item;
            helper.setText(R.id.tv_title, infoEntity.getTitle());
            helper.setText(R.id.tv_description, infoEntity.getDescription());
            helper.setVisible(R.id.divider_line, infoEntity.isDividerVisable());
        }
    }
}
