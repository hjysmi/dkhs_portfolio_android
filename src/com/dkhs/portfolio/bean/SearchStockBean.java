/**
 * @Title SearchStockBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午2:36:31
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

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
    private String stockName;
    @Column(column = "stock_code")
    private String stockCode;
    @Column(column = "stock_id")
    private long stockId;

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

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }
}
