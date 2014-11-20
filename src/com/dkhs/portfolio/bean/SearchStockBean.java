/**
 * @Title SearchStockBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午2:36:31
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @ClassName SearchStockBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-17 下午2:36:31
 * @version 1.0
 */

@Table(name = "SearchStock")
// 建议加上注解， 混淆后表名不受影响
public class SearchStockBean extends DBEntityBase {
    @Column(column = "stock_name")
    @SerializedName("abbr_name")
    private String stockName;
    @Column(column = "stock_code")
    @SerializedName("symbol")
    private String stockCode;
    // @Column(column = "stock_id")
    // private long stockId;

    @SerializedName("chi_spell")
    @Column(column = "chi_spell")
    private String chiSpell;
    @Column(column = "symbol_type")
    private String symbol_type;

    // "symbol_stype": symbol子类型（300, '股票'),(301, '混合型基金'),(302, '债卷型基金'),(303, '指数型基金'),(304, '保本型基金'),(305,
    // 'qdii'),(306, 'etf'),(307, 'lof'),(308, 'other')
    @Column(column = "symbol_stype")
    private String symbol_stype;

    @Column(column = "list_status")
    private String list_status;
    @Column(column = "is_stop")
    private String is_stop;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getChiSpell() {
        return chiSpell;
    }

    public void setChiSpell(String chiSpell) {
        this.chiSpell = chiSpell;
    }

    public String getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(String symbol_type) {
        this.symbol_type = symbol_type;
    }

    public String getList_status() {
        return list_status;
    }

    public void setList_status(String list_status) {
        this.list_status = list_status;
    }

    public String getIs_stop() {
        return is_stop;
    }

    public void setIs_stop(String is_stop) {
        this.is_stop = is_stop;
    }

    public boolean isStop() {
        return this.is_stop.equalsIgnoreCase("1") ? true : false;
    }
}
