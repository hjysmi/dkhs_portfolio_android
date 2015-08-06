package com.dkhs.portfolio.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.dkhs.portfolio.engine.AppUpdateCheckEngine;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.NetUtil;
import com.lidroid.xutils.util.LogUtils;


public class NetChangeReceiver extends BroadcastReceiver {


    public NetChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            if (NetUtil.checkNetWork()) {
                //fixme 这样的网络判断有缺陷: 当连接到无效的wifi时候 也会到这里来
                LogUtils.e("NetUtil 有网络连接");
                MessageManager.getInstance().connect();

            } else {
                //不做操作
                LogUtils.e("NetUtil 无网络连接");
            }
        }

    }

}
