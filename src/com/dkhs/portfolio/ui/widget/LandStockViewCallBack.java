/**
 * @Title LandStockViewCallBack.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-30 下午1:10:29
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import com.dkhs.portfolio.bean.StockQuotesBean;

/**
 * @ClassName LandStockViewCallBack
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-3-30 下午1:10:29
 * @version 1.0
 */
public interface LandStockViewCallBack {
    public String getCheckValue();

    public int getStickType();

    public StockQuotesBean getStockQuotesBean();

    public void setCheckValue(String checkValue);

    public void setStickType(int stickValue);

    public int getTabPosition();

    public void setTabPosition(int position);

    // public void setChange(boolean isChange);
}
