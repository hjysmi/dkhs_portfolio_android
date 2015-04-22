package com.dkhs.portfolio.ui.widget.chart;

public class StickEntity {

	/** Highest Value */
	private double high;
	
	/** Lowest Value */
	private double low;
	
	/** Date */
	private String date;
	
	private int color; //颜色值
	
	private boolean up; //是否提升
	private double macd;
	private double diff;
	private double dea;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public StickEntity(double high, double low, String date,double macd,double diff,double dea) {
		super();
		this.high = high;
		this.low = low;
		this.date = date;
		this.macd = macd;
		this.diff = diff;
		this.dea = dea;
	}

	public StickEntity() {
		super();
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
	
}
