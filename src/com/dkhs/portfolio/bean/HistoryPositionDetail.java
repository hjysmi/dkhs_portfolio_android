/**
 * @Title HistoryNetValue.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-23 下午4:10:39
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * @ClassName HistoryNetValue
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-23 下午4:10:39
 * @version 1.0
 */
public class HistoryPositionDetail {
    private int total_count;
    private int total_page;
    private int current_page;
    private List<HistoryPositionBean> results;

    public class HistoryPositionBean {
        private String created_at;
        private List<HistoryPositionItem> detailed_change;
        private String dateString;
		public String getDateString() {
			return dateString;
		}
		public void setDateString(String dateString) {
			this.dateString = dateString;
		}
		public String getCreated_at() {
			return created_at;
		}
		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}
		public List<HistoryPositionItem> getDetailed_change() {
			return detailed_change;
		}
		public void setDetailed_change(List<HistoryPositionItem> detailed_change) {
			this.detailed_change = detailed_change;
		}
        
    }
    public class HistoryPositionItem{
    	private String symbol;
    	private String symbol_code;
    	private String symbol_name;
    	private float from_percent;
    	private float to_percent;
    	private String created_at;
    	private String hourTime;
    	private String changeStr;
    	
		public String getHourTime() {
			return hourTime;
		}
		public void setHourTime(String hourTime) {
			this.hourTime = hourTime;
		}
		public String getChangeStr() {
			return changeStr;
		}
		public void setChangeStr(String changeStr) {
			this.changeStr = changeStr;
		}
		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		public String getSymbol_code() {
			return symbol_code;
		}
		public void setSymbol_code(String symbol_code) {
			this.symbol_code = symbol_code;
		}
		public String getSymbol_name() {
			return symbol_name;
		}
		public void setSymbol_name(String symbol_name) {
			this.symbol_name = symbol_name;
		}
		public float getFrom_percent() {
			return from_percent;
		}
		public void setFrom_percent(float from_percent) {
			this.from_percent = from_percent;
		}
		public float getTo_percent() {
			return to_percent;
		}
		public void setTo_percent(float to_percent) {
			this.to_percent = to_percent;
		}
		public String getCreated_at() {
			return created_at;
		}
		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}
    	
    }
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public int getTotal_page() {
		return total_page;
	}
	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}
	public int getCurrent_page() {
		return current_page;
	}
	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}
	public List<HistoryPositionBean> getResults() {
		return results;
	}
	public void setResults(List<HistoryPositionBean> results) {
		this.results = results;
	}
    
}
