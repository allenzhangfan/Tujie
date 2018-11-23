package com.netposa.component.jq.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/29 14:13
 */
public class JqItemEntity implements Parcelable {

    public static final int TYPE_PEOPLE = 0x1;
    public static final int TYPE_CAR = 0x2;

    public static final int TYPE_VALID = 0x10;
    public static final int TYPE_INVALID = 0x11;
    public static final int TYPE_SUSPENDING = 0x12;

    private String captureImgUrl;
    private String deployImgUrl;
    private String address;
    private String time;
    private String cameraLocation;

    /**
     * 判断是item类型是车还是人
     **/
    private int itemType;
    /**
     * 判断处理结果是有效、无效还是待处理
     **/
    private int itemHandleType;
    /**
     * 人(特有属性)
     **/
    private int similarity;
    /**
     * 车辆(特有属性)
     **/
    private String carNumber;

    public JqItemEntity() {
    }

    protected JqItemEntity(Parcel in) {
        captureImgUrl = in.readString();
        deployImgUrl = in.readString();
        address = in.readString();
        time = in.readString();
        cameraLocation = in.readString();
        itemType = in.readInt();
        itemHandleType = in.readInt();
        similarity = in.readInt();
        carNumber = in.readString();
    }

    public String getCaptureImgUrl() {
        return captureImgUrl;
    }

    public void setCaptureImgUrl(String captureImgUrl) {
        this.captureImgUrl = captureImgUrl;
    }

    public String getDeployImgUrl() {
        return deployImgUrl;
    }

    public void setDeployImgUrl(String deployImgUrl) {
        this.deployImgUrl = deployImgUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCameraLocation() {
        return cameraLocation;
    }

    public void setCameraLocation(String cameraLocation) {
        this.cameraLocation = cameraLocation;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getItemHandleType() {
        return itemHandleType;
    }

    public void setItemHandleType(int itemHandleType) {
        this.itemHandleType = itemHandleType;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public static final Creator<JqItemEntity> CREATOR = new Creator<JqItemEntity>() {
        @Override
        public JqItemEntity createFromParcel(Parcel in) {
            return new JqItemEntity(in);
        }

        @Override
        public JqItemEntity[] newArray(int size) {
            return new JqItemEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(captureImgUrl);
        dest.writeString(deployImgUrl);
        dest.writeString(address);
        dest.writeString(time);
        dest.writeString(cameraLocation);
        dest.writeInt(itemType);
        dest.writeInt(itemHandleType);
        dest.writeInt(similarity);
        dest.writeString(carNumber);
    }
}
