/**
 * @Title SMSBroadcastReceiver.java
 * @Package com.dkhs.portfolio.service
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-31 下午1:55:30
 * @version V1.0
 */
package com.dkhs.portfolio.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dkhs.portfolio.ui.NewMainActivity;
import com.dkhs.portfolio.ui.messagecenter.MessageReceive;

import io.rong.imlib.model.Message;

/**
 * @author zjz
 * @version 1.0
 * @ClassName SMSBroadcastReceiver
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-31 下午1:55:30
 */
public class MessageNotificationClickReceiver extends BroadcastReceiver {

    public static final String MESSAGE_NOTIFICATION_CLICK = "com.dkhs.portfolio.NotificationClick";
    public MessageNotificationClickReceiver() {
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MESSAGE_NOTIFICATION_CLICK)) {

            Message message=intent.getParcelableExtra(MessageReceive.KEY_MESSAGE);
            Intent intent1 = new Intent(context, NewMainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra(MessageReceive.KEY_MESSAGE, message);
            context.startActivity(intent1);

        }

    }


}
