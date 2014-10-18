/**
 * @Title FSLinePointEntity.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-17 下午3:53:57
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import com.dkhs.portfolio.utils.StringFromatUtils;

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
    private float avgPrice;
    private float increaseValue;
    private float increaseRange;
    private int turnover;

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

    public float getIncreaseValue() {
        return increaseValue;
    }

    public String getIncreaseValueDesc() {
        return "涨跌：" + StringFromatUtils.get2Point(increaseValue);
    }

    public void setIncreaseValue(float increaseValue) {
        this.increaseValue = increaseValue;
    }

    public float getIncreaseRange() {
        return increaseRange;
    }

    public String getIncreaseRangeDesc() {
        return "涨幅：" + StringFromatUtils.get2PointPercent(increaseRange);
    }

    public void setIncreaseRange(float increaseRange) {
        this.increaseRange = increaseRange;
    }

    public int getTurnover() {
        return turnover;
    }

    public String getTurnoverDesc() {
        return "成交量：" + StringFromatUtils.convertToWanHand(turnover);
    }

    public void setTurnover(int turnover) {
        this.turnover = turnover;
    }

    public float getAvgPrice() {
        return avgPrice;
    }

    public String getAvgPriceDesc() {
        return "均价：" + StringFromatUtils.get2Point(avgPrice);
    }

    public void setAvgPrice(float avgPrice) {
        this.avgPrice = avgPrice;
    }

}
