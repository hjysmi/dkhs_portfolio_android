/**
 * @Title ConbinationBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-27 下午3:06:15
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName ConbinationBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-27 下午3:06:15
 * @version 1.0
 */
public class ConbinationBean {
    String name;
    float currentValue;
    float addUpValue;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param name
     * @param currentValue
     * @param addUpValue
     */
    public ConbinationBean(String name, float currentValue, float addUpValue) {
        super();
        this.name = name;
        this.currentValue = currentValue;
        this.addUpValue = addUpValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public float getAddUpValue() {
        return addUpValue;
    }

    public void setAddUpValue(float addUpValue) {
        this.addUpValue = addUpValue;
    }

}
