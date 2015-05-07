/**
 * @Title TodayNetBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-23 上午11:29:40
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName TodayNetBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-23 上午11:29:40
 * @version 1.0
 */
public class TodayNetBean {
    private String timestamp;
    private float netvalue;
    private float percentage;
    private float change;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }
}
