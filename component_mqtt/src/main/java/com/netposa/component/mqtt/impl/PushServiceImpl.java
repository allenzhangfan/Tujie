package com.netposa.component.mqtt.impl;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.netposa.common.core.RouterHub;
import com.netposa.common.service.mqtt.PushService;
import com.netposa.component.mqtt.event.MqttEvent;
import com.netposa.component.mqtt.service.MqttPushService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yexiaokang on 2018/9/28.
 */

@Route(path = RouterHub.MQTT_PUSH_SERVICE, name = "消息推送服务")
public class PushServiceImpl implements PushService {

    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    @Override
    public void initPushService() {
        Intent service = new Intent(mContext, MqttPushService.class);
        mContext.startService(service);
    }

    @Override
    public void initMQTT() {
        EventBus.getDefault().post(MqttEvent.obtain(MqttEvent.TYPE_INIT));
    }

    @Override
    public void activateMQTT() {
        EventBus.getDefault().post(MqttEvent.obtain(MqttEvent.TYPE_ACTIVATE));
    }

    @Override
    public void closeMQTT() {
        EventBus.getDefault().post(MqttEvent.obtain(MqttEvent.TYPE_OVER));
    }
}
