package com.dkhs.portfolio.ui.widget.kline;

import com.google.gson.annotations.SerializedName;

public class OHLCEntity {

	private double volume; //成交量
	private double open;// 开盘价
	private double high;// 最高价
	private double low;// 最低价
	private double close;// 收盘价
	@SerializedName("tradedate")
	private String date;// 日期，如：2013-09-18
	private double change;
	private double percentage;
	private double macd;
    private double diff;
    private double dea;
    private double ma5;
    private double ma10;
    private double ma20;
    private double ma30;
    @SerializedName("vol_ma5")
    private double vol5;
    @SerializedName("vol_ma10")
    private double vol10;
    @SerializedName("vol_ma20")
    private double vol20;
    @SerializedName("div_info")
    private String info;
	public OHLCEntity() {
		super();
	}

	public OHLCEntity(double volumn,double open, double high, double low, double close, String date,double macd,double diff,double dea,String info) {
		super();
		this.volume = volumn;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.date = date;
		this.macd = macd;
        this.diff = diff;
        this.dea = dea;
        this.info = info;
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

    public double getMacd() {
        return macd;
    }

    public void setMacd(double macd) {
        this.macd = macd;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public double getDea() {
        return dea;
    }

    public void setDea(double dea) {
        this.dea = dea;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getMa5() {
        return ma5;
    }

    public void setMa5(double ma5) {
        this.ma5 = ma5;
    }

    public double getMa10() {
        return ma10;
    }

    public void setMa10(double ma10) {
        this.ma10 = ma10;
    }

    public double getMa20() {
        return ma20;
    }

    public void setMa20(double ma20) {
        this.ma20 = ma20;
    }

    public double getMa30() {
        return ma30;
    }

    public void setMa30(double ma30) {
        this.ma30 = ma30;
    }

    public double getVol5() {
        return vol5;
    }

    public void setVol5(double vol5) {
        this.vol5 = vol5;
    }

    public double getVol10() {
        return vol10;
    }

    public void setVol10(double vol10) {
        this.vol10 = vol10;
    }

    public double getVol20() {
        return vol20;
    }

    public void setVol20(double vol20) {
        this.vol20 = vol20;
    }
	
}
