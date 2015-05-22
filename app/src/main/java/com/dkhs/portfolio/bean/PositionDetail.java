/**
 * @Title PositionDetail.java
 * @Package com.dkhs.portfolio.bean
 * @Description 持仓明细
 * @author zjz
 * @date 2014-9-15 上午10:48:51
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName PositionDetail
 * @Description 持仓明细
 * @date 2014-9-15 上午10:48:51
 */
@Parcel
public class PositionDetail {

    static final long serialVersionUID = 4124672683521681615L;
    CombinationBean portfolio;
    @SerializedName("current_date")
    String currentDate;
    @SerializedName("positions_detail")
    List<PositionAdjustBean> adjustList;
    @SerializedName("positions")
    List<ConStockBean> positionList;
    float fund_percent;

    public PositionDetail() {
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
