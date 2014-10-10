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

@Table(name = "SearchFunds")
// 建议加上注解， 混淆后表名不受影响
public class SearchFundsBean extends DBEntityBase {
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
}
