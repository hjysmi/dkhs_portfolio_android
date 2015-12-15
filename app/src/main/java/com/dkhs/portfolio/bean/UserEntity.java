package com.dkhs.portfolio.bean;

import android.text.TextUtils;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

import org.parceler.Parcel;

@Parcel
public class UserEntity {

    // @Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    @NoAutoIncrement
    long id;

    /**
     * 昵称
     */
    String username;
    String mobile;
    boolean isFirstRegister;
    /**
     * 性别：0=保密, 1=男, 2=女.
     */
    String gender;
    /**
     * 简介
     */
    String description;
    /**
     * 省份
     */
    String province;
    /**
     * 城市
     */
    String city;
    /**
     * 关注数
     */
    int followed_by_count;
    /**
     * 粉丝数
     */
    int friends_count;
    /**
     * 评论数
     */
    int status_count;
    /**
     * 自建数
     */
    int symbols_count;
    /**
     * 30x30头像地址
     */

    String avatar_xs;
    String access_token;
    String avatar_md;
    String avatar_sm;
    String avatar_lg;


    String chi_spell;
    String chi_spell_all;


    String date_joined;

    boolean me_follow;

    public boolean verified = false;
    public int verified_type = -1;
    public String verified_reason;
    /*"verified": true, #是否认证用户
    "verified_type": 1, #认证类型 0, 投资牛人 1, 投资顾问 2, 分析师 3, 基金执业 4, 期货执业
    "verified_status": 1, #认证审核状态 0, '审核中' 1, '已认证' 2, '审核失败'
            "verified_reason": "兴业证券 投资顾问 aaa", #认证原因*/
    public int verified_status = -1;
    public UserEntity() { /*Required empty bean constructor*/ }

    int portfolios_following_count;


    public int getPortfolios_following_count() {
        return portfolios_following_count;
    }

    public void setPortfolios_following_count(int portfolios_following_count) {
        this.portfolios_following_count = portfolios_following_count;
    }

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

    public String getRawDescription() {
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

    public String getChi_spell() {
        return chi_spell;
    }

    public void setChi_spell(String chi_spell) {
        this.chi_spell = chi_spell;
    }

    public String getChi_spell_all() {
        return chi_spell_all;
    }

    public void setChi_spell_all(String chi_spell_all) {
        this.chi_spell_all = chi_spell_all;
    }


    public static boolean currentUser(String userId) {
        return null != UserEngineImpl.getUserEntity() && (UserEngineImpl.getUserEntity().getId() + "").equals(userId);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "access_token='" + access_token + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", isFirstRegister=" + isFirstRegister +
                ", gender='" + gender + '\'' +
                ", description='" + description + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", followed_by_count=" + followed_by_count +
                ", friends_count=" + friends_count +
                ", status_count=" + status_count +
                ", symbols_count=" + symbols_count +
                ", avatar_xs='" + avatar_xs + '\'' +
                ", avatar_md='" + avatar_md + '\'' +
                ", avatar_sm='" + avatar_sm + '\'' +
                ", avatar_lg='" + avatar_lg + '\'' +
                ", chi_spell='" + chi_spell + '\'' +
                ", chi_spell_all='" + chi_spell_all + '\'' +
                ", date_joined='" + date_joined + '\'' +
                ", me_follow=" + me_follow +
                ", portfolios_following_count=" + portfolios_following_count +
                '}';
    }


    public enum VERIFIEDTYPE {
        //未认证
        NORMAL(-1),
        // 投资牛人
        EXPERT(0),
        // 投资顾问
        ADVISER(1),
        //  分析师
        ANALYST(2),
        // 基金执业
        FUND_CERTIFICATE(3),
        //期货执业
        FUTURES_CERTIFICATE(4);
        VERIFIEDTYPE(int type) {
            this.typeid = type;
        }
        private int typeid;
        public int getTypeid(){
            return typeid;
        }
        public static VERIFIEDTYPE getEnumType(int type){
            switch (type){
                case 0:
                    return EXPERT;
                case 1:
                    return ADVISER;
                case 2:
                    return ANALYST;
                case 3:
                    return FUND_CERTIFICATE;
                case 4:
                    return FUTURES_CERTIFICATE;
            }
            return NORMAL;
        }
    }

    public enum VERIFIEDSTATUS {
        VERIFYING(0),
        SUCCESS(1),
        FAIL(2);
        VERIFIEDSTATUS(int type) {
            this.typeid = type;
        }
        private int typeid;
        public int getTypeid(){
            return typeid;
        }
    }
}
