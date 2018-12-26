package com.netposa.component.room.entity;

import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Author：yeguoqiang
 * Created time：2018/10/30 15:54
 */
@Entity(tableName = "SfjbSearchHistory")
public class SfjbSearchHistoryEntity {

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

    public SfjbSearchHistoryEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SfjbSearchHistoryEntity that = (SfjbSearchHistoryEntity) o;
        return uuid == that.uuid &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SfjbSearchHistoryEntity{");
        sb.append("uuid=").append(uuid);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
