package com.netposa.component.clcx.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.model.entity.CarTypeEntry;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


public class CarTypeAdapter extends BaseQuickAdapter<CarTypeEntry, BaseViewHolder> {

    @Inject
    public CarTypeAdapter(@Nullable List<CarTypeEntry> data) {
        super(R.layout.item_car_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarTypeEntry item) {
        helper.setText(R.id.tv_title,item.getTitle());
        helper.addOnClickListener(R.id.ll_car_item);
        if (item.isSelect) {
            helper.setImageDrawable(
                    R.id.iv_check_box,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_choosed));
        } else {
            helper.setImageDrawable(
                    R.id.iv_check_box,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_unchoosed));
        }
    }
}
