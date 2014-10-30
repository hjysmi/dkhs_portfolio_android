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

}
