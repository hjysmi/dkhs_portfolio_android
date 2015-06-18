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
 * @author zjz
 * @version 1.0
 * @ClassName SubmitSymbol
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-12 下午1:14:05
 */
public class SubmitSymbol {
    // symbol: 股票ID(int)
    private String symbol;
    private int percent;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
