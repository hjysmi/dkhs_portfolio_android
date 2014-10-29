/**
 * @Title ChampionBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 上午11:28:18
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName ChampionBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 上午11:28:18
 * @version 1.0
 */
public class ChampionBean {
    private String id;
    private String name;
    @SerializedName("increase_percent")
    private float increasePercent;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getIncreasePercent() {
        return increasePercent;
    }
    public void setIncreasePercent(float increasePercent) {
        this.increasePercent = increasePercent;
    }
}
