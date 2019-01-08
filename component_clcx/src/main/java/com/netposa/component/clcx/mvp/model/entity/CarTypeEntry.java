package com.netposa.component.clcx.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CarTypeEntry implements Parcelable {

    private String title;

    public boolean isSelect;

    public CarTypeEntry() {
    }

    public CarTypeEntry(Parcel in) {
        title = in.readString();
        isSelect = in.readByte() != 0;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static final Creator<CarTypeEntry> CREATOR = new Creator<CarTypeEntry>() {
        @Override
        public CarTypeEntry createFromParcel(Parcel in) {
            return new CarTypeEntry(in);
        }

        @Override
        public CarTypeEntry[] newArray(int size) {
            return new CarTypeEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CarTypeEntry{");
        sb.append("title='").append(title).append('\'');
        sb.append(", isSelect=").append(isSelect);
        sb.append('}');
        return sb.toString();
    }
}
