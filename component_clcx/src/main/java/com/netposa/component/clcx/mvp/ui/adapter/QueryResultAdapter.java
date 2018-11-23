package com.netposa.component.clcx.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.clcx.R;
import com.netposa.component.clcx.mvp.model.entity.QueryResultEntity;
import java.util.List;
import javax.inject.Inject;
import androidx.annotation.Nullable;


public class QueryResultAdapter extends BaseQuickAdapter<QueryResultEntity, BaseViewHolder> {
    @Inject
    Context mContext;

    @Inject
    public QueryResultAdapter(@Nullable List<QueryResultEntity> list) {
        super(R.layout.item_query_result, list);

    }

    @Override
    protected void convert(BaseViewHolder helper, QueryResultEntity item) {
        helper.setText(R.id.tv_car_num, item.getNum());
        helper.setText(R.id.tv_sxt, item.getName());
        helper.setText(R.id.tv_date, item.getDate());
        ImageView carView = helper.getView(R.id.iv_car);
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(6);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
//        Glide.with(mContext).load(item.getPic()).apply(options).into(carView);
    }
}
