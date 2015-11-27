package com.dkhs.portfolio.engine;

import android.content.Context;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class HomePageEngine {
    private Context mContext;

    public HomePageEngine(Context context) {
        mContext = context;
    }

    /**
     * 获取广告栏信息
     *
     * @param listener
     */
    public static void getBanner(IHttpListener listener) {
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Ads.GET_HOME_BANNER, null, listener);
    }

    /**
     * 获取子广告栏信息
     */
    public static void getSubBanner(IHttpListener listener){
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Ads.GET_SUB_HOME_BANNER, null, listener);
    }


    /**
     * 获取推荐悬赏/话题
     *
     * @param type     0话题40悬赏
     * @param listener
     */
    public static void getRecommendRewardAndTopic(String type, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("content_type", type);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("page_size", "6");
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Status.GET_RECOMMEND, params, listener);
    }

    /**
     * 获取推荐基金经理
     *
     * @param listener
     */
    public static void getRecommendFundManager(IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page_size", "3");
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.StockSymbol.RECOMMEND_FUND_MANAGER, params, listener);
    }

    /**
     * 获取推荐基金
     *
     * @param listener
     */
    public static void getRecommendFund(IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page_size", "2");
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.StockSymbol.RECOMMEND_FUND, params, listener);
    }

    /**
     * 获取推荐组合
     * @param listener
     */
    public static void getRecommendPortfolio(IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page_size", "3");
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Portfolio.RECOMMEND_PROTFOLIO, params, listener);
    }
}
