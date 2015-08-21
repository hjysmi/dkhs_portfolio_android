package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName InvitetionBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/19.
 */
public class InvitationBean {


    /**
     * is_active : true
     * portfolios_count : 0
     * avatar_sm :
     * portfolios_following_count : 0
     * avatar_md :
     * avatar_xs :
     * symbols_count : 0
     * city : null
     * followed_by_count : 0
     * id : 4
     * status_count : 0
     * username : emmahou
     * category : []
     * date_joined : 2015-06-17T07:15:04Z
     * friends_count : 0
     * description : null
     * avatar_lg :
     * province : null
     * gender : null
     */
    private boolean is_active;
    private int portfolios_count;
    private String avatar_sm;
    private int portfolios_following_count;
    private String avatar_md;
    private String avatar_xs;
    private int symbols_count;
    private String city;
    private int followed_by_count;
    private int id;
    private int status_count;
    private String username;
    private List<?> category;
    private String date_joined;
    private int friends_count;
    private String description;
    private String avatar_lg;
    private String province;
    private String gender;

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public void setPortfolios_count(int portfolios_count) {
        this.portfolios_count = portfolios_count;
    }

    public void setAvatar_sm(String avatar_sm) {
        this.avatar_sm = avatar_sm;
    }

    public void setPortfolios_following_count(int portfolios_following_count) {
        this.portfolios_following_count = portfolios_following_count;
    }

    public void setAvatar_md(String avatar_md) {
        this.avatar_md = avatar_md;
    }


    public void setAvatar_xs(String avatar_xs) {
        this.avatar_xs = avatar_xs;
    }

    public void setSymbols_count(int symbols_count) {
        this.symbols_count = symbols_count;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFollowed_by_count(int followed_by_count) {
        this.followed_by_count = followed_by_count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus_count(int status_count) {
        this.status_count = status_count;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCategory(List<?> category) {
        this.category = category;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvatar_lg(String avatar_lg) {
        this.avatar_lg = avatar_lg;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public boolean isIs_active() {
        return is_active;
    }

    public int getPortfolios_count() {
        return portfolios_count;
    }

    public String getAvatar_sm() {
        return avatar_sm;
    }

    public int getPortfolios_following_count() {
        return portfolios_following_count;
    }

    public String getAvatar_md() {
        return avatar_md;
    }


    public String getAvatar_xs() {
        return avatar_xs;
    }

    public int getSymbols_count() {
        return symbols_count;
    }

    public String getCity() {
        return city;
    }

    public int getFollowed_by_count() {
        return followed_by_count;
    }

    public int getId() {
        return id;
    }

    public int getStatus_count() {
        return status_count;
    }

    public String getUsername() {
        return username;
    }

    public List<?> getCategory() {
        return category;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public String getDescription() {
        return description;
    }

    public String getAvatar_lg() {
        return avatar_lg;
    }

    public String getProvince() {
        return province;
    }

    public String getGender() {
        return gender;
    }


}
