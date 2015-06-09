/**
 * @Title CompareFundsBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-14 上午10:35:14
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName CompareFundsBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-14 上午10:35:14
 * @version 1.0
 */
public class CompareFundsBean {
    /**
     * "pk": 106000082,
     * "symbol": "SH000001",
     * "end": 2363.87,
     * "begin": 2235.511,
     * "chartlist": [
     * {
     * "netvalue": 2235.511,
     * "date": "2014-09-01",
     * "percentage": 0
     * },
     * */

    @SerializedName("pk")
    private String fundsId;
    private String symbol;
    private float end;
    private float begin;
    private List<ComparePoint> chartlist;

    public class ComparePoint {
        private float netvalue;
        private String date;

        private float percentage;

        // public float getNetvalue() {
        // return netvalue;
        // }
        //
        // public void setNetvalue(float netvalue) {
        // this.netvalue = netvalue;
        // }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public float getNetvalue() {
            return netvalue;
        }

        public void setNetvalue(float netvalue) {
            this.netvalue = netvalue;
        }
    }

    public String getFundsId() {
        return fundsId;
    }

    public void setFundsId(String fundsId) {
        this.fundsId = fundsId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public List<ComparePoint> getChartlist() {
        return chartlist;
    }

    public void setChartlist(List<ComparePoint> chartlist) {
        this.chartlist = chartlist;
    }

    public float getBegin() {
        return begin;
    }

    public void setBegin(float begin) {
        this.begin = begin;
    }

}
