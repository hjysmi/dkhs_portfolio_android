package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by xuetong on 2015/11/16.
 * 综合搜索
 */
public class SelectGeneralEngine {

    public void searchBygeneral(String params,IHttpListener listener){
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("q",params);
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Search.search_general,requestParams,listener);
    }


    public static void searchStockFund(String symbolType,String symbolSType,String key,int page,int pageSize,IHttpListener listener){
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("symbol_type",symbolType);
        if(!TextUtils.isEmpty(symbolSType)){
            requestParams.addQueryStringParameter("symbol_stype",symbolSType);
        }
        requestParams.addQueryStringParameter("page", String.valueOf(page));
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        requestParams.addQueryStringParameter("q",key);
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Search.search_symools,requestParams,listener);
    }


}
