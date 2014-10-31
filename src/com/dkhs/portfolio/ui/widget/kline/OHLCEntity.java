package com.dkhs.portfolio.ui.widget.kline;

public class OHLCEntity {

	private double volume; //成交量
	private double open;// 开盘价
	private double high;// 最高价
	private double low;// 最低价
	private double close;// 收盘价
	private String date;// 日期，如：2013-09-18
	private double change;
	private double percentage;

	public OHLCEntity() {
		super();
	}

	public OHLCEntity(double volumn,double open, double high, double low, double close, String date) {
		super();
		this.volume = volumn;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.date = date;
	}
	
	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	/**
	 * 是否是提升的
	 * @return
	 */
	public boolean isup() {
		return close >= open;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}
	
}
