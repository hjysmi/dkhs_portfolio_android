/**
 * @Title MessageReceive.java
 * @Package com.dkhs.portfolio.ui.messagecenter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-20 下午4:23:16
 * @version V1.0
 */
package com.dkhs.portfolio.ui.messagecenter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.receiver.MessageNotificationClickReceiver;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.CommandNotificationMessage;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.ProfileNotificationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MessageReceive
 * @Description 接收推送消息广播监听
 * @date 2015-4-20 下午4:23:16
 */
public class MessageReceive extends BroadcastReceiver {


    public static final String TAG = "MessageReceive";

    public static final String KEY_MESSAGE = "message";

    public static Intent getMessageIntent(Message message) {
        MessageContent messageContent = message.getContent();
        String intentfilter = "com.dkhs.portfolio.SENDMESSAGE";
        Intent intent = new Intent(intentfilter); // 对应setAction()

        if (messageContent instanceof TextMessage) {// 文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());

        } else if (messageContent instanceof ImageMessage) {// 图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {// 语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {// 图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d(TAG, "onReceived-RichContentMessage:" + richContentMessage.getContent());
        }
        // else if (messageContent instanceof GroupInvitationNotification) {// 自定义群组消息
        // GroupInvitationNotification groupContentMessage = (GroupInvitationNotification) messageContent;
        // Log.d(TAG, "onReceived-GroupInvitationNotification:" + groupContentMessage.getMessage());
        // }
        else if (messageContent instanceof ContactNotificationMessage) {// 联系人（好友）操作通知消息
            ContactNotificationMessage contactMessage = (ContactNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-ContactNotificationMessage:" + contactMessage.getMessage());
        } else if (messageContent instanceof ProfileNotificationMessage) {// 资料变更通知消息
            ProfileNotificationMessage profileMessage = (ProfileNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-ProfileNotificationMessage:" + profileMessage.getExtra());
        } else if (messageContent instanceof CommandNotificationMessage) {// 命令通知消息
            CommandNotificationMessage commantMessage = (CommandNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-CommandNotificationMessage:" + commantMessage.getName());
        } else if (messageContent instanceof InformationNotificationMessage) {// 小灰条消息
            InformationNotificationMessage infoMessage = (InformationNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-GroupInvitationNotification:" + infoMessage.getMessage());
        } else if (messageContent instanceof DKImgTextMsg) {
            Log.d(TAG, "onReceived-其他消息，自己来判断处理");
            Log.d(TAG, "onReceived-message，" + message.getObjectName());
        }
        intent.putExtra(KEY_MESSAGE, message);
        return intent;
    }

    /**
     * 收到新消息后的处理逻辑
     */
    @Override
    public void onReceive(Context context, Intent intent) {
         Log.d("MessageReceive", "收到消息");
        //登陆状态处理消息,否则丢弃掉该消息
//        if (PortfolioApplication.hasUserLogin()) {
            MessageManager.getInstance().notifyNewMessage();
            MessageManager.getInstance().setHasNewUnread(true);
            Message message = intent.getParcelableExtra(KEY_MESSAGE);
            if (!PortfolioApplication.getInstance().isRunningForeground()) {
                if (null != message && "DK:ImgTextMsg".equals(message.getObjectName())) {
                    //推送通知
                    acquireWakeLock(context);
                    handDKImgTextMsg(context, message);
                }
            } else {
                //震动提醒
                acquireWakeLock(context);
                vibrationPhone();
            }
//        }
    }

    public void acquireWakeLock(Context ctx) {

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock m_wakeLockObj = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, TAG);


        m_wakeLockObj.acquire(1000 * 4);
        m_wakeLockObj.release();

    }

    /**
     * 震动手机提醒用户新信息进入
     */
    private void vibrationPhone() {
        Vibrator vibrator = (Vibrator) PortfolioApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{200l, 300l}, 1);
    }


    public void handDKImgTextMsg(Context context, Message msg) {

        DKImgTextMsg message = (DKImgTextMsg) msg.getContent();
        int notificationId = msg.getMessageId();
        String sendUserID = msg.getSenderUserId();

        //发送通知栏通知
        if (sendUserID != null) {
            Intent intent2 = new Intent(PortfolioApplication.getInstance(), MessageNotificationClickReceiver.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.setAction(MessageNotificationClickReceiver.MESSAGE_NOTIFICATION_CLICK);
            intent2.putExtra(KEY_MESSAGE, msg);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(PortfolioApplication.getInstance(), notificationId, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            String title = context.getResources().getString(R.string.app_name);
            Notification notificationCompat = new NotificationCompat.Builder(PortfolioApplication.getInstance()).setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(title).setContentText(message.getTitle()+" "+message.getContent()).setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent)
                    .build();
            notificationCompat.icon=R.drawable.ic_launcher;

            NotificationManager notificationManager = (NotificationManager) PortfolioApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, notificationCompat);
        }

    }

}
