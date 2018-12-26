package com.netposa.component.room.entity;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SpjkCollectionDevice")
public class SpjkCollectionDeviceEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "camerid")
    private String camerid;

    @ColumnInfo(name = "camername")
    private String camername;

    @ColumnInfo(name = "camertype")
    private int camertype;

    public SpjkCollectionDeviceEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getCamerid() {
        return camerid;
    }

    public void setCamerid(String camerid) {
        this.camerid = camerid;
    }

    public String getCamername() {
        return camername;
    }

    public void setCamername(String camername) {
        this.camername = camername;
    }

    public int getCamertype() {
        return camertype;
    }

    public void setCamertype(int camertype) {
        this.camertype = camertype;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpjkCollectionDeviceEntity{");
        sb.append("uuid=").append(uuid);
        sb.append(", camerid='").append(camerid).append('\'');
        sb.append(", camername='").append(camername).append('\'');
        sb.append(", camertype=").append(camertype);
        sb.append('}');
        return sb.toString();
    }
}
