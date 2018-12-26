package com.netposa.component.room.entity;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Author：yeguoqiang
 * Created time：2018/10/22 16:09
 */
@Entity(tableName = "LoginConfig")
public class LoginConfigEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private String uuid;

    /**
     * 用户名
     */
    @ColumnInfo(name = "username")
    private String username;

    /**
     * 密码
     */
    @ColumnInfo(name = "password")
    private String password;

    public LoginConfigEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

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
