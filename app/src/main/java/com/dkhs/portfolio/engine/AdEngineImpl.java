package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AdEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/18.
 */
public class AdEngineImpl {


    public static  void getNewsBannerAds(  IHttpListener listener) {
        DKHSClient.request(HttpRequest.HttpMethod.GET,DKHSUrl.Ads.getNewsBannerAds, null, listener);
    }
    public static void getSplashAds( IHttpListener listener) {
        DKHSClient.request(HttpRequest.HttpMethod.GET,DKHSUrl.Ads.getSplashAds, null, listener);
    }
    public static void getInvitingInfo( IHttpListener listener) {
        DKHSClient.request(HttpRequest.HttpMethod.GET,DKHSUrl.Ads.getInvitingInfo, null, listener);
    }

     public static void getInvitations( IHttpListener listener) {
        DKHSClient.request(HttpRequest.HttpMethod.GET,DKHSUrl.Ads.getInvitations, null, listener);
    }





}
