package com.dkhs.portfolio.ui.widget.LinePoint;

/**
 * Created by zjz on 2015/6/11.
 * 非零费率基金
 */
public class DefFundPointEntity extends FundLinePointEntity {

    private float netvalue;

    public float getNet_cumulative() {
        return net_cumulative;
    }

    public void setNet_cumulative(float net_cumulative) {
        this.net_cumulative = net_cumulative;
    }

    private float net_cumulative;
    private float compareValue;

    private String info;

    public float getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(float netvalue) {
        this.netvalue = netvalue;
    }

    public float getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(float compareValue) {
        this.compareValue = compareValue;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String info) {
        this.info = info;
    }

}
