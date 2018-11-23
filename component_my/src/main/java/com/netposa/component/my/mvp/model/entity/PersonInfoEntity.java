package com.netposa.component.my.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Author：yeguoqiang
 * Created time：2018/10/27 13:33
 */
public class PersonInfoEntity implements MultiItemEntity {

    public static final int TYPE_ITEM = 0x1;

    private boolean dividerVisable;
    private String title;
    private String description;

    public boolean isDividerVisable() {
        return dividerVisable;
    }

    public void setDividerVisable(boolean dividerVisable) {
        this.dividerVisable = dividerVisable;
    }

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
        return TYPE_ITEM;
    }
}
