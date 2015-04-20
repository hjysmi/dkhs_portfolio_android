/**
 * @Title MessageManager.java
 * @Package com.dkhs.portfolio.ui.messagecenter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-20 下午3:50:03
 * @version V1.0
 */
package com.dkhs.portfolio.ui.messagecenter;

/**
 * @ClassName MessageManager
 * @Description 消息中心/消息推送管理者
 * @author zjz
 * @date 2015-4-20 下午3:50:03
 * @version 1.0
 */
public class MessageManager {

    private static class SingleMessageManager {
        private static final MessageManager INSTANCE = new MessageManager();
    }

    private MessageManager() {
    }

    public static final MessageManager getInstance() {
        return SingleMessageManager.INSTANCE;
    }

}
