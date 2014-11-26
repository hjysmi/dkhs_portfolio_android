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

import com.dkhs.portfolio.bean.ChampionBean.CombinationUser;
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
    private String id;
    private String name;
    private CombinationUser createUser;
    private String description;
    private float percent;
    @SerializedName("cumulative")
    private float addUpValue;
    @SerializedName("net_value")
    private float netvalue;
    @SerializedName("created_at")
    private String createTime;
    @SerializedName("is_public")
    private String ispublic;
    private boolean ispublics;
    private float chng_pct_day;
    private float chng_pct_month;
    private float chng_pct_week;
    private float chng_pct_three_month;
    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param name
     * @param currentValue
     * @param addUpValue
     */
    public CombinationBean() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getIspublic() {
        return ispublic;
    }

    public void setIspublic(String ispublic) {
        if (ispublic.equals("0")) {
            setIspublics(true);
        } else {
            setIspublics(false);
        }
        this.ispublic = ispublic;
    }

    public boolean isIspublics() {
        return ispublics;
    }

    public void setIspublics(boolean ispublics) {
        this.ispublics = ispublics;
    }

    public CombinationUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(CombinationUser createUser) {
        this.createUser = createUser;
    }

    public static CombinationBean parse(ChampionBean cBean) {
        CombinationBean bean = new CombinationBean();
        bean.ispublic = cBean.getIs_public();
        bean.createUser = cBean.getUser();
        bean.createTime = cBean.getCreated_at();
        bean.id = cBean.getId();
        bean.description = cBean.getDescription();
        bean.name = cBean.getName();
        bean.netvalue = cBean.getNet_value();
        bean.setIspublic(cBean.getIs_public());
        bean.chng_pct_day = cBean.getChng_pct_day();
        bean.chng_pct_week = cBean.getChng_pct_week();
        bean.chng_pct_month = cBean.getChng_pct_month();
        bean.chng_pct_three_month = cBean.getChng_pct_three_month();
        return bean;
    }

	public float getChng_pct_day() {
		return chng_pct_day;
	}

	public void setChng_pct_day(float chng_pct_day) {
		this.chng_pct_day = chng_pct_day;
	}

	public float getChng_pct_month() {
		return chng_pct_month;
	}

	public void setChng_pct_month(float chng_pct_month) {
		this.chng_pct_month = chng_pct_month;
	}

	public float getChng_pct_week() {
		return chng_pct_week;
	}

	public void setChng_pct_week(float chng_pct_week) {
		this.chng_pct_week = chng_pct_week;
	}

	public float getChng_pct_three_month() {
		return chng_pct_three_month;
	}

	public void setChng_pct_three_month(float chng_pct_three_month) {
		this.chng_pct_three_month = chng_pct_three_month;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
