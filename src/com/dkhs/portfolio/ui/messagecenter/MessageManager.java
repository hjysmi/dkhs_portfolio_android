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
public final class MessageManager {

    // 当前登录用户信息
    private UserEntity mUserEntity;

    private static class SingleMessageManager {
        private static final MessageManager INSTANCE = new MessageManager();
    }

    private static final String TAG = "MessageManager";

    private MessageManager() {
        init();
    }

    private void init() {
        // Log.i(TAG, "------- init() -------");
        // try {
        //
        // // 注册融云sdk
        // RongIM.init(PortfolioApplication.getInstance());
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public static final MessageManager getInstance() {
        return SingleMessageManager.INSTANCE;
    }

    /**
     * 通知有新的未读消息，需要更新主页 我的tab的小红点
     */
    public void notifyNewMessage() {
        BusProvider.getInstance().post(new NewMessageEvent());
    }

}
