/**
 * @Title RongConnect.java
 * @Package com.dkhs.portfolio.ui.messagecenter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-20 下午5:46:52
 * @version V1.0
 */
package com.dkhs.portfolio.ui.messagecenter;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.RongTokenBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RongConnectSuccessEvent;
import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIMClientWrapper;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @author zjz
 * @version 1.0
 * @ClassName RongConnect
 * @Description 依赖于融云API的连接实现，及连接状态的监听
 * @date 2015-4-20 下午5:46:52
 */
public class RongConnect implements IConnectInterface, ConnectionStatusListener {

    protected static final String TAG = "RongConnect";
    private UserEngineImpl userEngine;
    private boolean isConnect = false;

    // 当获取用户数据后，就开始初始化融云连接
    // 注销后，需要断开连接，当监听到断开连接后，才正确退出。
    //
    // IMKit SDK调用第一步 初始化
    // context上下文
    // RongIM.init(this);
    // IMKit SDK调用第二步
    // 建立与服务器的连接 rong connect
    public RongConnect() {
        init();

    }


    public Map<String, UserInfo> userInfoList = new HashMap<String, UserInfo>();


    public void add(UserInfo userInfo) {


        if (!userInfoList.containsKey(userInfo.getUserId())) {
            userInfoList.put(userInfo.getUserId(), userInfo);
        }
    }

    public UserInfo get(String key) {
        return userInfoList.get(key);
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(final String arg0) {

                return RongConnect.this.getUserInfo(arg0);
            }


        }, true);//设置用户信息提供者。

        // RongIM.setGetFriendsProvider(this);//设置好友信息提供者.
        // RongIM.setGetGroupInfoProvider(this);//设置群组信息提供者。
        // RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        // RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
    }

    /*
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {
        setOnReceiveMessageListener();
    }

    public void setOnReceiveMessageListener() {
        //开启免打扰模式
        RongIM.getInstance().getRongIMClient().setNotificationQuietHours("00:00:00", 1439, new RongIMClient.OperationCallback() {

            @Override
            public void onSuccess() {
                Log.e(TAG, "----yb----设置会话通知周期-onSuccess");
                isConnect = true;
                RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(new OnReceiveMessageListener());
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "----yb----设置会话通知周期-oonError:" + errorCode.getValue());
            }
        });
    }

    private void init() {
        Log.i(TAG, "------- init() -------");
        userEngine = new UserEngineImpl();
        try {
            // IMKit SDK调用第一步 初始化
            // context上下文
            RongIM.init(PortfolioApplication.getInstance());
            RongIM.registerMessageTemplate(new MessageProvider());
            RongIM.registerMessageTemplate(new DKImgTextMsgProvider());
            RongIM.registerMessageType(DKImgTextMsg.class);
            initDefaultListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect() {
//        if (PortfolioApplication.)PortfolioApplication
        if (!isConnect) {
            UserEntity user = UserEngineImpl.getUserEntity();

            if(user ==null ||user.getUsername()== null ){
                Log.e(TAG, "----user ==null ||user.getUsername()== null ----");
                return;
            }

            // 先向服务器请求用户连接融云的token，取得token后再去连接融云的服务器。
            new UserEngineImpl().getToken(user.getId() + "", user.getUsername(), user.getAvatar_xs(),
                    new BasicHttpListener() {
                        @Override
                        public void onSuccess(String result) {
                            RongTokenBean rongTolenBean = (RongTokenBean) DataParse.parseObjectJson(RongTokenBean.class,
                                    result);
                            if (null!=rongTolenBean&&!TextUtils.isEmpty(rongTolenBean.getToken())) {
                                connectRongIM(rongTolenBean.getToken());
                            }
                        }
                    });
        }

    }

    public boolean isValid() {
        if (!isConnect) {
            connect();
        }
        return null != RongIM.getInstance() && isConnect;
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
                public void onTokenIncorrect() {

                }

                @Override
                public void onError(ErrorCode errorCode) {
                    // 此处处理连接错误。
                    LogUtils.e("Connect: Login failed.");
                }

                @Override
                public void onSuccess(String arg0) {
                    Log.e(TAG, "--------连接融云服务器-onSuccess:");
                    setOtherListener();
                    int unreadCount = getUnReadCount();
                    if (unreadCount > 0) {
                        MessageManager.getInstance().setHasNewUnread(true);
                    }
                    BusProvider.getInstance().post(new RongConnectSuccessEvent());

                }


            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class OnReceiveMessageListener implements io.rong.imlib.RongIMClient.OnReceiveMessageListener {

        @Override
        public boolean onReceived(Message message, int arg1) {


            PortfolioApplication.getInstance().sendBroadcast(MessageReceive.getMessageIntent(message));

            return false;
        }


    }


    public int getUnReadCount() {
        int unreadCount = 0;
        RongIMClientWrapper clientWrapper = RongIM.getInstance().getRongIMClient();

        if (null != clientWrapper) {
            unreadCount = clientWrapper.getUnreadCount(ConversationType.PRIVATE);
        }
        return unreadCount;
    }

    public void startPrivateChat(Context context, String id, String name) {

        if (TextUtils.isEmpty(name)) {
            name = "";
        }
        if (isValid()) {
            RongIM.getInstance().startPrivateChat(context, id, name);
        }
    }

    public void startConversationList(Context context) {
        cancelAllNotification(context);
        RongIM.getInstance().startConversationList(context);
    }


    @Override
    public boolean isConnecting() {
        if (RongIM.getInstance() == null) {
            return false;
        }
        // 连接状态可能是UNKNOWN
        // public RongIM.ConnectionStatusListener.ConnectionStatus RongIMgetCurrentConnectionStatus()
        ConnectionStatusListener.ConnectionStatus connectStatus = RongIMClient.getInstance()
                .getCurrentConnectionStatus();

        return connectStatus == ConnectionStatus.CONNECTED;
    }

    @Override
    public void disConnect(Context context) {
        try {
            isConnect = false;
            cancelAllNotification(context);
            if (RongIM.getInstance() != null) {
                // 断开融云连接
                RongIM.getInstance().disconnect(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param arg0
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onChanged(ConnectionStatus status) {
        Log.d(TAG, "onChanged:" + status);
    }

    public UserInfo getUserInfo(String userId) {
        final UserInfo userInfo = new UserInfo(userId, "", null);

        if (get(userId) == null) {
            userEngine.getUserInfo(userId, new BasicHttpListener() {
                        @Override
                        public void onSuccess(String result) {
                            UserEntity userEntity = DataParse.parseObjectJson(UserEntity.class, result);
                            if(null==userEntity){
                                return;
                            }
                            userInfo.setName(userEntity.getUsername());
                            userInfo.setPortraitUri(Uri.parse(userEntity.getAvatar_md()));
                            add(userInfo);
                        }

                        @Override
                        public void onFailure(int errCode, String errMsg) {
                            super.onFailure(errCode, errMsg);
                        }
                    }
            );
        } else {
            UserInfo item = get(userId);
            userInfo.setPortraitUri(item.getPortraitUri());
            userInfo.setName(item.getName());
        }

        return userInfo;
    }

    private void cancelAllNotification(Context context) {
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.cancelAll();
    }

}
