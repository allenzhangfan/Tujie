package com.netposa.component.room.entity;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "PlateType")
public class PlateTypeEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "isSelect")
    private boolean isSelect;

    public PlateTypeEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
