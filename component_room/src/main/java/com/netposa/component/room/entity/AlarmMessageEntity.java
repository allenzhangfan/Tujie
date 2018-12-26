package com.netposa.component.room.entity;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by yexiaokang on 2018/10/8.
 */
@Entity(tableName = "AlarmMessage")
public class AlarmMessageEntity {

    /**
     * absTime : 1544431032072
     * alarmAddress : 大门36
     * alarmLevel : 1
     * alarmStatus : 2
     * alarmTime : 1544431032321
     * alarmType : 2
     * deviceId : fxSJ000139MWIx1M
     * endTime : 2017038914000
     * ext : [{"birthday":"2018-12-10","libId":"521726253426802688","nation":"汉族","sex":"0","libName":"测试库liu","remark":"户籍地址：","targetImage":"PFSB:/viasbimg/bimg/data/20181210/16_0/35faaf9c8e18e302cb2b27cc24ae5306","score":97,"createTime":"2018-12-10 16:35:02","idcard":"412124190001017873","census":"fdgsdgsgsd户籍地址：","name":"lm","personId":"521726550748430336"}]
     * id : 3e75c447-bfd0-419e-9d97-3666189c7fe2
     * idCard : 412124190001017873
     * imageUrl : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181210/16_0/d3e333446003e19048777d9d44807ff4
     * latitude : null
     * libId : 521726253426802688
     * libName : 测试库liu
     * longitude : null
     * name : lm
     * note : ssdfafaf
     * receiveUser : af2005b8-59df-4a71-a231-e0e6d232b60c
     * recordId : 9340a841-2b29-422b-9d5f-08bb37818be1
     * score : 97
     * sex : null
     * startTime : 1544430914000
     * target : 测试库liu
     * targetImage : null
     * taskId : d34a5ba6-dbe1-4068-bac3-c95a23aee2fc
     * taskName : ceshi
     * taskReason : null
     * userName : 管理员
     */

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "absTime")
    private long absTime;
    @ColumnInfo(name = "alarmAddress")
    private String alarmAddress;
    @ColumnInfo(name = "alarmLevel")
    private String alarmLevel;
    @ColumnInfo(name = "alarmStatus")
    private String alarmStatus;
    @ColumnInfo(name = "alarmTime")
    private long alarmTime;
    @ColumnInfo(name = "alarmType")
    private String alarmType;
    @ColumnInfo(name = "deviceId")
    private String deviceId;
    @ColumnInfo(name = "endTime")
    private long endTime;
    @ColumnInfo(name = "ext")
    private String ext;
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "idCard")
    private String idCard;
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "libId")
    private String libId;
    @ColumnInfo(name = "libName")
    private String libName;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "note")
    private String note;
    @ColumnInfo(name = "receiveUser")
    private String receiveUser;
    @ColumnInfo(name = "recordId")
    private String recordId;
    @ColumnInfo(name = "score")
    private int score;
    @ColumnInfo(name = "sex")
    private String sex;
    @ColumnInfo(name = "startTime")
    private long startTime;
    @ColumnInfo(name = "target")
    private String target;
    @ColumnInfo(name = "targetImage")
    private String targetImage;
    @ColumnInfo(name = "taskId")
    private String taskId;
    @ColumnInfo(name = "taskName")
    private String taskName;
    @ColumnInfo(name = "taskReason")
    private String taskReason;
    @ColumnInfo(name = "userName")
    private String userName;

    public AlarmMessageEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(String targetImage) {
        this.targetImage = targetImage;
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

    public String getTaskReason() {
        return taskReason;
    }

    public void setTaskReason(String taskReason) {
        this.taskReason = taskReason;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
