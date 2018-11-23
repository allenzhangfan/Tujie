package com.netposa.common.mqtt.client;

import com.netposa.common.mqtt.MqttClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yexiaokang on 2016/5/26.
 */
abstract class AbstractClient implements MqttClient {

    List<MqttClient.TopicCallback> mCallbacks = new ArrayList<>();

    @Override
    public void addCallBack(TopicCallback callback) {
        if (callback == null) {
            throw new NullPointerException("callback == null");
        }
        synchronized (this) {
            if (!mCallbacks.contains(callback)) {
                mCallbacks.add(callback);
            }
        }
    }

    @Override
    public void removeCallBack(TopicCallback callback) {
        if (callback == null) {
            throw new NullPointerException("callback == null");
        }
        synchronized (this) {
            mCallbacks.remove(callback);
        }
    }

    protected final void notifyTopic(String topicName, byte[] payload) {
        synchronized (this) {
            for (TopicCallback callback : mCallbacks) {
                callback.publish(topicName, payload);
            }
        }
    }
}
