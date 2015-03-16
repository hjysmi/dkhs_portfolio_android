/**
 * @Title SignupBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-16 下午1:26:50
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName SignupBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-3-16 下午1:26:50
 * @version 1.0
 */
public class SignupBean {

    @SerializedName("is_new_user")
    private boolean isNewUser;
    private UserEntity user;
    private String tokenString;

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getToken() {
        return tokenString;
    }

    public void setToken(String token) {
        this.tokenString = token;
    }
}
