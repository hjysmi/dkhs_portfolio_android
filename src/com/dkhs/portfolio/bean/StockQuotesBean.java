/**
 * @Title StockQuotesBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-26 下午5:09:55
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.util.List;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName StockQuotesBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-26 下午5:09:55
 * @version 1.0
 */
public class StockQuotesBean {

    private String code;
    private float high;
    private float open;
    private float low;
    private float percentage;
    private float current;
    private float change;
    @SerializedName("last_close")
    private float lastClose;
    private long id;
    @SerializedName("buy_price_level")
    private BuyPrice buyPrice;
    @SerializedName("abbr_name")
    private String name;
    private boolean followed;
    private String symbol;
    private String moment;
    private String trade_status;
    // 0开市 1休市 2闭市
    private String tradetile;

    // 成交量
    private float volume;
    // 成交额
    private float amount;
    // 换手率
    private float turnover_rate;
    // 流通市值 单位：元
    private float market_capital;
    // 总市值 单位：元
    private float total_capital;
    // 动态市盈率
    private float pe_ttm;
    // 市净率
    private float pb;

    // (0, '其他'),(1, '股票'),(2, '债券'),(3, '基金'),(4, '权证'),(5, '指数'),(6, '集合理财'),(9, '期货'),(10, '期权')
    private String symbol_type;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BuyPrice getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BuyPrice buyPrice) {
        this.buyPrice = buyPrice;
    }

    public SellPrice getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(SellPrice sellPrice) {
        this.sellPrice = sellPrice;
    }

    public float getLastClose() {
        return lastClose;
    }

    public void setLastClose(float lastClose) {
        this.lastClose = lastClose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
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

    public float getTotal_capital() {
        return total_capital;
    }

    public void setTotal_capital(float total_capital) {
        this.total_capital = total_capital;
    }

    public float getPe_ttm() {
        return pe_ttm;
    }

    public void setPe_ttm(float pe_ttm) {
        this.pe_ttm = pe_ttm;
    }

    public float getPb() {
        return pb;
    }

    public void setPb(float pb) {
        this.pb = pb;
    }

    public String getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(String symbol_type) {
        this.symbol_type = symbol_type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
        if (getTrade_status().equalsIgnoreCase("")) {
            return "集合竞价";
        }
        return "";
    }

}
