package com.netposa.component.my.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author：yeguoqiang
 * Created time：2018/12/24 13:57
 */
public class LogOutResponseEntity {

    /**
     * message : 成功
     */
    @SerializedName(value = "message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
