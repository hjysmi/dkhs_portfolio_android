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
    private int is_public;
    @SerializedName("increase_percent")
    private float increasePercent;

    private User user;
    private String description;

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

    public class User implements Serializable {

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIs_public() {
        return is_public;
    }

    public void setIs_public(int is_public) {
        this.is_public = is_public;
    }

}
