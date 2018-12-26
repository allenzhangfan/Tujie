package com.netposa.common.entity.push;

import java.util.List;

/**
 * Author：yeguoqiang
 * Created time：2018/12/11 16:51
 */
public class AlarmCarDetailResponseForExt {

    /**
     * vehicleColor : B
     * monitorId : fxSJ000139MWIx1V
     * libId : 610b325e-addd-e02b-a648-9f2fcfcf25b0
     * plateType : 2
     * targetImages : ["PFSB:/viasbimg/bimg/data/20181211/15_0/eb0a30480ea1ccc0d03f628ba882436d"]
     * monitorName : baitian
     * passTime : 2018-12-11 16:54:12
     * libName : 车辆库03
     * alarmObjName : 闽AHA857
     * speed : 0.0
     * plateColor : 2
     * vehicleBrand : 68
     * imageURLs : http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181211/16_0/955d57bf628ba78c2d8bf7baf7b3d3ec
     * x : 114.4045808831109
     * y : 30.49291132658303
     * vehicleType : K94
     */

    private String vehicleColor;
    private String monitorId;
    private String libId;
    private String plateType;
    private String monitorName;
    private String passTime;
    private String libName;
    private String alarmObjName;
    private String speed;
    private String plateColor;
    private String vehicleBrand;
    private String imageURLs;
    private String x;
    private String y;
    private String vehicleType;
    private List<String> targetImages;

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getLibId() {
        return libId;
    }

    public void setLibId(String libId) {
        this.libId = libId;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getAlarmObjName() {
        return alarmObjName;
    }

    public void setAlarmObjName(String alarmObjName) {
        this.alarmObjName = alarmObjName;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(String imageURLs) {
        this.imageURLs = imageURLs;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<String> getTargetImages() {
        return targetImages;
    }

    public void setTargetImages(List<String> targetImages) {
        this.targetImages = targetImages;
    }
}
