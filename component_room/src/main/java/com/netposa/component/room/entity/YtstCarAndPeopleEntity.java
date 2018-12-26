package com.netposa.component.room.entity;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "YtstCarAndPeople")
public class YtstCarAndPeopleEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "gender")
    private int gender;
    @ColumnInfo(name = "traitImg")
    private String traitImg;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "source")
    private int source;
    @ColumnInfo(name = "deviceId")
    private String deviceId;

    @ColumnInfo(name = "deviceName")
    private String deviceName;
    @ColumnInfo(name = "traitLocation")
    private String traitLocation;
    @ColumnInfo(name = "smile")
    private int smile;
    @ColumnInfo(name = "recordId")
    private String recordId;
    @ColumnInfo(name = "sceneImg")
    private String sceneImg;

    @ColumnInfo(name = "eyeGlass")
    private int eyeGlass;
    @ColumnInfo(name = "startTime")
    private Long startTime;
    @ColumnInfo(name = "saveTime")
    private Long saveTime;
    @ColumnInfo(name = "pushTime")
    private Long pushTime;
    @ColumnInfo(name = "mask")
    private String mask;

    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "sunGlass")
    private int sunGlass;
    @ColumnInfo(name = "attractive")
    private String attractive;
    @ColumnInfo(name = "expression")
    private String expression;
    @ColumnInfo(name = "confidence")
    private int confidence;

    @ColumnInfo(name = "nodeType")
    private String nodeType;
    @ColumnInfo(name = "absTime")
    private long absTime;
    @ColumnInfo(name = "cameraType")
    private String cameraType;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "endTime")
    private long endTime;

    @ColumnInfo(name = "age")
    private int age;
    @ColumnInfo(name = "type1")
    private String type1;
    @ColumnInfo(name = "relation_record")
    private String relation_record;
    @ColumnInfo(name = "recordId1")
    private String recordId1;
    @ColumnInfo(name = "absTime1")
    private long absTime1;

    @ColumnInfo(name = "score")
    private int score;
    @ColumnInfo(name = "plateNumber")
    private String plateNumber;
    @ColumnInfo(name = "select")
    private boolean select;


    public YtstCarAndPeopleEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getTraitImg() {
        return traitImg;
    }

    public void setTraitImg(String traitImg) {
        this.traitImg = traitImg;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTraitLocation() {
        return traitLocation;
    }

    public void setTraitLocation(String traitLocation) {
        this.traitLocation = traitLocation;
    }

    public int getSmile() {
        return smile;
    }

    public void setSmile(int smile) {
        this.smile = smile;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getSceneImg() {
        return sceneImg;
    }

    public void setSceneImg(String sceneImg) {
        this.sceneImg = sceneImg;
    }

    public int getEyeGlass() {
        return eyeGlass;
    }

    public void setEyeGlass(int eyeGlass) {
        this.eyeGlass = eyeGlass;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Long saveTime) {
        this.saveTime = saveTime;
    }

    public Long getPushTime() {
        return pushTime;
    }

    public void setPushTime(Long pushTime) {
        this.pushTime = pushTime;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSunGlass() {
        return sunGlass;
    }

    public void setSunGlass(int sunGlass) {
        this.sunGlass = sunGlass;
    }

    public String getAttractive() {
        return attractive;
    }

    public void setAttractive(String attractive) {
        this.attractive = attractive;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public long getAbsTime() {
        return absTime;
    }

    public void setAbsTime(long absTime) {
        this.absTime = absTime;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getRelation_record() {
        return relation_record;
    }

    public void setRelation_record(String relation_record) {
        this.relation_record = relation_record;
    }

    public String getRecordId1() {
        return recordId1;
    }

    public void setRecordId1(String recordId1) {
        this.recordId1 = recordId1;
    }

    public long getAbsTime1() {
        return absTime1;
    }

    public void setAbsTime1(long absTime1) {
        this.absTime1 = absTime1;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
