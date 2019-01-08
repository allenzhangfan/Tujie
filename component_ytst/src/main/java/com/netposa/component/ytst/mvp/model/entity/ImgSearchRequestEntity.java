package com.netposa.component.ytst.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class ImgSearchRequestEntity implements Parcelable {
    /**
     * excludedDataKeys : ["string"]
     * includeDataKey : string
     * sessionKey : string
     */

    private String includeDataKey;
    private String sessionKey;
    private List<String> excludedDataKeys;
    private String distance;

    public ImgSearchRequestEntity() {
    }

    protected ImgSearchRequestEntity(Parcel in) {
        includeDataKey = in.readString();
        sessionKey = in.readString();
        excludedDataKeys = in.createStringArrayList();
        distance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(includeDataKey);
        dest.writeString(sessionKey);
        dest.writeStringList(excludedDataKeys);
        dest.writeString(distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImgSearchRequestEntity> CREATOR = new Creator<ImgSearchRequestEntity>() {
        @Override
        public ImgSearchRequestEntity createFromParcel(Parcel in) {
            return new ImgSearchRequestEntity(in);
        }

        @Override
        public ImgSearchRequestEntity[] newArray(int size) {
            return new ImgSearchRequestEntity[size];
        }
    };

    public String getIncludeDataKey() {
        return includeDataKey;
    }

    public void setIncludeDataKey(String includeDataKey) {
        this.includeDataKey = includeDataKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public List<String> getExcludedDataKeys() {
        return excludedDataKeys;
    }

    public void setExcludedDataKeys(List<String> excludedDataKeys) {
        this.excludedDataKeys = excludedDataKeys;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ImgSearchRequestEntity{");
        sb.append("includeDataKey='").append(includeDataKey).append('\'');
        sb.append(", sessionKey='").append(sessionKey).append('\'');
        sb.append(", excludedDataKeys=").append(excludedDataKeys);
        sb.append(", distance='").append(distance).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
