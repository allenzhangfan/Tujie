/*
 * Copyright (C) 2016 LingDaNet.Co.Ltd. All Rights Reserved.
 */
package com.netposa.commonres.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.netposa.common.entity.PopupWindowEntity;
import com.netposa.commonres.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：赵炜
 * 创建日期：2016/10/10
 * 邮箱：zhaowei0920@lingdanet.com
 * 描述：PopWindow 工具类
 */

public class PopupWindowUtil {

    private ListView listView;
    private PopupWindow window;
    //窗口在x轴偏移量
    private int xOff = 0;
    //窗口在y轴的偏移量
    private int yOff = 0;

    public PopupWindowUtil(Context context, List<PopupWindowEntity> datas) {

        window = new PopupWindow(context);
        //ViewGroup.LayoutParams.WRAP_CONTENT，自动包裹所有的内容
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        //点击 back 键的时候，窗口会自动消失
        window.setBackgroundDrawable(new BitmapDrawable());

        View localView = LayoutInflater.from(context).inflate(R.layout.base_common_ppw_menu, null);
        listView = (ListView) localView.findViewById(R.id.menu_lv);

        listView.setAdapter(new MyAdapter(context, datas));
        listView.setTag(window);
        //设置显示的视图
        window.setContentView(localView);
    }

    public void setItemClickListener(AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }

    public void dismiss() {
        window.dismiss();
    }

    /**
     * @param xOff x轴（左右）偏移
     * @param yOff y轴（上下）偏移
     */
    public void setOff(int xOff, int yOff) {
        this.xOff = xOff;
        this.yOff = yOff;
    }

    /**
     * @param paramView 点击的按钮
     * @param ratio     倍率值，与paramView的积等于popwindow的宽度
     */
    public void show(View paramView, float ratio) {
        window.setWidth((int) (paramView.getWidth() * ratio));
        window.showAsDropDown(paramView, xOff, yOff);
        //更新窗口状态
        window.update();
    }

    class MyAdapter extends BaseAdapter {

        private Context context;
        private List<PopupWindowEntity> mDatas;

        public MyAdapter(Context context, List<PopupWindowEntity> datas) {
            this.context = context;
            if (datas == null) {
                datas = new ArrayList<PopupWindowEntity>();
            }
            mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            if (mDatas == null)
                return null;
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.base_common_item_pw_menu, null);
                viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tv_item_pw_menu);
                viewHolder.ivItem = (ImageView) convertView.findViewById(R.id.iv_item_pw_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            PopupWindowEntity entity = (PopupWindowEntity) getItem(position);

            if (entity != null) {
                viewHolder.tvItem.setText(entity.getText());
                if (entity.getResId() != 0) {
                    viewHolder.ivItem.setImageResource(entity.getResId());
                }
            }
            return convertView;
        }

        class ViewHolder {
            TextView tvItem;
            ImageView ivItem;
        }
    }
}
