/**
 * @Title StockViewCallBack.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-26 下午7:15:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

public abstract interface StockViewCallBack {
    public abstract void landViewFadeOut();

    public abstract void setViewType(int paramInt);

    public abstract void stockMarkShow();

}
