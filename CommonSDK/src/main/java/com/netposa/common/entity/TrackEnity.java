package com.netposa.common.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TrackEnity implements  Parcelable {

    private String titleName;

    private long startTime;

    private String cropPic;

    private String sceneImg;

    private List<String> point;// 经纬度点
    private String location;// 截图的四个点


    public TrackEnity() {
    }

    protected TrackEnity(Parcel in) {
        titleName = in.readString();
        startTime = in.readLong();
        cropPic = in.readString();
        sceneImg = in.readString();
        point = in.createStringArrayList();
        location = in.readString();
    }

    public static final Creator<TrackEnity> CREATOR = new Creator<TrackEnity>() {
        @Override
        public TrackEnity createFromParcel(Parcel in) {
            return new TrackEnity(in);
        }

        @Override
        public TrackEnity[] newArray(int size) {
            return new TrackEnity[size];
        }
    };

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getCropPic() {
        return cropPic;
    }

    public void setCropPic(String cropPic) {
        this.cropPic = cropPic;
    }

    public String getSceneImg() {
        return sceneImg;
    }

    public void setSceneImg(String sceneImg) {
        this.sceneImg = sceneImg;
    }

    public List<String> getPoint() {
        return point;
    }

    public void setPoint(List<String> point) {
        this.point = point;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(titleName);
        parcel.writeLong(startTime);
        parcel.writeString(cropPic);
        parcel.writeString(sceneImg);
        parcel.writeStringList(point);
        parcel.writeString(location);
    }
}
