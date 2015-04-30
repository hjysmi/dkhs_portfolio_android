package com.dkhs.portfolio.bean;

import android.text.TextUtils;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

public class UserEntity {

    // @Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    @NoAutoIncrement
    private long id;

    /**
     * 昵称
     */
    private String username;
    private String mobile;
    private boolean isFirstRegister;
    /**
     * 性别：0=保密, 1=男, 2=女.
     */
    private String gender;
    /**
     * 简介
     */
    private String description;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 关注数
     */
    private int followed_by_count;
    /**
     * 粉丝数
     */
    private int friends_count;
    /**
     * 评论数
     */
    private int status_count;
    /**
     * 自建数
     */
    private int symbols_count;
    /**
     * 30x30头像地址
     */
    private String avatar_xs;
    private String access_token;
    private String avatar_md;
    private String avatar_sm;
    private String avatar_lg;
    private String date_joined;

    private boolean me_follow;

    public boolean isMe_follow() {
        return me_follow;
    }

    public void setMe_follow(boolean me_follow) {
        this.me_follow = me_follow;
    }

    public int getSymbols_count() {
        return symbols_count;
    }

    public void setSymbols_count(int symbols_count) {
        this.symbols_count = symbols_count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAvatar_xs() {
        return avatar_xs;
    }

    public void setAvatar_xs(String avatar_xs) {
        this.avatar_xs = avatar_xs;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        if (TextUtils.isEmpty(description)) {
            return PortfolioApplication.getInstance().getResources().getString(R.string.desc_def_text);
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFollowed_by_count() {
        return followed_by_count;
    }

    public void setFollowed_by_count(int followed_by_count) {
        this.followed_by_count = followed_by_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getStatus_count() {
        return status_count;
    }

    public void setStatus_count(int status_count) {
        this.status_count = status_count;
    }

    public boolean isFirstRegister() {
        return isFirstRegister;
    }

    public void setFirstRegister(boolean isFirstRegister) {
        this.isFirstRegister = isFirstRegister;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar_md() {
        return avatar_md;
    }

    public void setAvatar_md(String avatar_md) {
        this.avatar_md = avatar_md;
    }

    public String getAvatar_lg() {
        return avatar_lg;
    }

    public void setAvatar_lg(String avatar_lg) {
        this.avatar_lg = avatar_lg;
    }

    public String getAvatar_sm() {
        return avatar_sm;
    }

    public void setAvatar_sm(String avatar_sm) {
        this.avatar_sm = avatar_sm;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }
}
