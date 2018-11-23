package com.netposa.component.gzt.mvp.model.entity;

import androidx.annotation.DrawableRes;

public class GztEntity {

    private int id;
    @DrawableRes
    private int resId;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DrawableRes
    public int getResId() {
        return resId;
    }

    public void setResId(@DrawableRes int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
