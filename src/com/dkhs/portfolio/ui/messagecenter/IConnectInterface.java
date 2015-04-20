/**
 * @Title IConnectManager.java
 * @Package com.dkhs.portfolio.ui.messagecenter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-20 下午4:13:10
 * @version V1.0
 */
package com.dkhs.portfolio.ui.messagecenter;

/**
 * @ClassName IConnectManager
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-20 下午4:13:10
 * @version 1.0
 */
public interface IConnectInterface {
    public void connect();

    public boolean isConnecting();

    public void disConnect();
}
