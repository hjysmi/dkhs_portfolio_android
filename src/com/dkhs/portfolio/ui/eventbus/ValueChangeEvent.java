/**
 * @Title ValueChangeEvent.java
 * @Package com.dkhs.portfolio.ui.eventbus
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-1-16 下午2:06:27
 * @version V1.0
 */
package com.dkhs.portfolio.ui.eventbus;

/**
 * @ClassName ValueChangeEvent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-1-16 下午2:06:27
 * @version 1.0
 */
public class ValueChangeEvent {
    public float value;
    public String type;

    public ValueChangeEvent(String type, float value) {
        this.value = value;
        this.type = type;
    }
}
