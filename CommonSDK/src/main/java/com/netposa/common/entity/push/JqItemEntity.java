package com.netposa.common.entity.push;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：yeguoqiang
 * Created time：2018/10/29 14:13
 */
public class JqItemEntity implements Parcelable {

    private String id;//
    private String captureImgUrl; //抓拍图片
    private String deployImgUrl;//布控图片
    private String scenneImg;//原图
    private String alarmTime; //抓拍时间
    private String cameraLocation; //摄像机名称
    private String captureLibName; //布控任务名称
    private String captureLib; // 所属库
    private String capturetTipMsg; //备注
    private String target; //目标
    private Double latitude;
    private Double longitude;
    private String taskReason;
    private String level;
    private String startTime;
    private String endTime;


    /**
     * 人的属性
     */
    private String captureName;
    private String captureGender;
    private String captureIdCard;
    private String captureAdress; //户籍地址
    private String captureDetailInfo;

    /**
     * 车的属性
     */
    private String dealPerson; //处理人


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
        id = in.readString();
        captureImgUrl = in.readString();
        deployImgUrl = in.readString();
        scenneImg =in.readString();
        alarmTime = in.readString();
        cameraLocation = in.readString();
        captureLibName = in.readString();
        captureLib = in.readString();
        capturetTipMsg = in.readString();
        target = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        taskReason = in.readString();
        level = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        captureName = in.readString();
        captureGender = in.readString();
        captureIdCard = in.readString();
        captureAdress = in.readString();
        captureDetailInfo = in.readString();
        dealPerson = in.readString();
        itemType = in.readInt();
        itemHandleType = in.readInt();
        similarity = in.readInt();
        carNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(captureImgUrl);
        dest.writeString(deployImgUrl);
        dest.writeString(scenneImg);
        dest.writeString(alarmTime);
        dest.writeString(cameraLocation);
        dest.writeString(captureLibName);
        dest.writeString(captureLib);
        dest.writeString(capturetTipMsg);
        dest.writeString(target);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(taskReason);
        dest.writeString(level);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(captureName);
        dest.writeString(captureGender);
        dest.writeString(captureIdCard);
        dest.writeString(captureAdress);
        dest.writeString(captureDetailInfo);
        dest.writeString(dealPerson);
        dest.writeInt(itemType);
        dest.writeInt(itemHandleType);
        dest.writeInt(similarity);
        dest.writeString(carNumber);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getScenneImg() { return scenneImg; }

    public void setScenneImg(String scenneImg) { this.scenneImg = scenneImg; }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getCameraLocation() {
        return cameraLocation;
    }

    public void setCameraLocation(String cameraLocation) {
        this.cameraLocation = cameraLocation;
    }

    public String getCaptureLibName() {
        return captureLibName;
    }

    public void setCaptureLibName(String captureLibName) {
        this.captureLibName = captureLibName;
    }

    public String getCaptureLib() {
        return captureLib;
    }

    public void setCaptureLib(String captureLib) {
        this.captureLib = captureLib;
    }

    public String getCapturetTipMsg() {
        return capturetTipMsg;
    }

    public void setCapturetTipMsg(String capturetTipMsg) {
        this.capturetTipMsg = capturetTipMsg;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTaskReason() {
        return taskReason;
    }

    public void setTaskReason(String taskReason) {
        this.taskReason = taskReason;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCaptureName() {
        return captureName;
    }

    public void setCaptureName(String captureName) {
        this.captureName = captureName;
    }

    public String getCaptureGender() {
        return captureGender;
    }

    public void setCaptureGender(String captureGender) {
        this.captureGender = captureGender;
    }

    public String getCaptureIdCard() {
        return captureIdCard;
    }

    public void setCaptureIdCard(String captureIdCard) {
        this.captureIdCard = captureIdCard;
    }

    public String getCaptureAdress() {
        return captureAdress;
    }

    public void setCaptureAdress(String captureAdress) {
        this.captureAdress = captureAdress;
    }

    public String getCaptureDetailInfo() {
        return captureDetailInfo;
    }

    public void setCaptureDetailInfo(String captureDetailInfo) {
        this.captureDetailInfo = captureDetailInfo;
    }

    public String getDealPerson() {
        return dealPerson;
    }

    public void setDealPerson(String dealPerson) {
        this.dealPerson = dealPerson;
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

    public static Creator<JqItemEntity> getCREATOR() {
        return CREATOR;
    }

}
