package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * Created by zjz on 2015/7/3.
 */
public class MarkStockBean {

    private int total_page;
    private List<StockQuotesBean> results;
    private String server_time;
    private int total_count;
    private int trade_status;
    private int current_page;

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public void setResults(List<StockQuotesBean> results) {
        this.results = results;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public void setTrade_status(int trade_status) {
        this.trade_status = trade_status;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTotal_page() {
        return total_page;
    }

    public List<StockQuotesBean> getResults() {
        return results;
    }

    public String getServer_time() {
        return server_time;
    }

    public int getTotal_count() {
        return total_count;
    }

    public int getTrade_status() {
        return trade_status;
    }

    public int getCurrent_page() {
        return current_page;
    }
}
