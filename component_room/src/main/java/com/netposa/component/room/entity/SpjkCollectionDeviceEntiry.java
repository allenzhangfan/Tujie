package com.netposa.component.room.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SpjkCollectionDevice")
public class SpjkCollectionDeviceEntiry {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;

    @ColumnInfo(name = "camerid")
    private String camerid;

    @ColumnInfo(name = "camername")
    private String camername;

    @ColumnInfo(name = "camertype")
    private int camertype;

    @NonNull
    public int getUid() {
        return uid;
    }

    public void setUid(@NonNull int uid) {
        this.uid = uid;
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
        final StringBuilder sb = new StringBuilder("SpjkCollectionDeviceEntiry{");
        sb.append("uid=").append(uid);
        sb.append(", camerid='").append(camerid).append('\'');
        sb.append(", camername='").append(camername).append('\'');
        sb.append(", camertype=").append(camertype);
        sb.append('}');
        return sb.toString();
    }
}
