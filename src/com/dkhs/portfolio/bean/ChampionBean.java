/**
 * @Title ChampionBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 上午11:28:18
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName ChampionBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 上午11:28:18
 * @version 1.0
 */
public class ChampionBean implements Serializable {

    private static final long serialVersionUID = 129599595218L;
    private String id;
    private String name;
    // 0公开/1保密
    private String is_public;
     @SerializedName("increase_percent")
     private float increasePercent;
    private String created_at;
    private CombinationUser user;
    private String description;

    private float chng_pct_month;
    private float chng_pct_week;
    private float chng_pct_three_month;

    // "chng_pct_day": 100805.451612477,
    // "chng_pct_week": 1.21951342938796,
    // "chng_pct_month": 1.18893024702341,
    // "chng_pct_three_month": 1.18893024702341

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

    public class CombinationUser implements Serializable {

        private static final long serialVersionUID = 12894595218L;
        private String id;
        private String username;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public CombinationUser getUser() {
        return user;
    }

    public void setUser(CombinationUser user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

}
