/**
 * @Title MessageReceive.java
 * @Package com.dkhs.portfolio.ui.messagecenter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-20 下午4:23:16
 * @version V1.0
 */
package com.dkhs.portfolio.ui.messagecenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @ClassName MessageReceive
 * @Description 接收推送消息广播监听
 * @author zjz
 * @date 2015-4-20 下午4:23:16
 * @version 1.0
 */
public class MessageReceive extends BroadcastReceiver {

    public static Intent getMessageIntent() {
        String intentfilter = "com.dkhs.portfolio.SENDMESSAGE";
        Intent intent = new Intent(intentfilter); // 对应setAction()
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MessageReceive", "收到消息---");
    }

}