package com.netposa.common.mqtt.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yexiaokang on 2018/9/4.
 */
public final class MqttHookPlugins {

    private static volatile String sServerUri = "tcp://192.168.101.10:1883";

    private static volatile String sClientId = "mqttId";

    private static volatile String sUserName = "admin";

    private static volatile String sPassword = "public";

    public static String getServerUri() {
        return sServerUri;
    }

    public static void setServerUri(String serverUri) {
        sServerUri = serverUri;
    }

    public static String getClientId() {
        return sClientId;
    }

    public static void setClientId(String clientId) {
        sClientId = clientId;
    }

    public static String getUserName() {
        return sUserName;
    }

    public static void setUserName(String userName) {
        sUserName = userName;
    }

    public static String getPassword() {
        return sPassword;
    }

    public static void setPassword(String password) {
        sPassword = password;
    }

    @SuppressWarnings({"ConstantConditions", "deprecation"})
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getClientId(Context context) {
        Context appContext = context.getApplicationContext();
        TelephonyManager telephonyManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            return telephonyManager.getDeviceId(TelephonyManager.PHONE_TYPE_CDMA);
        } else if (Build.VERSION.SDK_INT >= 21) {
            try {
                Method method = TelephonyManager.class.getDeclaredMethod("getDeviceId", int.class);
                method.setAccessible(true);
                return method.invoke(telephonyManager, TelephonyManager.PHONE_TYPE_CDMA).toString();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                return telephonyManager.getDeviceId();
            }
        } else {
            return telephonyManager.getDeviceId();
        }
    }
}
