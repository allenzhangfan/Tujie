package com.netposa.common.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Base64;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.netposa.common.constant.UrlConstant;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class TujieImageUtils {

    private static final String sImgUrl ="http://172.16.90.168:6551/DownLoadFile?filename=";

    /**
     * 获取车辆图片显示路径
     *
     * @param resUrl   车辆大图url
     *  basePath 图片根路径
     * @return sceneImg  --resUrl     location --> point
     */
    public static String getDisplayUrl(String resUrl,String location) {
        String destUrl = "";
        String basePath= UrlConstant.parseLocationImageUrlSuffix();
        if (TextUtils.isEmpty(basePath)){
            return destUrl;
        }
        if(!(resUrl.startsWith("http"))){
            resUrl= sImgUrl +resUrl;
        }
        if (TextUtils.isEmpty(location)){
            return destUrl;
        }
        final String[] split = location.split("\\.");
        final int[] coorFaces = {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])};
        try {
            String s = URLEncoder.encode(Base64.encodeToString(resUrl.getBytes(),Base64.NO_WRAP), "utf-8");//Base64.NO_WRAP 这个参数意思是略去所有的换行符
            Map<String, Integer> result = calculationPosition(coorFaces[0], coorFaces[1], coorFaces[2], coorFaces[3], 1.6);
            Integer tLeft = result.get("tLeft");
            Integer tTop = result.get("tTop");
            Integer tRight = result.get("tRight");
            Integer tBottom = result.get("tBottom");
            destUrl = basePath + s + "&left=" + tLeft + "&top=" + tTop + "&right=" + tRight + "&bottom=" + tBottom + "&ratio=2&encode=1";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destUrl;
    }


    public static String getPicUrl(String resUrl, String location) {
        String destUrl = "";
        String basePath= UrlConstant.parseLocationImageUrlSuffix();
        if (TextUtils.isEmpty(basePath)){
            return destUrl;
        }
        if(!(resUrl.startsWith("http"))){
            resUrl= sImgUrl +resUrl;
        }
        if (TextUtils.isEmpty(location)){
            return destUrl;
        }
        final String[] split = location.split(",");
        final int[] coorFaces = {Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])};
        try {
            String s = URLEncoder.encode(Base64.encodeToString(resUrl.getBytes(),Base64.NO_WRAP), "utf-8");
            Map<String, Integer> result = calculationPosition(coorFaces[0], coorFaces[1], coorFaces[2], coorFaces[3], 1.6);
            Integer tLeft = result.get("tLeft");
            Integer tTop = result.get("tTop");
            Integer tRight = result.get("tRight");
            Integer tBottom = result.get("tBottom");
            destUrl = basePath + s + "&left=" + tLeft + "&top=" + tTop + "&right=" + tRight + "&bottom=" + tBottom + "&ratio=2&encode=1";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destUrl;
    }

    /**
     * 计算图片截取参数
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param magnification 图片放大系数（默认1.6）
     * @return
     */
    public static Map<String, Integer> calculationPosition(int left, int top, int right, int bottom, double magnification) {
        if (magnification == 0) {
            magnification = 1.6;
        }
        Map<String, Integer> result = Maps.newHashMap();
        int width = right - left;
        int height = bottom - top;
        int sideSize = width > height ? width : height;
        int offsetSize = (int) Math.floor((sideSize * (magnification - 1)) / 2);
        int tLeft = left - offsetSize;
        int tTop = top - offsetSize;
        int tRight = left + sideSize + offsetSize;
        int tBottom = top + sideSize + offsetSize;
        if (tLeft < 0) {
            tLeft = 0;
            tRight = sideSize + offsetSize;
        }
        if (tTop < 0) {
            tTop = 0;
            tBottom = sideSize + offsetSize;
        }
        result.put("tLeft", tLeft);
        result.put("tRight", tRight);
        result.put("tBottom", tBottom);
        result.put("tTop", tTop);
        return result;
    }

    /**
     * 圈出目标
     * http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181220/15_0/f16aff757f6a3869c33004ff4e3a3f43
     *
     * @param resUrl
     * @return
     */
    @SuppressLint("NewApi")
    public static String circleTarget(String resUrl, List<String> location) {
        String destUrl = "";
        if(!(resUrl.startsWith("http"))){
            resUrl= sImgUrl +resUrl;
        }
        if (location == null || location.size() == 0) {
            return destUrl;
        }
        try {
            String encodePath = URLEncoder.encode(Base64.encodeToString(resUrl.getBytes(),Base64.NO_WRAP), "utf-8");
            List<Map<String, Object>> params = Lists.newArrayList();
            location.forEach(s -> {
                Map<String, Object> map = Maps.newHashMap();
                map.put("lin", 2);
                map.put("r", 255);
                map.put("g", 0);
                map.put("b", 0);
                map.put("pos", s);
                params.add(map);
            });
            Map<String, Object> pathParam = Maps.newHashMap();
            pathParam.put("rects", params);
            String s = Base64.encodeToString(new Gson().toJson(pathParam).getBytes(),Base64.NO_WRAP);
            destUrl = UrlConstant.parseLocationImageUrlSuffix() + encodePath + "&encode=1&osd=" + s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destUrl;
    }
}
