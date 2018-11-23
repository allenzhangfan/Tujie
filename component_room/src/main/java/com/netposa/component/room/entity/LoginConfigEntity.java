package com.netposa.component.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Author：yeguoqiang
 * Created time：2018/10/22 16:09
 */
@Entity(tableName = "LoginConfig")
public class LoginConfigEntity {

    /**
     * 主键
     */
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
