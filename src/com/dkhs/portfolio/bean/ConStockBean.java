/**
 * @Title CombinationStock.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 上午9:37:22
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName CombinationStock
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-26 上午9:37:22
 * @version 1.0
 */
public class ConStockBean {
    int id;
    int dutyValue;
    int dutyColor;
    float currentValue;
    float increaseValue;
    String name;
    String num;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param id Id
     * @param dutyValue 占比
     * @param dutyColor 占比颜色值
     * @param name 股票名称
     * @param num 股票号码
     */
    public ConStockBean(int id, int dutyValue, int dutyColor, String name, String num) {
        super();
        this.id = id;
        this.dutyValue = dutyValue;
        this.dutyColor = dutyColor;
        this.name = name;
        this.num = num;
    }

    public ConStockBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDutyValue() {
        return dutyValue;
    }

    public void setDutyValue(int dutyValue) {
        this.dutyValue = dutyValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getDutyColor() {
        return dutyColor;
    }

    public void setDutyColor(int dutyColor) {
        this.dutyColor = dutyColor;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public float getIncreaseValue() {
        return increaseValue;
    }

    public void setIncreaseValue(float increaseValue) {
        this.increaseValue = increaseValue;
    }

    public boolean equals(Object obj) {
        ConStockBean param = (ConStockBean) obj;
        if (this.id == param.id) {
            return true;
        } else {
            return false;
        }
    }
}
