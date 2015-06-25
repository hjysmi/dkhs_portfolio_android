package com.dkhs.portfolio.bean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName WapShareBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/25.
 */
public class WapShareBean  extends ShareBean {

    /**
     * content : 下载谁牛APP追牛股，输入我的邀请码(83125706)免费领流量，参加活动赢大奖。我在谁牛，等你来PK!
     * title : 千元大奖500M流量周周送
     * successCallback : success
     * errorCallback : error
     * img : https://www.dkhs.com/static/portfolio/img/shuiniuwap/favicon.png
     * url : https://www.dkhs.com/portfolio/portfoliogame/
     */
    private String successCallback;
    private String errorCallback;

    public void setSuccessCallback(String successCallback) {
        this.successCallback = successCallback;
    }
    public void setErrorCallback(String errorCallback) {
        this.errorCallback = errorCallback;
    }

    public String getSuccessCallback() {
        return successCallback;
    }
    public String getErrorCallback() {
        return errorCallback;
    }


}
