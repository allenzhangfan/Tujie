package com.netposa.component.sfjb.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class DetailInfoEntity implements MultiItemEntity {
    public static final int TYPE_DETAIL_INFO = 0x1;
    public String title;
    public String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getItemType() {
        return TYPE_DETAIL_INFO;
    }
}
