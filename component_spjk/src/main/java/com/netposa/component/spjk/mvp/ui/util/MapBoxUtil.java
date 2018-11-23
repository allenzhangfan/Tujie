package com.netposa.component.spjk.mvp.ui.util;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.netposa.common.log.Log;
import com.netposa.component.spjk.mvp.model.entity.WebMoctorPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：yeguoqiang
 * Created time：2018/11/12 10:14
 */
public class MapBoxUtil {

    private static final String TAG = MapBoxUtil.class.getSimpleName();

    private static final double POLE = 20037508.34;
    //默认是61个点位形成一公里范围圈
    private static final int BOUNDS_SIZE = 60;

    private static MapBoxUtil sInstance;

    private MapBoxUtil() {
    }

    public static MapBoxUtil getInstance() {
        if (sInstance == null) {
            sInstance = new MapBoxUtil();
        }
        return sInstance;
    }

    public List<Point> createCircleGeometry(LatLng center, int radius) {
        double centerLongitude = center.getLongitude();
        double centerLatitude = center.getLatitude();
        WebMoctorPoint pmCenter = webMoctorJW2PM(centerLongitude, centerLatitude);
        List<WebMoctorPoint> points = createRegularPolygon(pmCenter, radius, BOUNDS_SIZE);
        List<Point> newPoints = new ArrayList<>();
        Point mp0 = null;
        //形成一公里范围圈前提条件是起始点坐标和终点坐标必须是同一个
        for (int i = 0; i < points.size(); i++) {//循环59次
            Point mp = inverseMercator(points.get(i).getLon(), points.get(i).getLat());
            if (i == 0) {
                mp0 = mp;
            }
            newPoints.add(mp);
        }
        newPoints.add(mp0);
        return newPoints;
    }

    private WebMoctorPoint webMoctorJW2PM(double lon, double lat) {
        WebMoctorPoint pmCenter = new WebMoctorPoint();
        pmCenter.setLon((lon / 180.0) * POLE);
        if (lat > 85.05112) {
            lat = 85.05112;
        }
        if (lat < -85.05112) {
            lat = -85.05112;
        }
        lat = (Math.PI / 180.0) * lat;
        double tmp = Math.PI / 4.0 + lat / 2.0;
        pmCenter.setLat(POLE * Math.log(Math.tan(tmp)) / Math.PI);
        return pmCenter;
    }

    private List<WebMoctorPoint> createRegularPolygon(WebMoctorPoint pmCenter, int radius, int sides) {
        double angle = Math.PI * ((1 / sides) - (1 / 2));
        double rotatedAngle;
        List<WebMoctorPoint> points = new ArrayList<>();
        for (int i = 0; i < sides; i++) {
            WebMoctorPoint point = new WebMoctorPoint();
            rotatedAngle = angle + (i * 2 * Math.PI / sides);
            point.setLat(pmCenter.getLat() + (radius * Math.sin(rotatedAngle)));
            point.setLon(pmCenter.getLon() + (radius * Math.cos(rotatedAngle)));
            points.add(point);
        }
        return points;
    }

    /**
     * 墨卡托投影转换-》经纬度
     *
     * @param {number} lon 经度
     * @param {number} lat 纬度
     * @return {object} 经纬度坐标
     * @method inverseMercator
     */
    private Point inverseMercator(double lon, double lat) {
        lon = 180 * lon / POLE;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp((lat / POLE) * Math.PI)) - Math.PI / 2);
        return Point.fromLngLat(lon, lat);
    }
}
