package com.netposa.component.clcx.mvp.ui.adapter;

import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.model.entity.CarTypeEntry;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


public class CarTypeAdapter extends BaseQuickAdapter<CarTypeEntry, BaseViewHolder> {

    @Inject
    public CarTypeAdapter(@Nullable ArrayList<CarTypeEntry> data) {
        super(R.layout.item_car_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarTypeEntry item) {
        helper.setText(R.id.tv_title, item.getTitle());
       TextView title= helper.getView(R.id.tv_title);
        if (item.isSelect) {
            helper.setImageDrawable(
                    R.id.iv_check_box,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_choosed));
            title.getPaint().setFakeBoldText(true);
        } else {
            helper.setImageDrawable(
                    R.id.iv_check_box,
                    ContextCompat.getDrawable(mContext, R.drawable.ic_unchoosed));
            title.getPaint().setFakeBoldText(false);
        }
    }
}
