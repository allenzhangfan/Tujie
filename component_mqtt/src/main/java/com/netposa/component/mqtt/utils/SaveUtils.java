package com.netposa.component.mqtt.utils;

import com.netposa.common.constant.MqttConstants;
import com.netposa.common.mqtt.util.MqttHookPlugins;
import com.netposa.common.utils.SPUtils;

/**
 * Created by yexiaokang on 2018/9/4.
 */
public class SaveUtils {


    public static void saveMqttInfo() {
        SPUtils.getInstance().put(MqttConstants.MQTT_SERVER_URI, MqttHookPlugins.getServerUri());
        /*SPUtils.getInstance().put(MqttConstants.MQTT_CLIENT_ID, MqttHookPlugins.getClientId());*/
        SPUtils.getInstance().put(MqttConstants.MQTT_USER_NAME, MqttHookPlugins.getUserName());
        SPUtils.getInstance().put(MqttConstants.MQTT_PASSWORD, MqttHookPlugins.getPassword());
    }

    public static void getMqttInfo() {
        String serverURI = SPUtils.getInstance().getString(MqttConstants.MQTT_SERVER_URI,
                MqttHookPlugins.getServerUri());
        MqttHookPlugins.setServerUri(serverURI);

        /*String clientId = SPUtils.getInstance().getString(MqttConstants.MQTT_CLIENT_ID,
                MqttHookPlugins.getClientId());
        MqttHookPlugins.setClientId(clientId);*/

        String userName = SPUtils.getInstance().getString(MqttConstants.MQTT_USER_NAME,
                MqttHookPlugins.getUserName());
        MqttHookPlugins.setUserName(userName);

        String password = SPUtils.getInstance().getString(MqttConstants.MQTT_PASSWORD,
                MqttHookPlugins.getPassword());
        MqttHookPlugins.setPassword(password);
    }
}
