package com.netposa.component.spjk.mvp.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netposa.component.spjk.R;
import com.netposa.component.spjk.mvp.model.entity.HistoryVideoResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import static com.netposa.common.utils.TimeUtils.FORMAT10;
import static com.netposa.common.utils.TimeUtils.FORMAT_ONE;

/**
 * Author：yeguoqiang
 * Created time：2018/10/25 14:29
 */
public class HistoryVideoAdapter extends BaseQuickAdapter<HistoryVideoResponseEntity.VideoListEntity, BaseViewHolder> {

    @Inject
    public HistoryVideoAdapter(@Nullable List<HistoryVideoResponseEntity.VideoListEntity> data) {
        super(R.layout.item_history_video, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryVideoResponseEntity.VideoListEntity item) {
        String begin_time = null;
        String end_time = null;
        try {
            begin_time = new SimpleDateFormat(FORMAT_ONE).format(new SimpleDateFormat(FORMAT10).parse(item.getBeginTime().substring(0, 14)));
            end_time = new SimpleDateFormat(FORMAT_ONE).format(new SimpleDateFormat(FORMAT10).parse(item.getEndTime().substring(0, 14)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        helper.setText(R.id.tv_item_time, begin_time + "  -  " + end_time);
    }
}
