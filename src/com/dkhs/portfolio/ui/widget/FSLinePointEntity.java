/**
 * @Title FSLinePointEntity.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-17 下午3:53:57
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

/**
 * @ClassName FSLinePointEntity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-17 下午3:53:57
 * @version 1.0
 */
public class FSLinePointEntity extends LinePointEntity {
    private String time;
    private String price;
    private String increaseValue;
    private String increaseRange;
    private String turnover;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = "时间：" + time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = "价格：" + price;
    }

    public String getIncreaseValue() {
        return increaseValue;
    }

    public void setIncreaseValue(String increaseValue) {
        this.increaseValue = "涨跌：" + increaseValue;
    }

    public String getIncreaseRange() {
        return increaseRange;
    }

    public void setIncreaseRange(String increaseRange) {
        this.increaseRange = "涨幅：" + increaseRange;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = "成交量：" + turnover;
    }

}
