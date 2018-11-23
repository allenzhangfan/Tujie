package com.netposa.component.login.mvp.model.entity;

import androidx.annotation.Keep;

/**
 * Author：yeguoqiang
 * Created time：2018/11/1 12:49
 */
@Keep
public class LoginRequestEntity {

    private String username;
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
