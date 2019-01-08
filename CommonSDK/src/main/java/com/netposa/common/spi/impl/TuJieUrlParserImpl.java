package com.netposa.common.spi.impl;

import android.text.TextUtils;

import com.google.auto.service.AutoService;
import com.netposa.common.constant.GlobalConstants;
import com.netposa.common.log.Log;
import com.netposa.common.mqtt.util.MqttHookPlugins;
import com.netposa.common.spi.UrlParser;
import com.netposa.common.utils.SPUtils;

import androidx.annotation.Keep;

import static com.netposa.common.constant.GlobalConstants.IS_LOCAL_NET;
import static com.netposa.common.constant.GlobalConstants.OUTER_NET_IMAGE_IP;
import static com.netposa.common.constant.GlobalConstants.OUTER_NET_PLAY_IP;

/**
 * Created by yeguoqiang on 2018/9/28.
 */
@Keep
@AutoService(UrlParser.class)
public class TuJieUrlParserImpl implements UrlParser {

    private static final String TAG = TuJieUrlParserImpl.class.getSimpleName();

    //内网图片地址
    public static final String DEFAULT_LOCAL_IMAGE_URL = "172.16.90.168:6551";
    //内网视频播放地址
    public static final String DEFAULT_LOCAL_PLAY_IP_PORT = "192.168.101.31:554";

    private String mImagePrefix = "http://";
    private String mImageSign = "PFSB";
    private String mIpFilter = "/DownLoadFile";
    private String mImageSuffix = "/DownLoadFile?filename=";
    private String mMqttPrefix = "tcp://";

    public TuJieUrlParserImpl() {

    }

    /**
     * 内网
     * http://172.16.90.168:6551/DownLoadFile?filename=PFSB:/viasbimg/bimg/data/20181120/07_0/79d1c677009b270c7d0fcd2a8696ad9a
     */
    @Override
    public String onGetImageUrl(String originalUrl) {
        if (TextUtils.isEmpty(originalUrl)) {
            Log.e(TAG, "image url is null , please check !");
            return "";
        }
        boolean isLocal = SPUtils.getInstance().getBoolean(IS_LOCAL_NET);
        String outerIp = SPUtils.getInstance().getString(GlobalConstants.IMAGE_URL, OUTER_NET_IMAGE_IP);
        if (originalUrl.startsWith(mImageSign)) {//图片url以PFSB开头
            if (isLocal) {
                originalUrl = mImagePrefix.concat(DEFAULT_LOCAL_IMAGE_URL).concat(mImageSuffix).concat(originalUrl);
            } else {
                originalUrl = mImagePrefix.concat(outerIp).concat(mImageSuffix).concat(originalUrl);
            }
            return originalUrl;
        }
        if (isLocal) {
            originalUrl = mImagePrefix.concat(DEFAULT_LOCAL_IMAGE_URL).concat(mIpFilter).concat(originalUrl.split(mIpFilter)[1]);
        } else {
            originalUrl = mImagePrefix.concat(outerIp).concat(mIpFilter).concat(originalUrl.split(mIpFilter)[1]);
        }
        return originalUrl;
    }

    @Override
    public String onGetLocationImageUrlSuffix() {
        String result;
        boolean isLocal = SPUtils.getInstance().getBoolean(IS_LOCAL_NET);
        String outerIp = SPUtils.getInstance().getString(GlobalConstants.IMAGE_URL, OUTER_NET_IMAGE_IP);
        if (isLocal) {
            result = mImagePrefix.concat(DEFAULT_LOCAL_IMAGE_URL).concat(mImageSuffix);
        } else {
            result = mImagePrefix.concat(outerIp).concat(mImageSuffix);
        }
        return result;
    }

    /**
     * 替换Ip
     * 外网url案例(rtsp://58.49.28.186:58287/PVG/live/?PVG=172.16.90.151:2100/admin/admin/av/ONVIF/海康239)
     * 内网url案例(rtsp://192.168.101.31:554/PVG/live/?PVG=172.16.90.151:2100/admin/admin/av/ONVIF/海康239)
     *
     * @param originalUrl
     */
    @Override
    public String onGetPlayUrl(String originalUrl) {
        String[] strs = originalUrl.split("//");
        String[] ips = strs[1].split("/PVG");
        boolean isLocal = SPUtils.getInstance().getBoolean(IS_LOCAL_NET);
        String outerIp = SPUtils.getInstance().getString(GlobalConstants.PLAY_URL, OUTER_NET_PLAY_IP);
        if (!isLocal) {
            originalUrl = new StringBuilder()
                    .append(strs[0])
                    .append("//")
                    .append(outerIp)
                    .append("/PVG")
                    .append(ips[1])
                    .toString();
        }
        return originalUrl;
    }

    @Override
    public String onGetMqttUrl(String originalUrl) {
        boolean isLocal = SPUtils.getInstance().getBoolean(IS_LOCAL_NET);
        String mqttUrl;
        if (isLocal) {
            mqttUrl = MqttHookPlugins.getServerUri();
        } else {
            mqttUrl = mMqttPrefix.concat(originalUrl);
        }
        return mqttUrl;
    }
}
