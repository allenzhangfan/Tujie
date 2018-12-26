package com.netposa.component.sfjb.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import androidx.annotation.Keep;

@Keep
public class PersonInfoEntity implements MultiItemEntity {
    public static final int TYPE_PERSON_INFO = 0x2;
    public String title;
    public String description;
    public boolean dividerVisable;

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

    public boolean isDividerVisable() {
        return dividerVisable;
    }

    public void setDividerVisable(boolean dividerVisable) {
        this.dividerVisable = dividerVisable;
    }

    @Override
    public int getItemType() {
        return TYPE_PERSON_INFO;
    }
}
