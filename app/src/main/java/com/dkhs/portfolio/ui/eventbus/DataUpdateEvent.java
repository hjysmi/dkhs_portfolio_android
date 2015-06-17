/**
 * @Title DataUpdateEvent.java
 * @Package com.dkhs.portfolio.ui.eventbus
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-9 上午11:04:16
 * @version V1.0
 */
package com.dkhs.portfolio.ui.eventbus;

/**
 * @ClassName DataUpdateEvent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-3-9 上午11:04:16
 * @version 1.0
 */
public class DataUpdateEvent {

    public boolean isEmpty;

    public DataUpdateEvent(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
}
