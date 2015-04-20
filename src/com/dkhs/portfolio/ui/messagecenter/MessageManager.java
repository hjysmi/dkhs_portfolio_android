/**
 * @Title MessageManager.java
 * @Package com.dkhs.portfolio.ui.messagecenter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-20 下午3:50:03
 * @version V1.0
 */
package com.dkhs.portfolio.ui.messagecenter;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback.ErrorCode;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.RongTokenBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.LoginRegisterAcitvity;
import com.dkhs.portfolio.ui.NewMainActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.util.LogUtils;

import android.app.NotificationManager;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * @ClassName MessageManager
 * @Description 消息中心/消息推送管理者
 * @author zjz
 * @date 2015-4-20 下午3:50:03
 * @version 1.0
 */
public class MessageManager implements IConnectInterface {

    private static class SingleMessageManager {
        private static final MessageManager INSTANCE = new MessageManager();
    }

    private static final String TAG = "MessageManager";

    private MessageManager() {
        init();
    }

    private void init() {
        Log.i(TAG, "------- init() -------");
    }

    public static final MessageManager getInstance() {
        return SingleMessageManager.INSTANCE;
    }

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

}
