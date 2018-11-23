package com.netposa.common.mqtt;

import androidx.annotation.NonNull;

/**
 * Created by yexiaokang on 2016/5/26.
 */
public interface MqttClient {

    /**
     * 初始化MQTT
     */
    void initMQTT(@NonNull MqttStateListener listener);

    /**
     * 连接MQTT
     */
    void connectMQTT(@NonNull MqttActionListener listener);

    /**
     * 是否已连接
     *
     * @return {@code true}为已连接，否则为{@code false}
     */
    boolean isConnected();

    /**
     * 订阅话题
     *
     * @param topics 话题名
     */
    void subscribeTopic(@NonNull MqttActionListener listener, String... topics);

    /**
     * 取消订阅话题
     *
     * @param topics 话题名
     */
    void unsubscribeTopic(@NonNull MqttActionListener listener, String... topics);

    /**
     * 发布一个话题
     *
     * @param topic   话题标题
     * @param payload 话题内容
     * @param qos     QOS
     * @param retain  retain
     */
    void publishTopic(String topic, byte[] payload, int qos, boolean retain);

    /**
     * 添加话题监听接口
     *
     * @param callback 接口
     */
    void addCallBack(TopicCallback callback);

    /**
     * 移除话题监听接口
     *
     * @param callback 接口
     */
    void removeCallBack(TopicCallback callback);

    /**
     * 关闭所有连接
     */
    void close();

    /**
     * 话题监听接口
     */
    interface TopicCallback {

        /**
         * 发布话题
         *
         * @param topicName 话题标题
         * @param payload   话题内容
         */
        void publish(String topicName, byte[] payload);
    }

    interface MqttActionListener {

        void onSuccess();

        void onFailure(Throwable tr);
    }

    interface MqttStateListener extends MqttActionListener {

        void onConnectionLost(Throwable tr);
    }
}
