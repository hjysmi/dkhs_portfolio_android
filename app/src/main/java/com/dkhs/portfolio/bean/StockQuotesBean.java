/**
 * @Title StockQuotesBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-26 下午5:09:55
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName StockQuotesBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-26 下午5:09:55
 */
public class StockQuotesBean extends QuotesBean {


    private float change;
    @SerializedName("last_close")
    private float lastClose;
    @SerializedName("buy_price_level")
    private BuyPrice buyPrice;
    private String moment;
    private String trade_status;
    // 0开市 1休市 2闭市
    private String tradetile;

    // 0为false,1为true
    private int is_stop;

    // 换手率
    private float turnover_rate;


    // 振幅
    private float amplitude;
    // 流通市值 单位：元
    private float market_capital;
    // 总市值 单位：元
    private float total_capital;
    // 动态市盈率
    private float pe_ttm;

    // 静态态市盈率
    private float pe_lyr;
    // 市净率
    private float pb;


    @SerializedName("sell_price_level")
    private SellPrice sellPrice;

    private List<FiveRangeItem> buyList;
    private List<FiveRangeItem> sellList;

    public class BuyPrice {
        @SerializedName("buy_vol")
        private List<String> buyVol;
        @SerializedName("buy_price")
        private List<String> buyPrice;

        public List<String> getBuyVol() {
            return buyVol;
        }

        public void setBuyVol(List<String> buyVol) {
            this.buyVol = buyVol;
        }

        public List<String> getBuyPrice() {
            return buyPrice;
        }

        public void setBuyPrice(List<String> buyPrice) {
            this.buyPrice = buyPrice;
        }
    }

    public class SellPrice {
        @SerializedName("sell_vol")
        private List<String> sellVol;
        @SerializedName("sell_price")
        private List<String> sellPrice;

        public List<String> getSellVol() {
            return sellVol;
        }

        public void setSellVol(List<String> sellVol) {
            this.sellVol = sellVol;
        }

        public List<String> getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(List<String> sellPrice) {
            this.sellPrice = sellPrice;
        }
    }

    public float getTotal_capital() {
        return total_capital;
    }

    public void setTotal_capital(float total_capital) {
        this.total_capital = total_capital;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public float getLastClose() {
        return lastClose;
    }

    public void setLastClose(float lastClose) {
        this.lastClose = lastClose;
    }

    public BuyPrice getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BuyPrice buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public void setTradetile(String tradetile) {
        this.tradetile = tradetile;
    }

    public int getIs_stop() {
        return is_stop;
    }

    public void setIs_stop(int is_stop) {
        this.is_stop = is_stop;
    }

    public float getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(float turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public float getMarket_capital() {
        return market_capital;
    }

    public void setMarket_capital(float market_capital) {
        this.market_capital = market_capital;
    }

    public float getPe_ttm() {
        return pe_ttm;
    }

    public void setPe_ttm(float pe_ttm) {
        this.pe_ttm = pe_ttm;
    }

    public float getPe_lyr() {
        return pe_lyr;
    }

    public void setPe_lyr(float pe_lyr) {
        this.pe_lyr = pe_lyr;
    }

    public float getPb() {
        return pb;
    }

    public void setPb(float pb) {
        this.pb = pb;
    }

    public SellPrice getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(SellPrice sellPrice) {
        this.sellPrice = sellPrice;
    }

    public List<FiveRangeItem> getBuyList() {
        return buyList;
    }

    public void setBuyList(List<FiveRangeItem> buyList) {
        this.buyList = buyList;
    }

    public List<FiveRangeItem> getSellList() {
        return sellList;
    }

    public void setSellList(List<FiveRangeItem> sellList) {
        this.sellList = sellList;
    }


    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    public String getTradetile() {
        if (TextUtils.isEmpty(getTrade_status())) {
            return "";
        }
        if (getTrade_status().equalsIgnoreCase("0")) {
            return "开市";
        }
        if (getTrade_status().equalsIgnoreCase("1")) {
            return "休市";
        }
        if (getTrade_status().equalsIgnoreCase("2")) {
            return "闭市";
        }
        if (getTrade_status().equalsIgnoreCase("3")) {
            return "集合竞价";
        }
        return "";
    }

}
