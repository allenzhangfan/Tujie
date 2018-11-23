package com.netposa.component.my.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.netposa.component.my.R;
import com.netposa.component.my.mvp.model.entity.MenuEntity;

import java.util.List;

/**
 * Created by yexiaokang on 2018/9/23.
 */
public class MyMenuAdapter extends BaseAdapter {

    private static final int TYPE_COUNT = 3;

    private List<MenuEntity> mEntities;

    public MyMenuAdapter(List<MenuEntity> entities) {
        mEntities = entities;
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return mEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mEntities.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int viewType = getItemViewType(position);
        final Context context = parent.getContext();
        MenuEntity menuEntity = mEntities.get(position);
        if (convertView == null) {
            if (viewType == MenuEntity.TYPE_DEFAULT) {//关于
                convertView = LayoutInflater.from(context).inflate(R.layout.item_my_menu, parent, false);
                DefaultViewHolder viewHolder = new DefaultViewHolder();
                viewHolder.imageIv = convertView.findViewById(R.id.iv_image);
                viewHolder.titleTv = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else if (viewType == MenuEntity.TYPE_TEXT) {//清理缓存
                convertView = LayoutInflater.from(context).inflate(R.layout.item_my_menu_text, parent, false);
                TextViewHolder viewHolder = new TextViewHolder();
                viewHolder.imageIv = convertView.findViewById(R.id.iv_image);
                viewHolder.titleTv = convertView.findViewById(R.id.tv_title);
                viewHolder.valueTv = convertView.findViewById(R.id.tv_value);
                convertView.setTag(viewHolder);
            } else if (viewType == MenuEntity.TYPE_TOGGLE) {//刷脸登录
                convertView = LayoutInflater.from(context).inflate(R.layout.item_my_menu_toggle, parent, false);
                ToggleViewHolder viewHolder = new ToggleViewHolder();
                viewHolder.imageIv = convertView.findViewById(R.id.iv_image);
                viewHolder.titleTv = convertView.findViewById(R.id.tv_title);
                viewHolder.switchBt = convertView.findViewById(R.id.switch_button);
                convertView.setTag(viewHolder);
            } else {
                throw new IllegalArgumentException("error viewType = " + viewType);
            }
        }
        if (viewType == MenuEntity.TYPE_DEFAULT) {
            DefaultViewHolder viewHolder = (DefaultViewHolder) convertView.getTag();
            viewHolder.imageIv.setImageResource(menuEntity.getResId());
            viewHolder.titleTv.setText(menuEntity.getTitle());
        } else if (viewType == MenuEntity.TYPE_TEXT) {
            TextViewHolder viewHolder = (TextViewHolder) convertView.getTag();
            viewHolder.imageIv.setImageResource(menuEntity.getResId());
            viewHolder.titleTv.setText(menuEntity.getTitle());
            viewHolder.valueTv.setText(menuEntity.getValue());
        } else if (viewType == MenuEntity.TYPE_TOGGLE) {
            ToggleViewHolder viewHolder = (ToggleViewHolder) convertView.getTag();
            viewHolder.imageIv.setImageResource(menuEntity.getResId());
            viewHolder.titleTv.setText(menuEntity.getTitle());
            viewHolder.switchBt.setChecked(menuEntity.isChecked());
            viewHolder.switchBt.setOnCheckedChangeListener(menuEntity.getOnCheckedChangeListener());
        } else {
            throw new IllegalArgumentException("error viewType = " + viewType);
        }
        return convertView;
    }

    private static class DefaultViewHolder {
        ImageView imageIv;
        TextView titleTv;
    }

    private static class TextViewHolder {
        ImageView imageIv;
        TextView titleTv;
        TextView valueTv;
    }

    private static class ToggleViewHolder {
        ImageView imageIv;
        TextView titleTv;
        SwitchButton switchBt;
    }
}
