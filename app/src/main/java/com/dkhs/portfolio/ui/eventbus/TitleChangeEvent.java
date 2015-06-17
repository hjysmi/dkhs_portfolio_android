/**
 * @Title TitleChangeEvent.java
 * @Package com.dkhs.portfolio.ui.eventbus
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-16 上午11:17:00
 * @version V1.0
 */
package com.dkhs.portfolio.ui.eventbus;

/**
 * @ClassName TitleChangeEvent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-3-16 上午11:17:00
 * @version 1.0
 */
public class TitleChangeEvent {

    public float netvalue;

    public TitleChangeEvent(float value) {
        this.netvalue = value;
    }
}
