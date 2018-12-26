package com.netposa.component.room.entity;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Author：yeguoqiang
 * Created time：2018/10/30 15:54
 */
@Entity(tableName = "JqSearchHistory")
public class JqSearchHistoryEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JqSearchHistoryEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }
}
