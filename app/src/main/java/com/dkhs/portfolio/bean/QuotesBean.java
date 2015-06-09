/**
 * @Title StockQuotesBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-26 下午5:09:55
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author zjz
 * @version 1.0
 * @ClassName StockQuotesBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-26 下午5:09:55
 */
public class QuotesBean {

    long id;
    @SerializedName("abbr_name")
    String abbrName;
    String name;
    String symbol;
    String code;
    // (0, '其他'),(1, '股票'),(2, '债券'),(3, '基金'),(4, '权证'),(5, '指数'),(6, '集合理财'),(9, '期货'),(10, '期权')
    String symbol_type;
    int symbol_stype;
    int list_status;
    String chi_spell;

    float high;
    float open;
    float low;
    float percentage;
    float current;
    // 成交量
    float volume;
    // 成交额
    float amount;

    boolean followed;

    String server_time;

    @SerializedName("alert_settings")
    public AlertSetBean alertSetBean;

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public String getAbbrName() {
        return abbrName;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    public AlertSetBean getAlertSetBean() {
        return alertSetBean;
    }

    public void setAlertSetBean(AlertSetBean alertSetBean) {
        this.alertSetBean = alertSetBean;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getChi_spell() {
        return chi_spell;
    }

    public void setChi_spell(String chi_spell) {
        this.chi_spell = chi_spell;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getList_status() {
        return list_status;
    }

    public void setList_status(int list_status) {
        this.list_status = list_status;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getSymbol_stype() {
        return symbol_stype;
    }

    public void setSymbol_stype(int symbol_stype) {
        this.symbol_stype = symbol_stype;
    }

    public String getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(String symbol_type) {
        this.symbol_type = symbol_type;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }


    public String getStypeText() {
//       (300, '股票型')
//        (301, '混合型')
//        (302, '债卷型')
//        (303, '指数型')
//        (305, 'QDII')
//        (306, '货币型')
//        (307, '理财型')
//        (308, '封闭型')
        String stypeText = "";
        if (getSymbol_stype() == 300) {
            stypeText = "股票型";
        } else if (getSymbol_stype() == 301) {
            stypeText = "混合型";
        } else if (getSymbol_stype() == 302) {
            stypeText = "债卷型";
        } else if (getSymbol_stype() == 303) {
            stypeText = "指数型";
        } else if (getSymbol_stype() == 305) {
            stypeText = "QDII";
        } else if (getSymbol_stype() == 306) {
            stypeText = "货币型";
        } else if (getSymbol_stype() == 307) {
            stypeText = "理财型";
        } else if (getSymbol_stype() == 308) {
            stypeText = "封闭型";
        }
        return stypeText;

    }
}
