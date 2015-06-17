/**
 * @Title LandStockViewCallBack.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-30 下午1:10:29
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import java.util.List;

import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;

/**
 * @ClassName LandStockViewCallBack
 * @Description 横/竖屏切换，显示UI变化回调
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
