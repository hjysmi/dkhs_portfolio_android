/**
 * @Title SurpusStock.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 上午11:52:46
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import android.graphics.Color;

/**
 * @ClassName SurpusStock
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-26 上午11:52:46
 * @version 1.0
 */
public class SurpusStock extends ConStockBean {

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param id
     * @param dutyValue
     * @param dutyColor
     * @param name
     * @param num
     */
    public SurpusStock(int id, int dutyValue, int dutyColor, String name, String num) {
        super(id, dutyValue, dutyColor, name, num);

    }

    public SurpusStock(int dutyValue) {
        super.stockName = "剩余资金占比";
        this.dutyColor = Color.RED;

        this.dutyValue = dutyValue;
    }

    boolean iSurplus;

    public boolean isiSurplus() {
        return iSurplus;
    }

    public void setiSurplus(boolean iSurplus) {
        this.iSurplus = iSurplus;
    }

}
