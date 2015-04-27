package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FollowerBean
 * @date 2015/4/23.12:22
 * @Description TODO(关注人和粉丝的试题类)
 */
public class PeopleBean {






    /**
     * symbols_count : 0
     * friends_count : 1
     * is_active : true
     * gender : 1
     * city : 浦东
     * description : 这是测试帐号testuser1
     * avatar_md : /static/common/img/avatar_80x80.png
     * avatar_xs :
     * avatar_lg :
     * avatar_sm :
     * province : 上海
     * followed_by_count : 2
     * id : 15
     * category : [1,5]
     * status_count : 0
     * username : testuser1
     * me_follow : true
     */
    private int symbols_count;
    private int friends_count;
    private boolean is_active;
    private String gender;
    private String city;
    private String description;
    private String avatar_md;
    private String avatar_xs;
    private String avatar_lg;
    private String avatar_sm;
    private String province;
    private int followed_by_count;
    private int id;
    private List<Integer> category;
    private int status_count;
    private String username;
    private boolean me_follow;


    /**
     * total_count : 1
     * total_page : 1
     * results : [{"symbols_count":0,"follow_me":false,"friends_count":0,"is_active":true,"gender":"","city":"","description":"","avatar_md":"http://com-dkhs-media-test.oss.aliyuncs.com/avatar/2015/04/01/10/4409/gegugonggao.320x320.png","avatar_xs":"http://com-dkhs-media-test.oss.aliyuncs.com/avatar/2015/04/01/10/4409/gegugonggao.80x80.png","avatar_lg":"http://com-dkhs-media-test.oss.aliyuncs.com/avatar/2015/04/01/10/4409/gegugonggao.640x640.png","avatar_sm":"http://com-dkhs-media-test.oss.aliyuncs.com/avatar/2015/04/01/10/4409/gegugonggao.160x160.png","province":"","followed_by_count":0,"id":1,"date_joined":"2014-12-08T09:00:32Z","category":[],"status_count":0,"username":"root"}]
     * current_page : 1
     */
    private int total_count;
    private int total_page;


    public void setSymbols_count(int symbols_count) {
        this.symbols_count = symbols_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvatar_md(String avatar_md) {
        this.avatar_md = avatar_md;
    }

    public void setAvatar_xs(String avatar_xs) {
        this.avatar_xs = avatar_xs;
    }

    public void setAvatar_lg(String avatar_lg) {
        this.avatar_lg = avatar_lg;
    }

    public void setAvatar_sm(String avatar_sm) {
        this.avatar_sm = avatar_sm;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setFollowed_by_count(int followed_by_count) {
        this.followed_by_count = followed_by_count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(List<Integer> category) {
        this.category = category;
    }

    public void setStatus_count(int status_count) {
        this.status_count = status_count;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMe_follow(boolean me_follow) {
        this.me_follow = me_follow;
    }

    public int getSymbols_count() {
        return symbols_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getAvatar_md() {
        return avatar_md;
    }

    public String getAvatar_xs() {
        return avatar_xs;
    }

    public String getAvatar_lg() {
        return avatar_lg;
    }

    public String getAvatar_sm() {
        return avatar_sm;
    }

    public String getProvince() {
        return province;
    }

    public int getFollowed_by_count() {
        return followed_by_count;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getCategory() {
        return category;
    }

    public int getStatus_count() {
        return status_count;
    }

    public String getUsername() {
        return username;
    }

    public boolean isMe_follow() {
        return me_follow;
    }
}
