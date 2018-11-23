package com.netposa.common.service.mqtt;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by yexiaokang on 2018/9/28.
 */
public interface PushService extends IProvider {

    void initPushService();

    void initMQTT();

    void activateMQTT();

    void closeMQTT();
}
