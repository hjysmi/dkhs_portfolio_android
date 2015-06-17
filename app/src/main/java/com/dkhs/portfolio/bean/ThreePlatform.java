/**
 * @Title ThreePlatform.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-20 下午2:26:36
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName ThreePlatform
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-20 下午2:26:36
 * @version 1.0
 */
public class ThreePlatform {
    private String access_token;
    private String openid;
    private String refresh_token;
    private String avatar;
    private String username;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
