/**
 * @Title RongConnect.java
 * @Package com.dkhs.portfolio.ui.messagecenter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-20 下午5:46:52
 * @version V1.0
 */
package com.dkhs.portfolio.ui.messagecenter;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.message.CommandNotificationMessage;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.ProfileNotificationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.RongTokenBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName RongConnect
 * @Description 依赖于融云API的连接实现，及连接状态的监听
 * @author zjz
 * @date 2015-4-20 下午5:46:52
 * @version 1.0
 */
public class RongConnect implements IConnectInterface, RongIM.ConnectionStatusListener {

    protected static final String TAG = "RongConnect";

    @Override
    public void connect() {
        UserEntity user = UserEngineImpl.getUserEntity();
        if (user != null && !TextUtils.isEmpty(user.getAccess_token())) {

            new UserEngineImpl().getToken(user.getId() + "", user.getUsername(), user.getAvatar_xs(),
                    new BasicHttpListener() {
                        @Override
                        public void onSuccess(String result) {
                            RongTokenBean rongTolenBean = (RongTokenBean) DataParse.parseObjectJson(
                                    RongTokenBean.class, result);
                            if (!TextUtils.isEmpty(rongTolenBean.getToken())) {
                                connectRongIM(rongTolenBean.getToken());
                            }
                        }
                    });
        }

    }

    /**
     * 连接融云服务器。
     * 
     * @param token
     */
    private void connectRongIM(String token) {

        try {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onSuccess(String s) {
                    // 此处处理连接成功。
                    LogUtils.d("Connect: Login successfully.");
                    /**
                     * 开启显示 下方 tab 选项'我的' 的小红点
                     */
                    BusProvider.getInstance().post(new NewMessageEvent());
                    RongIM.getInstance().setReceiveMessageListener(listener);

                }

                @Override
                public void onError(ErrorCode errorCode) {
                    // 此处处理连接错误。
                    LogUtils.d("Connect: Login failed.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final RongIM.OnReceiveMessageListener listener = new RongIM.OnReceiveMessageListener() {
        @Override
        public void onReceived(RongIMClient.Message message, int left) {
            // 输出消息类型。
            Log.d("Receive:---", "收到");

            RongIMClient.MessageContent messageContent = message.getContent();

            if (messageContent instanceof TextMessage) {// 文本消息
                TextMessage textMessage = (TextMessage) messageContent;
                Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
                Log.d(TAG, "onReceived-TextMessage:" + textMessage.getPushContent());
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
            } else {
                Log.d(TAG, "onReceived-其他消息，自己来判断处理");
            }

            /**
             * demo 代码 开发者需替换成自己的代码。
             */
            // Intent in = new Intent();
            // in.setAction(MainActivity.ACTION_DMEO_RECEIVE_MESSAGE);
            // in.putExtra("rongCloud", RongIM.getInstance().getTotalUnreadCount());
            // mContext.sendBroadcast(in);

            PortfolioApplication.getInstance().sendBroadcast(MessageReceive.getMessageIntent());
            // NewMainActivity.this.runOnUiThread(new Runnable() {
            // @Override
            // public void run() {
            // PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.S_APP_NEW_MESSAGE, true);
            // BusProvider.getInstance().post(new NewMessageEvent());
            // }
            // });
        }
    };

    @Override
    public boolean isConnecting() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void disConnect() {
        // 断开融云连接
        RongIM.getInstance().disconnect(false);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param arg0
     * @return
     */
    @Override
    public void onChanged(ConnectionStatus status) {
        Log.d(TAG, "onChanged:" + status);

    }

}
