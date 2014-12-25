/**
 * @Title SectorBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-24 下午4:51:06
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName SectorBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-24 下午4:51:06
 * @version 1.0
 */
public class SectorBean {
    private String id;
    private String name;
    private float percentage;
    private float top_symbol_current;
    private float top_symbol_percentage;
    private String top_symbol_id;
    private String top_symbol;
    private String top_symbol_name;

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

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getTop_symbol_current() {
        return top_symbol_current;
    }

    public void setTop_symbol_current(float top_symbol_current) {
        this.top_symbol_current = top_symbol_current;
    }

    public float getTop_symbol_percentage() {
        return top_symbol_percentage;
    }

    public void setTop_symbol_percentage(float top_symbol_percentage) {
        this.top_symbol_percentage = top_symbol_percentage;
    }

    public String getTop_symbol_id() {
        return top_symbol_id;
    }

    public void setTop_symbol_id(String top_symbol_id) {
        this.top_symbol_id = top_symbol_id;
    }

    public String getTop_symbol() {
        return top_symbol;
    }

    public void setTop_symbol(String top_symbol) {
        this.top_symbol = top_symbol;
    }

    public String getTop_symbol_name() {
        return top_symbol_name;
    }

    public void setTop_symbol_name(String top_symbol_name) {
        this.top_symbol_name = top_symbol_name;
    }
}
