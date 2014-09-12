/**
 * @Title SubmitSymbol.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-12 下午1:14:05
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName SubmitSymbol
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-12 下午1:14:05
 * @version 1.0
 */
public class SubmitSymbol {
    // 股票名称
    private long symbol;
    private float percent;

    public long getSymbol() {
        return symbol;
    }

    public void setSymbol(long symbol) {
        this.symbol = symbol;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
