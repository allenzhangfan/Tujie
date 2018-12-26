package com.netposa.component.mqtt;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.netposa.common.mqtt.MqttClient;
import com.netposa.common.mqtt.MqttClientFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MqttMessageActivity extends AppCompatActivity {

    private final static String FORMAT = "yyyy-MM-dd HH:mm:ss:SSS: ";
    private final static SimpleDateFormat SDF = new SimpleDateFormat(FORMAT, Locale.CHINA);

    @BindView(R2.id.amm_mqtt_topic_et)
    TextInputEditText mTopicEditText;
    @BindView(R2.id.amm_mqtt_payload_et)
    TextInputEditText mPayloadEditText;
    @BindView(R2.id.amm_mqtt_scrollView)
    ScrollView mScrollView;
    @BindView(R2.id.amm_mqtt_message_tv)
    TextView mMsgTextView;

    private MqttClient mMqttClient;
    private MqttClient.TopicCallback mTopicCallback = new MqttTopicCallback();
    private long firstTime = 0L;
    private String mTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mqtt_activity_mqtt_message);
        ButterKnife.bind(this);

        mMqttClient = MqttClientFactory.getInstance().getMqttClient(MqttClientFactory.PAHO);
        mMqttClient.addCallBack(mTopicCallback);
    }

    @OnClick({
            R2.id.amm_mqtt_publish_bt,
            R2.id.amm_mqtt_subscribe_bt
    })
    void onClick(View view) {
        String topic = mTopicEditText.getText().toString().trim();
        if (view.getId() == R.id.amm_mqtt_publish_bt) {
            String payload = mPayloadEditText.getText().toString().trim();

            if (TextUtils.isEmpty(topic) || TextUtils.isEmpty(payload)) {
                Snackbar.make(view, "发布信息不完善", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (mMqttClient.isConnected()) {
                mMqttClient.publishTopic(topic, payload.getBytes(), 2, false);
                Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "MQTT未连接，发布失败", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.amm_mqtt_subscribe_bt) {
            if (TextUtils.isEmpty(topic)) {
                Snackbar.make(view, "订阅信息不完善", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (mMqttClient.isConnected()) {
                mMqttClient.subscribeTopic(new MqttClient.MqttActionListener() {
                    @Override
                    public void onSuccess() {
                        mTopic = topic;
                        runOnUiThread(() -> Toast.makeText(MqttMessageActivity.this,
                                "订阅成功", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onFailure(Throwable tr) {
                        runOnUiThread(() -> Toast.makeText(MqttMessageActivity.this,
                                "订阅失败", Toast.LENGTH_SHORT).show());
                    }
                }, topic);
            } else {
                Toast.makeText(this, "MQTT未连接，订阅失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mMqttClient.isConnected() && !TextUtils.isEmpty(mTopic)) {
            mMqttClient.unsubscribeTopic(new MqttClient.MqttActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable tr) {

                }
            }, mTopic);
        }
        mMqttClient.removeCallBack(mTopicCallback);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            super.onBackPressed();
        }
    }

    private String getCurrentTime() {
        return SDF.format(new Date());
    }

    private class MqttTopicCallback implements MqttClient.TopicCallback {

        @Override
        public void publish(String topicName, byte[] payload) {
            String content = new String(payload);

            mScrollView.post(() -> mMsgTextView.append("\n" + getCurrentTime() + topicName + "/" + content));
            mScrollView.postDelayed(() -> mScrollView.fullScroll(ScrollView.FOCUS_DOWN), 200);
        }
    }
}
