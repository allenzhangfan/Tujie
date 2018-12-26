package com.netposa.common.mqtt;

import com.netposa.common.log.Log;
import com.netposa.common.mqtt.client.PahoClient;
import java.util.HashMap;

/**
 * Created by yexiaokang on 2016/5/26.
 */
public class MqttClientFactory {

    private static final String TAG = "MqttClientFactory";

    public static final String PAHO = "paho";

    private HashMap<String, MqttClient> mMap = new HashMap<>();

    private volatile static MqttClientFactory sFactory;

    private MqttClientFactory() {
    }

    public static MqttClientFactory getInstance() {
        if (sFactory == null) {
            synchronized (MqttClientFactory.class) {
                if (sFactory == null) {
                    sFactory = new MqttClientFactory();
                }
            }
        }
        return sFactory;
    }

    public void clear() {
        mMap.clear();
    }

    public MqttClient getMqttClient(String clientType) {
        if (mMap.containsKey(clientType)) {
            return mMap.get(clientType);
        } else {
            MqttClient client = null;
            switch (clientType) {
                case PAHO:
                    client = new PahoClient();
                    break;
                default:
                    Log.e(TAG, "getMqttClient: clientType = " + clientType);
                    break;
            }
            if (client == null) {
                throw new RuntimeException("clientType is not correct");
            }
            mMap.put(clientType, client);
            return client;
        }
    }
}
