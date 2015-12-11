package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by zhangcm on 2015/12/8.
 */
public class FundHomeEngineImpl {
    public void getMarketInfo(IHttpListener listener){
        DKHSClient.requestByGet(listener,DKHSUrl.StockSymbol.quotes,"SH000001,SZ399001,SZ399006");
    }
    public void getRecommendBanners(IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("content_type","50");
        params.addQueryStringParameter("content_subtype","501");
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Status.get_recommend, params, listener);
    }
    public void getRecommendSpecials(IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("content_type","50");
        params.addQueryStringParameter("content_subtype","502");
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Status.get_recommend, params, listener);
    }
    public void getRecommendSpecialFinancings(IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("content_type","50");
        params.addQueryStringParameter("content_subtype", "503");
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Status.get_recommend, params, listener);
    }
    /**
     * 获取推荐基金经理
     *
     * @param listener
     */
    public void getRecommendFundManager(IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page_size", "3");
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.StockSymbol.RECOMMEND_FUND_MANAGER, params, listener);
    }
}
