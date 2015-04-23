/**
 * @Title TodayNetValue.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-23 上午11:29:20
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * @ClassName TodayNetValue
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-23 上午11:29:20
 * @version 1.0
 */
public class TodayNetValue {
    private float begin;
    private float last_netvalue;
    private float end;
    private List<TodayNetBean> chartlist;
    private float maxOffetValue;
    private int trade_status;

    public int getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(int trade_status) {
        this.trade_status = trade_status;
    }

    public float getBegin() {
        return begin;
    }

    public void setBegin(float begin) {
        this.begin = begin;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public List<TodayNetBean> getChartlist() {
        return chartlist;
    }

    public void setChartlist(List<TodayNetBean> chartlist) {
        this.chartlist = chartlist;
    }

    public float getMaxOffetValue() {
        return maxOffetValue;
    }

    public void setMaxOffetValue(float maxOffetValue) {
        this.maxOffetValue = maxOffetValue;
    }

    public float getLast_netvalue() {
        return last_netvalue;
    }

    public void setLast_netvalue(float last_netvalue) {
        this.last_netvalue = last_netvalue;
    }

}
