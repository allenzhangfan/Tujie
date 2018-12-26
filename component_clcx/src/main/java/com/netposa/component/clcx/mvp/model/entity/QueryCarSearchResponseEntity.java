package com.netposa.component.clcx.mvp.model.entity;

import androidx.annotation.Keep;

@Keep
public class QueryCarSearchResponseEntity {
    /**
     * recordId : bd2669f8-2b76-41a4-abfb-379fc5e037b3
     * deviceId : fxSJ000139MWIx1F
     * deviceName : null
     * nodeType : null
     * plateNumber : 京A71R19
     * plateColor : 2
     * sceneImg : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181109/10_0/8a3e893ed99d3344241e32fa87fda99e
     * absTime : 1541730590431
     * location : 1011.16.1552.959
     * longitude : null
     * latitude : null
     * source : 9
     */

    private String recordId;
    private String deviceId;
    private String deviceName;
    private String nodeType;
    private String plateNumber;
    private String plateColor;
    private String sceneImg;
    private long absTime;
    private String location;
    private String longitude;
    private String latitude;
    private int source;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public String getSceneImg() {
        return sceneImg;
    }

    public void setSceneImg(String sceneImg) {
        this.sceneImg = sceneImg;
    }

    public long getAbsTime() {
        return absTime;
    }

    public void setAbsTime(long absTime) {
        this.absTime = absTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
