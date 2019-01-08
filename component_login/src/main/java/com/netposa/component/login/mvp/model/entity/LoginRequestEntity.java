package com.netposa.component.login.mvp.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Author：yeguoqiang
 * Created time：2018/11/1 12:49
 */
public class LoginRequestEntity {

    @SerializedName(value = "username")
    private String username;
    @SerializedName(value = "password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
