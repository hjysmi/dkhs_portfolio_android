/**
 * @Title StockProfileDataBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-11 下午4:37:51
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * @ClassName StockProfileDataBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-11 下午4:37:51
 * @version 1.0
 */
public class StockProfileDataBean {
    private List<SearchStockBean> results;
    private String last_datetime;

    public List<SearchStockBean> getResults() {
        return results;
    }

    public void setResults(List<SearchStockBean> results) {
        this.results = results;
    }

    public String getLast_datetime() {
        return last_datetime;
    }

    public void setLast_datetime(String last_datetime) {
        this.last_datetime = last_datetime;
    }
}
