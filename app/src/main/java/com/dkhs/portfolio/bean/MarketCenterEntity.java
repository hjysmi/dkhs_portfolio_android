package com.dkhs.portfolio.bean;

import java.util.List;

public class MarketCenterEntity {
	private String total_count;
	private String total_page;
	private String current_page;
	private List<StockQuotesBean> results;
	public String getTotal_count() {
		return total_count;
	}
	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}
	public String getTotal_page() {
		return total_page;
	}
	public void setTotal_page(String total_page) {
		this.total_page = total_page;
	}
	public String getCurrent_page() {
		return current_page;
	}
	public void setCurrent_page(String current_page) {
		this.current_page = current_page;
	}
	public List<StockQuotesBean> getResults() {
		return results;
	}
	public void setResults(List<StockQuotesBean> results) {
		this.results = results;
	}
	
	
}
