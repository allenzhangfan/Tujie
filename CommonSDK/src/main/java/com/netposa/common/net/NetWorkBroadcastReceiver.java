package com.netposa.common.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.netposa.common.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

public class NetWorkBroadcastReceiver extends BroadcastReceiver {

    private NetWorkEventInterface mNetWorkEventInterface;
    private static final String INTERNET_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private static Map<Context, NetWorkBroadcastReceiver> receiverMap = new HashMap<>();

    public NetWorkBroadcastReceiver(NetWorkEventInterface netWorkEventInterface) {
        this.mNetWorkEventInterface = netWorkEventInterface;
    }

    public NetWorkBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkStatus = NetworkUtils.getNetWrokState();
            if (mNetWorkEventInterface != null) {
                mNetWorkEventInterface.onNetWorkChange(netWorkStatus);
            }
        }
    }

    /**
     * 网络监听注册广播
     *
     * @param context
     * @param netWorkEventInterface
     */
    public static void register(Context context, NetWorkEventInterface netWorkEventInterface) {
        if (receiverMap.containsKey(context)) {//已经注册
            return;
        }
        NetWorkBroadcastReceiver receiver = new NetWorkBroadcastReceiver(netWorkEventInterface);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTERNET_ACTION);
        context.registerReceiver(receiver, intentFilter);
        receiverMap.put(context, receiver);
    }

    /**
     * 注销广播
     *
     * @param context
     */
    public static void unregister(Context context) {
        NetWorkBroadcastReceiver receiver = receiverMap.remove(context);
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
