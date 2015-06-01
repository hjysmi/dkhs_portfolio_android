/**
 * @Title TabStockTitleChangeEvent.java
 * @Package com.dkhs.portfolio.ui.eventbus
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-25 下午3:06:31
 * @version V1.0
 */
package com.dkhs.portfolio.ui.eventbus;

/**
 * @ClassName TabStockTitleChangeEvent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-25 下午3:06:31
 * @version 1.0
 */
public class TabFundsTitleChangeEvent {
    public String tabType;

    public TabFundsTitleChangeEvent(String type) {
        this.tabType = type;
    }
}
