/**
 * @Title StockBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-15 上午10:59:20
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * @ClassName StockBean
 * @Description 我的组合里面，和股票相关的bean,
 * @author zjz
 * @date 2014-9-15 上午10:59:20
 * @version 1.0
 */
@Parcel
public class StockBean  {
    private static final long serialVersionUID = 12329959598L;
    @SerializedName("symbol_code")
    protected String stockCode;
    @SerializedName("symbol")
    protected long stockId;
    @SerializedName("symbol_name")
    protected String stockName;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
