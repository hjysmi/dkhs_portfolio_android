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

    /*
     * [
     * "1411983240.0", 时间戳
     * "9.31",当前价
     * "286104",成交量
     * "9.31",均价
     * "0.0"涨跌幅
     * ],
     */
    private List<List<Float>> mainstr;

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

    public List<List<Float>> getMainstr() {
        return mainstr;
    }

    public void setMainstr(List<List<Float>> mainstr) {
        this.mainstr = mainstr;
    }

}
