/**
 * @Title BindThreePlat.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-25 上午9:49:33
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

/**
 * @ClassName BindThreePlat
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-25 上午9:49:33
 * @version 1.0
 */
public class BindThreePlat {
    // {"status": true, "username": "13606907270", "provider": "mobile"}
    private boolean status;
    private String username;
    private String provider;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
