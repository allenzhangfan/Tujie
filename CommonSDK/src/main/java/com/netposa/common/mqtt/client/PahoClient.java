package com.netposa.common.mqtt.client;

import android.os.ConditionVariable;

import com.netposa.common.mqtt.util.MqttHookPlugins;
import com.netposa.common.mqtt.util.MqttLoggerFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import androidx.annotation.NonNull;

/**
 * Created by yexiaokang on 2016/5/30.
 */
public class PahoClient extends AbstractClient {

    private static final String TAG = "PahoClient";

    private MqttAsyncClient mMqttClient;
    private MqttConnectOptions mOptions;
    private ExecutorService mService;

    private volatile boolean isInitialized = false;
    private volatile boolean isConnected = false;
    private volatile boolean cancelled = false;

    private final Lock mConnectingLock = new ReentrantLock();
    private final ConditionVariable mInitializedCV = new ConditionVariable(true);
    private final Logger mLogger = MqttLoggerFactory.getInstance().getLogger(TAG);

    public PahoClient() {
        mService = Executors.newCachedThreadPool();
    }

    @Override
    public void initMQTT(@NonNull MqttStateListener listener) {
        if (isInitialized) {
            isInitialized = true;
            listener.onSuccess();
            return;
        }
        // 将线程阻塞的操作提到创建线程之前，防止{@link #connectMQTT()}函数先执行
        cancelled = false;
        mInitializedCV.close();
        mService.submit(() -> {
            try {
                mMqttClient = new MqttAsyncClient(MqttHookPlugins.getServerUri(),
                        MqttHookPlugins.getClientId(), new MemoryPersistence());
                mOptions = new MqttConnectOptions();
                mOptions.setCleanSession(true);
                mOptions.setUserName(MqttHookPlugins.getUserName());
                mOptions.setPassword(MqttHookPlugins.getPassword().toCharArray());
                mOptions.setConnectionTimeout(10);
                mOptions.setKeepAliveInterval(20);
                mMqttClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        mLogger.log(Level.SEVERE, "connectionLost: ", cause);
                        cause.printStackTrace();
                        isInitialized = false;
                        isConnected = false;
                        listener.onConnectionLost(cause);
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        mLogger.info("messageArrived: " + topic);
                        notifyTopic(topic, message.getPayload());
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        try {
                            if (token.getMessage() != null) {
                                mLogger.info("deliveryComplete: " + token.getMessage().toString());
                            }
                        } catch (MqttException e) {
                            mLogger.log(Level.SEVERE, "deliveryComplete: ", e);
                            e.printStackTrace();
                        }
                    }
                });
                isInitialized = true;
                listener.onSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                isInitialized = false;
                listener.onFailure(e);
            } finally {
                mInitializedCV.open();
            }
        });
    }

    @Override
    public void connectMQTT(@NonNull MqttActionListener listener) {
        mService.submit(() -> {
            mInitializedCV.block();
            if (cancelled) {
                return;
            }
            if (!isInitialized) {
                listener.onFailure(new IllegalStateException("Not init mqtt"));
                return;
            }
            if (isConnected) {
                listener.onFailure(new IllegalStateException("already connected mqtt"));
                return;
            }
            if (mConnectingLock.tryLock()) {
                try {
                    mMqttClient.connect(mOptions).waitForCompletion();
                    isConnected = true;
                    listener.onSuccess();
                } catch (Exception e) {
                    isConnected = false;
                    isInitialized = false;
                    listener.onFailure(e);
                } finally {
                    mConnectingLock.unlock();
                }
            }
        });
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void subscribeTopic(@NonNull MqttActionListener listener, final String... topics) {
        mService.submit(() -> {
            for (String s : topics) {
                mLogger.info("subscribeTopic: topics = " + s);
            }
            int[] qos = new int[topics.length];
            for (int i = 0; i < qos.length; i++) {
                qos[i] = 1;
            }
            try {
                mMqttClient.subscribe(topics, qos);
                listener.onSuccess();
            } catch (Exception e) {
                listener.onFailure(e);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void unsubscribeTopic(@NonNull MqttActionListener listener, final String... topics) {
        mService.submit(() -> {
            try {
                mMqttClient.unsubscribe(topics);
                listener.onSuccess();
            } catch (Exception e) {
                listener.onFailure(e);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void publishTopic(final String topic, final byte[] payload, final int qos, final boolean retain) {
        mService.submit(() -> {
            try {
                mMqttClient.publish(topic, payload, qos, retain);
                mLogger.info("onSuccess: publishTopic");
            } catch (Exception e) {
                mLogger.log(Level.SEVERE, "onFailure: publishTopic", e);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void close() {
        cancelled = true;
        mInitializedCV.open();
        if (isConnected) {
            mService.submit(() -> {
                try {
                    mMqttClient.disconnect();
                    mLogger.info("onSuccess: disconnect");
                } catch (Exception e) {
                    mLogger.log(Level.SEVERE, "onFailure: disconnect", e);
                    e.printStackTrace();
                }
                try {
                    mMqttClient.close();
                    mLogger.info("onSuccess: close");
                } catch (Exception e) {
                    mLogger.log(Level.SEVERE, "onFailure: close", e);
                    e.printStackTrace();
                }
            });
        }
        isConnected = false;
        isInitialized = false;
    }
}
