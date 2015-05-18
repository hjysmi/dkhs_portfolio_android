package com.dkhs.portfolio.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.dkhs.portfolio.ui.NewMainActivity;
import com.dkhs.portfolio.ui.messagecenter.MessageReceive;

import io.rong.imkit.RongIM;
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
    public static final String ACTION_CHAT = "com.dkhs.portfolio.action_chat";
    public static final String ACTION_CHAT_LIST = "com.dkhs.portfolio.action_chat_list";
    public static final String KEY_ACTION = "action";
    public static final String KEY_SEND_USER_ID = "send_user_id";
    public static final String KEY_SEND_USER_NAME = "send_user_name";
    public MessageNotificationClickReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MESSAGE_NOTIFICATION_CLICK)) {
            Message message=intent.getParcelableExtra(MessageReceive.KEY_MESSAGE);
            String id = message.getSenderUserId();
            String name="";
            if(null !=  message.getContent().getUserInfo()) {
                name = message.getContent().getUserInfo().getName();
            }
            Intent intent1 = new Intent(context, NewMainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra(KEY_SEND_USER_ID, id);
            intent1.putExtra(KEY_SEND_USER_NAME, name);
            intent1.putExtra(KEY_ACTION, ACTION_CHAT_LIST);
            context.startActivity(intent1);
        }

    }


}
