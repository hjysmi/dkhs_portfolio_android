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
    // symbol: 股票ID(int)
    private long symbol;
    private int percent;

    public long getSymbol() {
        return symbol;
    }

    public void setSymbol(long symbol) {
        this.symbol = symbol;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
