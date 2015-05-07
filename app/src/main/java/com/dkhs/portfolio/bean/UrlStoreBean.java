/**
 * @Title UrlStoreBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-1-20 上午9:41:46
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.lidroid.xutils.db.annotation.Id;

/**
 * @ClassName UrlStoreBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-1-20 上午9:41:46
 * @version 1.0
 */
public class UrlStoreBean {
    private String authorization;
    @Id
    private String url;
    private String responseJson;

    // private int id;
    //
    // public int getId() {
    // return id;
    // }
    //
    // public void setId(int id) {
    // this.id = id;
    // }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }
}
