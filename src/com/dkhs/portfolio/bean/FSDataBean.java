/**
 * @Title FSDataBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-29 上午10:06:42
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * @ClassName FSDataBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-29 上午10:06:42
 * @version 1.0
 */
public class FSDataBean {
    private long amend;
    private long amstart;
    private long pmstart;
    private long pmend;
    private String curtime;
    /*
     * [
     * "1411983240.0", 时间戳
     * "9.31",当前价
     * "286104",成交量
     * "9.31",均价
     * "0.0"涨跌幅
     * ],
     */
    private List<TimeStock> mainstr;

    public long getAmend() {
        return amend;
    }

    public void setAmend(long amend) {
        this.amend = amend;
    }

    public long getAmstart() {
        return amstart;
    }

    public void setAmstart(long amstart) {
        this.amstart = amstart;
    }

    public long getPmstart() {
        return pmstart;
    }

    public void setPmstart(long pmstart) {
        this.pmstart = pmstart;
    }

    public long getPmend() {
        return pmend;
    }

    public void setPmend(long pmend) {
        this.pmend = pmend;
    }

    public List<TimeStock> getMainstr() {
        return mainstr;
    }

    public void setMainstr(List<TimeStock> mainstr) {
        this.mainstr = mainstr;
    }

    /**
     * "volume": "0",
     * "avgcurrent": "19.78",
     * "avgline": 0,
     * "current": "19.78",
     * "time": "2014-09-29T09:29:00",
     * "percentage": "0.0"
     * */

    public class TimeStock {
        private float volume;
        private float avgcurrent;
        private float avgline;
        private float current;
        private float percentage;
        private String time;

        public float getVolume() {
            return volume;
        }

        public void setVolume(float volume) {
            this.volume = volume;
        }

        public float getAvgcurrent() {
            return avgcurrent;
        }

        public void setAvgcurrent(float avgcurrent) {
            this.avgcurrent = avgcurrent;
        }

        public float getAvgline() {
            return avgline;
        }

        public void setAvgline(float avgline) {
            this.avgline = avgline;
        }

        public float getCurrent() {
            return current;
        }

        public void setCurrent(float current) {
            this.current = current;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public String getCurtime() {
        return curtime;
    }

    public void setCurtime(String curtime) {
        this.curtime = curtime;
    }

}
