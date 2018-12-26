package com.netposa.component.mqtt;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.netposa.common.mqtt.MqttClient;
import com.netposa.common.mqtt.MqttClientFactory;
import com.netposa.common.mqtt.util.MqttHookPlugins;
import com.netposa.component.mqtt.utils.SaveUtils;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R2.id.am_mqtt_host_et)
    TextInputEditText mServerURIEditText;
    @BindView(R2.id.am_mqtt_client_id_et)
    TextInputEditText mClientIdEditText;
    @BindView(R2.id.am_mqtt_user_et)
    TextInputEditText mUserNameEditText;
    @BindView(R2.id.am_mqtt_pwd_et)
    TextInputEditText mPasswordEditText;

    private MqttClient mMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mqtt_activity_main);
        ButterKnife.bind(this);
        initViews();

        mMqttClient = MqttClientFactory.getInstance().getMqttClient(MqttClientFactory.PAHO);
    }

    private void initViews() {
        SaveUtils.getMqttInfo();
        mServerURIEditText.setText(MqttHookPlugins.getServerUri());
        mClientIdEditText.setText(MqttHookPlugins.getClientId());
        mUserNameEditText.setText(MqttHookPlugins.getUserName());
        mPasswordEditText.setText(MqttHookPlugins.getPassword());
    }

    @OnClick(R2.id.am_mqtt_connect_bt)
    void onClick(View view) {
        String serverURI = mServerURIEditText.getText().toString().trim();
        String clientId = mClientIdEditText.getText().toString().trim();
        String username = mUserNameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(serverURI)
                || TextUtils.isEmpty(clientId)
                || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(password)) {
            Snackbar.make(view, "信息不完善", Snackbar.LENGTH_SHORT).show();
            return;
        }
        MqttHookPlugins.setServerUri(serverURI);
        MqttHookPlugins.setClientId(clientId);
        MqttHookPlugins.setUserName(username);
        MqttHookPlugins.setPassword(password);

        mMqttClient.close();
        mMqttClient.initMQTT(new MqttClient.MqttStateListener() {
            @Override
            public void onSuccess() {
                mMqttClient.connectMQTT(new MqttClient.MqttActionListener() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "连接成功",
                                Toast.LENGTH_SHORT).show());
                        SaveUtils.saveMqttInfo();
                        startActivity(new Intent(MainActivity.this, MqttMessageActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable tr) {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "连接失败",
                                Toast.LENGTH_SHORT).show());
                        tr.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(Throwable tr) {

            }

            @Override
            public void onConnectionLost(Throwable tr) {

            }
        });
    }
}
