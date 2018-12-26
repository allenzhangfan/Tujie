package com.netposa.component.jq.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class AlarmDetailForIdResponseEntity implements Parcelable {

    /**
     * id : 6b589337-10c3-4635-916f-a8ee894f5ea2
     * absTime : 1543988277202
     * alarmAddress : 大门36
     * alarmLevel : 1
     * alarmStatus : 2
     * alarmTime : 1543988277299
     * alarmType : 2
     * deviceId : fxSJ000139MWIx1M
     * ext : [{"birthday":"","libId":"517697470927470592","nation":"未知","sex":"0","libName":"","remark":"","targetImage":"PFSB:/viasbimg/bimg/data/20181204/14_0/d1065061a3c4fd28957dcf22ba883c65","score":57,"createTime":"2018-12-04 14:07:21","idcard":"","census":"","name":"111","personId":"519515057676091392"}]
     * idCard :
     * imageUrl : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181205/13_0/a73db965d73ab9ef3e097aa4d3b534fa
     * libId : 517697470927470592
     * libName :
     * latitude : null
     * longitude : null
     * name : 111
     * recordId : 6d6e0887-0f47-4604-9cc0-ad04ef765179
     * score : 57
     * sex : null
     * taskId : e443fb44-baa6-491f-b300-cd8079c3af13
     * taskName : 2018120005
     * userName : 管理员
     * taskReason : 人脸
     * note : 当前警情有效
     * loginId : 74534477-9089-46a0-9cec-2b63381f5a08,af2005b8-59df-4a71-a231-e0e6d232b60c,175ebc50-9794-43cc-810f-cb34cdbdfec9
     * startTime : 1543981065000
     * endTime : 1546227465000
     * target : 人员布控库01,人员布控库02,人员布控库03,人员布控库04,人员布控库05,人脸布控库001
     * receiveUser : null
     * sceneImg : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181205/13_0/61bcdcabe6de477a4f018597ed92dbd1
     * position : 1697.546.1753.602
     */

    private String id;
    private long absTime;
    private String alarmAddress;
    private String alarmLevel;
    private String alarmStatus;
    private long alarmTime;
    private String alarmType;
    private String deviceId;
    private String ext;
    private String idCard;
    private String imageUrl;
    private String libId;
    private String libName;
    private double latitude;
    private double longitude;
    private String name;
    private String recordId;
    private int score;
    private int sex;
    private String taskId;
    private String taskName;
    private String userName;
    private String taskReason;
    private String note;
    private String loginId;
    private long startTime;
    private long endTime;
    private String target;
    private String receiveUser;
    private String sceneImg;
    private String position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAbsTime() {
        return absTime;
    }

    public void setAbsTime(long absTime) {
        this.absTime = absTime;
    }

    public String getAlarmAddress() {
        return alarmAddress;
    }

    public void setAlarmAddress(String alarmAddress) {
        this.alarmAddress = alarmAddress;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLibId() {
        return libId;
    }

    public void setLibId(String libId) {
        this.libId = libId;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTaskReason() {
        return taskReason;
    }

    public void setTaskReason(String taskReason) {
        this.taskReason = taskReason;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getSceneImg() {
        return sceneImg;
    }

    public void setSceneImg(String sceneImg) {
        this.sceneImg = sceneImg;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.absTime);
        dest.writeString(this.alarmAddress);
        dest.writeString(this.alarmLevel);
        dest.writeString(this.alarmStatus);
        dest.writeLong(this.alarmTime);
        dest.writeString(this.alarmType);
        dest.writeString(this.deviceId);
        dest.writeString(this.ext);
        dest.writeString(this.idCard);
        dest.writeString(this.imageUrl);
        dest.writeString(this.libId);
        dest.writeString(this.libName);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.name);
        dest.writeString(this.recordId);
        dest.writeInt(this.score);
        dest.writeInt(this.sex);
        dest.writeString(this.taskId);
        dest.writeString(this.taskName);
        dest.writeString(this.userName);
        dest.writeString(this.taskReason);
        dest.writeString(this.note);
        dest.writeString(this.loginId);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeString(this.target);
        dest.writeString(this.receiveUser);
        dest.writeString(this.sceneImg);
        dest.writeString(this.position);
    }

    public AlarmDetailForIdResponseEntity() {
    }

    protected AlarmDetailForIdResponseEntity(Parcel in) {
        this.id = in.readString();
        this.absTime = in.readLong();
        this.alarmAddress = in.readString();
        this.alarmLevel = in.readString();
        this.alarmStatus = in.readString();
        this.alarmTime = in.readLong();
        this.alarmType = in.readString();
        this.deviceId = in.readString();
        this.ext = in.readString();
        this.idCard = in.readString();
        this.imageUrl = in.readString();
        this.libId = in.readString();
        this.libName = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.name = in.readString();
        this.recordId = in.readString();
        this.score = in.readInt();
        this.sex = in.readInt();
        this.taskId = in.readString();
        this.taskName = in.readString();
        this.userName = in.readString();
        this.taskReason = in.readString();
        this.note = in.readString();
        this.loginId = in.readString();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.target = in.readString();
        this.receiveUser = in.readString();
        this.sceneImg = in.readString();
        this.position = in.readString();
    }

    public static final Parcelable.Creator<AlarmDetailForIdResponseEntity> CREATOR = new Parcelable.Creator<AlarmDetailForIdResponseEntity>() {
        @Override
        public AlarmDetailForIdResponseEntity createFromParcel(Parcel source) {
            return new AlarmDetailForIdResponseEntity(source);
        }

        @Override
        public AlarmDetailForIdResponseEntity[] newArray(int size) {
            return new AlarmDetailForIdResponseEntity[size];
        }
    };
}
