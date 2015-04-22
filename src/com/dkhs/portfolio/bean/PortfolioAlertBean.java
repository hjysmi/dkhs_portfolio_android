/**
 * @Title PortfolioAlertBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-14 下午7:03:36
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.io.Serializable;

/**
 * @ClassName PortfolioAlertBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-14 下午7:03:36
 * @version 1.0
 */
public class PortfolioAlertBean implements Serializable {
    private float portfolio_price_up;// 27.8 # 股价涨到提醒，0取消订阅
    private float portfolio_price_down;// ：27.8，#股价跌倒提醒,0取消订阅
    private float portfolio_percentage;// ：0.05， # 股价当日涨跌幅提醒，为NULL时和不传等效,0取消订阅
    private int portfolio_adjust_alert;// : 1 #组合换仓通知，1：订阅调仓通知，0：取消调仓通知

    public PortfolioAlertBean(float priceUp, float priceDown, float percent, boolean isAdjuest) {
        this.portfolio_price_up = priceUp;
        this.portfolio_price_down = priceDown;
        this.portfolio_percentage = percent;
        setAdjustAlert(isAdjuest);

    }

    public float getPortfolio_price_up() {
        return portfolio_price_up;
    }

    public void setPortfolio_price_up(float portfolio_price_up) {
        this.portfolio_price_up = portfolio_price_up;
    }

    public float getPortfolio_price_down() {
        return portfolio_price_down;
    }

    public void setPortfolio_price_down(float portfolio_price_down) {
        this.portfolio_price_down = portfolio_price_down;
    }

    public float getPortfolio_percentage() {
        return portfolio_percentage;
    }

    public void setPortfolio_percentage(float portfolio_percentage) {
        this.portfolio_percentage = portfolio_percentage;
    }

    public int getPortfolio_adjust_alert() {
        return portfolio_adjust_alert;
    }

    public void setPortfolio_adjust_alert(int portfolio_adjust_alert) {
        this.portfolio_adjust_alert = portfolio_adjust_alert;
    }

    public void setAdjustAlert(boolean isAdjust) {
        setPortfolio_adjust_alert(isAdjust ? 1 : 0);
    }

    public boolean isAdjustAlert() {
        return getPortfolio_adjust_alert() == 1 ? true : false;
    }
}
