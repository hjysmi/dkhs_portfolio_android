/**
 * @Title ConbinationBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-27 下午3:06:15
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName ConbinationBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-27 下午3:06:15
 * @version 1.0
 */
public class CombinationBean implements Serializable {

    private static final long serialVersionUID = 12959959598L;
    private int id;
    private String name;
    private String description;
    private float percent;
    @SerializedName("cumulative")
    private float addUpValue;
    @SerializedName("net_value")
    private float netvalue;
    @SerializedName("created_at")
    private String createTime;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param name
     * @param currentValue
     * @param addUpValue
     */
    public CombinationBean(String name, float currentValue, float addUpValue) {
        super();
        this.name = name;
        this.percent = currentValue;
        this.addUpValue = addUpValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCurrentValue() {
        return percent;
    }

    public void setCurrentValue(float currentValue) {
        this.percent = currentValue;
    }

    public float getAddUpValue() {
        return addUpValue;
    }

    public void setAddUpValue(float addUpValue) {
        this.addUpValue = addUpValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public float getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(float netvalue) {
        this.netvalue = netvalue;
    }

}
