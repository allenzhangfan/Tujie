package com.netposa.component.mqtt.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netposa.common.constant.PushConstants;
import com.netposa.common.entity.push.AlarmMessage;
import com.netposa.common.entity.push.JqItemEntity;
import com.netposa.common.entity.push.NotificationEvent;
import com.netposa.common.log.Log;
import com.netposa.common.mqtt.MqttClient;
import com.netposa.common.mqtt.MqttClientFactory;
import com.netposa.common.mqtt.TransformUtils;
import com.netposa.common.mqtt.util.MqttHookPlugins;
import com.netposa.common.utils.SPUtils;
import com.netposa.component.mqtt.R;
import com.netposa.component.mqtt.event.MqttEvent;
import com.netposa.component.mqtt.utils.SaveUtils;
import com.netposa.component.room.DbHelper;
import com.netposa.component.room.entity.AlarmMessageEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.app.NotificationCompat;
import io.reactivex.schedulers.Schedulers;

import static com.netposa.common.constant.GlobalConstants.CONFIG_LAST_USER_LOGIN_ID;
import static com.netposa.common.constant.GlobalConstants.KEY_JQ_ITEM;

public class MqttPushService extends Service {

    private static final String TAG = "MqttPushService";
    private static final int MQTT_KEEP_ALIVE_INTERVAL = 5 * 60 * 1000;      // 5分钟检测一次

    private static final String CHANNEL_ID_MQTT = "channel_id_mqtt";
    private static final String CHANNEL_ID_ALARM = "channel_id_alarm";
    private static final String CHANNEL_NAME_MQTT = "channel_name_mqtt";
    private static final String CHANNEL_NAME_ALARM = "channel_name_alarm";


    private MqttClient mMqttClient;
    private MqttClient.TopicCallback mCallback = new TopicCallback();

    private final Gson mGson = new Gson();
    private final Type mAlarmListType = new TypeToken<AlarmMessage>() {
    }.getType();

    // 线程保活机制字段
    private volatile boolean mCheckMqttState = false;
    private volatile boolean mKeepAlive = false;
    private final ConditionVariable mKeepAliveCV = new ConditionVariable(false);
    private Runnable mKeepAliveRunnable = new KeepAliveRunnable();
    private ExecutorService mExecutorService = Executors.newCachedThreadPool();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        startForegroundIfNeeded();
        EventBus.getDefault().register(this);
        mMqttClient = MqttClientFactory.getInstance().getMqttClient(MqttClientFactory.PAHO);
        mMqttClient.addCallBack(mCallback);
        mKeepAlive = true;
        mKeepAliveCV.close();
        mExecutorService.submit(mKeepAliveRunnable);
    }

    private void startForegroundIfNeeded() {
        if (Build.VERSION.SDK_INT >= 26) {

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_MQTT,
                    CHANNEL_NAME_MQTT, NotificationManager.IMPORTANCE_NONE);
            if (manager != null) {
                manager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_MQTT);
            builder.setSmallIcon(R.mipmap.ic_logo)
                    .setContentTitle("消息推送服务器")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true);

            startForeground(1, builder.build());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onHandleEvent(MqttEvent event) {
        int eventType = event.getEventType();
        if (eventType == MqttEvent.TYPE_INIT) {
            if (mMqttClient.isConnected()) {
                mMqttClient.close();
            }
            scheduleInitMQTT();
        } else if (eventType == MqttEvent.TYPE_OVER) {
            // 关闭消息推送的时候停止对状态的监控
            mCheckMqttState = false;
            mMqttClient.close();
        } else if (eventType == MqttEvent.TYPE_ACTIVATE) {
            if (!mMqttClient.isConnected()) {
                mMqttClient.close();
                scheduleInitMQTT();
            }
        }

        // recycle
        event.recycle();
    }

    private void scheduleInitMQTT() {
        MqttHookPlugins.setClientId(MqttHookPlugins.getClientId(this));
        SaveUtils.getMqttInfo();
        mMqttClient.initMQTT(new MqttClient.MqttStateListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "initMQTT success");
                scheduleConnectMQTT();
            }

            @Override
            public void onFailure(Throwable tr) {
                Log.e(TAG, "initMQTT onFailure", tr);
            }

            @Override
            public void onConnectionLost(Throwable tr) {
                Log.e(TAG, "onConnectionLost", tr);
                try {
                    // 如果仅仅只连接了MQTT服务器，没有订阅主题，可能不需要做重连操作
                    // 真需要重连操作的话，取消下面这行的注释
//                    mCheckMqttState = true;
                    // 唤醒检测线程，进行重连操作
                    synchronized (mKeepAliveCV) {
                        mKeepAliveCV.notifyAll();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onConnectionLost KeepAlive", e);
                }
            }
        });
    }

    private void scheduleConnectMQTT() {
        mMqttClient.connectMQTT(new MqttClient.MqttActionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "connectMQTT success");
                scheduleSubscribeTopic();
            }

            @Override
            public void onFailure(Throwable tr) {
                Log.e(TAG, "connectMQTT onFailure", tr);
            }
        });
    }

    private void scheduleSubscribeTopic() {
        mMqttClient.subscribeTopic(new MqttClient.MqttActionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "subscribeTopic success");
                // 默认情况下，订阅主题成功之后才进行mqtt的状态监控
                mCheckMqttState = true;
            }

            @Override
            public void onFailure(Throwable tr) {
                Log.i(TAG, "subscribeTopic onFailure", tr);
            }
        }, PushConstants.TOPIC_ALARM);
    }

    @Override
    public void onDestroy() {
        mCheckMqttState = false;
        mKeepAlive = false;
        mKeepAliveCV.open();
        mMqttClient.removeCallBack(mCallback);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private class TopicCallback implements MqttClient.TopicCallback {

        @Override
        public void publish(String topicName, byte[] payload) {
            String content = new String(payload);
            Log.i(TAG, "publish: " + topicName + "/" + content);
            if (PushConstants.TOPIC_ALARM.equals(topicName)) {
                try {
                    AlarmMessage message = mGson.fromJson(content, mAlarmListType);
                    AlarmMessageEntity alarmMessageEntity = TransformUtils.copyToAlarmMessage(message, mGson);
                    String loginUserId = SPUtils.getInstance().getString(CONFIG_LAST_USER_LOGIN_ID);
                    if (loginUserId.equals(alarmMessageEntity.getReceiveUser())) {//只有对应的人才能接收此消息
                        DbHelper
                                .getInstance()
                                .insertAlarmMessage(alarmMessageEntity)
                                .subscribeOn(Schedulers.io())
                                .subscribe();
                        showAlarmNotification(message);
                        EventBus.getDefault().post(new NotificationEvent(NotificationEvent.TYPE_ALARM));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "fromJson AlarmList:" + e);
                    Log.e(TAG, "fromJson:" + content);
                }
            }
        }
    }

    private class KeepAliveRunnable implements Runnable {

        @Override
        public void run() {
            while (mKeepAlive) {
                if (mCheckMqttState && !mMqttClient.isConnected()) {
                    scheduleInitMQTT();
                }
                mKeepAliveCV.block(MQTT_KEEP_ALIVE_INTERVAL);
            }
        }
    }

    /**
     * https://blog.csdn.net/qq1073273116/article/details/83087120
     * https://blog.csdn.net/yinyignfenlei/article/details/78666325
     * Android点击通知栏消息，仅打开App，不跳转到具体Activity
     * 针对Android 7.0以上的设备可以采用如下方法强制不合并消息
     * Notification7.0取消自动分组
     */
    private void showAlarmNotification(AlarmMessage message) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        JqItemEntity jqItemEntity = TransformUtils.transform(message, mGson);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_ALARM,
                    CHANNEL_NAME_ALARM, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_ALARM);

        try {
            Class<?> alarmDetailsActivity = getClassLoader().loadClass("com.netposa.component.jq.mvp.ui.activity.AlarmDetailsActivity");
            Intent intent = new Intent(this, alarmDetailsActivity);
            intent.putExtra(KEY_JQ_ITEM, jqItemEntity.getId());
            int notifyId = (int) SystemClock.uptimeMillis();
            //消息内容按需添加
            builder.setSmallIcon(R.mipmap.ic_logo)
                    .setContentTitle(message.getTitle())
//                    .setContentText(message.getTitle())
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                    .setContentIntent(PendingIntent.getActivity(this, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setAutoCancel(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setGroupSummary(false)
                        .setGroup("group");
            }
            manager.notify(notifyId, builder.build());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
