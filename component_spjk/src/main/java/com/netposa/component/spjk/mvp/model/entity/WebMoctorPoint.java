package com.netposa.component.spjk.mvp.model.entity;

/**
 * Author：yeguoqiang
 * Created time：2018/11/12 16:38
 * 描述：根据中心点坐标值计算出能投影出一个圆圈的经纬度数组，再将这些数组还原成对应的地理经纬度值
 */

public class WebMoctorPoint {

    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
