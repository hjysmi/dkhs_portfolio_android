/**
 * @Title PositionDetail.java
 * @Package com.dkhs.portfolio.bean
 * @Description 持仓明细
 * @author zjz
 * @date 2014-9-15 上午10:48:51
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName PositionDetail
 * @Description 持仓明细
 * @author zjz
 * @date 2014-9-15 上午10:48:51
 * @version 1.0
 */
public class PositionDetail implements Serializable {

    private static final long serialVersionUID = 4124672683521681615L;
    private CombinationBean portfolio;
    @SerializedName("current_date")
    private String currentDate;
    @SerializedName("positions_detail")
    private List<PositionAdjustBean> adjustList;
    @SerializedName("positions")
    private List<ConStockBean> positionList;
    private float fund_percent;

    public class PositionAdjustBean extends StockBean {

        private static final long serialVersionUID = 4874874844544L;
        @SerializedName("from_percent")
        private float fromPercent;
        @SerializedName("to_percent")
        private float toPercent;
        @SerializedName("created_at")
        private String modifyTime;

        public float getFromPercent() {
            return fromPercent;
        }

        public void setFromPercent(float fromPercent) {
            this.fromPercent = fromPercent;
        }

        public float getToPercent() {
            return toPercent;
        }

        public void setToPercent(float toPercent) {
            this.toPercent = toPercent;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }
    }

    public CombinationBean getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(CombinationBean portfolio) {
        this.portfolio = portfolio;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public List<PositionAdjustBean> getAdjustList() {
        return adjustList;
    }

    public void setAdjustList(List<PositionAdjustBean> adjustList) {
        this.adjustList = adjustList;
    }

    public List<ConStockBean> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<ConStockBean> positionList) {
        this.positionList = positionList;
    }

    public float getFund_percent() {
        return fund_percent;
    }

    public void setFund_percent(float fund_percent) {
        this.fund_percent = fund_percent;
    }

}
