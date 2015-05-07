/**
 * @Title MessageManager.java
 * @Package com.dkhs.portfolio.ui.messagecenter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-20 下午3:50:03
 * @version V1.0
 */
package com.dkhs.portfolio.ui.messagecenter;


import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;


/**
 * @ClassName MessageManager
 * @Description 消息中心/消息推送管理者
 * @author zjz
 * @date 2015-4-20 下午3:50:03
 * @version 1.0
 */
public final class MessageManager {

    private RongConnect mConnct;
    private boolean hasNewUnread;

    // private int mTotalUnreadCount;

    private static class SingleMessageManager {
        private static final MessageManager INSTANCE = new MessageManager();
    }

    private static final String TAG = "MessageManager";

    private MessageManager() {
        init();
    }

    private void init() {
        mConnct = new RongConnect();
    }

    public static final MessageManager getInstance() {
        return SingleMessageManager.INSTANCE;
    }

    /**
     * 通知有新的未读消息，需要更新主页 我的tab的小红点
     */
    public void notifyNewMessage() {
        // mTotalUnreadCount = RongConnect.
        BusProvider.getInstance().post(new NewMessageEvent(isHasNewUnread()));
    }

    public void connect() {
        mConnct.connect();
    }

    public void disConnect() {
        hasNewUnread = false;
        mConnct.disConnect();
    }

    public boolean isHasNewUnread() {
        return hasNewUnread;
    }

    public void setHasNewUnread(boolean hasNewUnread) {
        this.hasNewUnread = hasNewUnread;
        // BusProvider.getInstance().post(new NewMessageEvent());
        notifyNewMessage();
    }

    public int getTotalUnreadCount() {
        return mConnct.getUnReadCount();
    }

}
