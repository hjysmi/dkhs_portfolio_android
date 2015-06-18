package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by zjz on 2015/6/18.
 */
public class FlowExchangeEngine {

    public static void overview(IHttpListener listener) {

        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.FlowExchange.overview, null, listener);

    }

    public static void packages(IHttpListener listener) {

        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.FlowExchange.packages, null, listener);

    }

}
