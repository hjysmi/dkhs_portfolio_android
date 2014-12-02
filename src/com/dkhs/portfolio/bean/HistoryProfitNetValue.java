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

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName HistoryNetValue
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-23 下午4:10:39
 * @version 1.0
 */
public class HistoryProfitNetValue {
    private int total_count;
    private int total_page;
    private int current_page;
    private List<HistoryNetBean> results;

    public class HistoryNetBean {
        private int id;
        private String date;
        @SerializedName("net_value")
        private float netvalue;
        private float percentage;
        @SerializedName("percentage_begin")
        private float percentageBegin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public float getNetvalue() {
            return netvalue;
        }

        public void setNetvalue(float netvalue) {
            this.netvalue = netvalue;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public float getPercentageBegin() {
            return percentageBegin;
        }

        public void setPercentageBegin(float percentageBegin) {
            this.percentageBegin = percentageBegin;
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

	public List<HistoryNetBean> getResults() {
		return results;
	}

	public void setResults(List<HistoryNetBean> results) {
		this.results = results;
	}

    
}
