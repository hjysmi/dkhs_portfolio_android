package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
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



    public static void getSignUp(   Action1<AdBean> action1) {
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Ads.getSignUp, null,new AdParseHttpListener(action1) );
    }
    public static void getInvite(   Action1<AdBean> action1) {
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Ads.getInvite, null,new AdParseHttpListener(action1) );
    }


    static   class AdParseHttpListener extends  SimpleParseHttpListener{

        Action1<AdBean> action1;

        public AdParseHttpListener(Action1<AdBean> action1) {
            this.action1 = action1;
        }

        @Override
        public Class getClassType() {
            return AdBean.class;
        }

        @Override
        protected void afterParseData(Object object) {
            if(object !=null ){
                action1.call((AdBean) object);
            }else{
                action1.call(null);
            }
        }
    }



}
