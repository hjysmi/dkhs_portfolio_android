package com.dkhs.portfolio.engine;

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


}
