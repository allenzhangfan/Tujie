package com.netposa.component.sfjb.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FaceDetailEntity implements Parcelable {

    private String  picUrl;
    private double  similarity;
    private String  name;
    private String  gender;
    private String  idCard;
    private String  libName;// 所属库
    private long  createTime;
    private String  remark;

    public FaceDetailEntity() {
    }


    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    protected FaceDetailEntity(Parcel in) {
        picUrl = in.readString();
        similarity = in.readDouble();
        name = in.readString();
        gender = in.readString();
        idCard = in.readString();
        libName = in.readString();
        createTime = in.readLong();
        remark = in.readString();
    }

    public static final Creator<FaceDetailEntity> CREATOR = new Creator<FaceDetailEntity>() {
        @Override
        public FaceDetailEntity createFromParcel(Parcel in) {
            return new FaceDetailEntity(in);
        }

        @Override
        public FaceDetailEntity[] newArray(int size) {
            return new FaceDetailEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(picUrl);
        parcel.writeDouble(similarity);
        parcel.writeString(name);
        parcel.writeString(gender);
        parcel.writeString(idCard);
        parcel.writeString(libName);
        parcel.writeLong(createTime);
        parcel.writeString(remark);
    }

}
