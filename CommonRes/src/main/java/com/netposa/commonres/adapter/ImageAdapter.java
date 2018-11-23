package com.netposa.commonres.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.entity.ImageEntity;
import com.netposa.commonres.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * Created by yexiaokang on 2018/9/17.
 */
public class ImageAdapter extends BaseAdapter {

    private static final int TYPE_COUNT = 3;

    private List<ImageEntity> mEntities;
    private ImageLoader mImageLoader;
    private Context mContext;

    public ImageAdapter(List<ImageEntity> entities, Context context) {
        mEntities = entities;
        mContext = context;
        mImageLoader = ArmsUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    public int getCount() {
        return mEntities.size();
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
    public Object getItem(int position) {
        return mEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image,
                    parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageEntity imageEntity = mEntities.get(position);
        switch (getItemViewType(position)) {
            case ImageEntity.TYPE_IMAGE:
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageLoader.loadImage(mContext, ImageConfigImpl
                        .builder()
                        .cacheStrategy(0)
                        .placeholder(R.drawable.ic_image_default)
                        .errorPic(R.drawable.ic_image_load_failed)
                        .url(imageEntity.getPath())
                        .imageView(holder.imageView)
                        .build());
                holder.imageView.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.white)));
                break;
            case ImageEntity.TYPE_ADD:
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.imageView.setImageResource(R.drawable.ic_add_black);
                holder.imageView.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.color_bg_grey)));
                break;
            case ImageEntity.TYPE_DEL:
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.imageView.setImageResource(R.drawable.ic_delete_black);
                holder.imageView.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.color_bg_grey)));
                break;
        }
        return convertView;
    }

    public int getPictureCount() {
        int count = 0;
        for (ImageEntity entity : mEntities) {
            if (entity.getType() == ImageEntity.TYPE_IMAGE) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<String> getPicturePath() {
        ArrayList<String> value = new ArrayList<>();
        for (ImageEntity entity : mEntities) {
            if (entity.getType() == ImageEntity.TYPE_IMAGE) {
                value.add(entity.getPath());
            }
        }
        return value;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
