package com.dkhs.portfolio.engine;

import java.io.Serializable;

public class NewsforImpleEngine implements Serializable{
	private String symbolId;
	private String symbol;
	private String userid;
	private String portfolioId;
	private String contentType;
	private String contentSubType;
	private String page;
	private String pageSize;
	public String getSymbolId() {
		return symbolId;
	}
	public void setSymbolId(String symbolId) {
		this.symbolId = symbolId;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContentSubType() {
		return contentSubType;
	}
	public void setContentSubType(String contentSubType) {
		this.contentSubType = contentSubType;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	
}
