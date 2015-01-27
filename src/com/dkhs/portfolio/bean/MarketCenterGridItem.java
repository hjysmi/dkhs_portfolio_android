/**
 * @Title MarketCenterGridItem.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-18 下午5:26:13
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName MarketCenterGridItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-18 下午5:26:13
 * @version 1.0
 */
public class MarketCenterGridItem {
    /**
     * 图片的路径
     */
    private String path;
    /**
     * 图片加入手机中的时间，只取了年月日
     */
    private String time;
    /**
     * 每个Item对应的HeaderId
     */
    private int headerId;

    public MarketCenterGridItem(String path, String time) {
        super();
        this.path = path;
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }
}
