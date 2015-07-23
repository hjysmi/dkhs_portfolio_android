package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.TreeMap;

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

    public static void recharge(int amount, ParseHttpListener listener) {
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("package_amount", amount + "");
//        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.FlowExchange.recharge, params, listener);
//

        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
        paramsMap.put("package_amount", amount + "");
        DKHSClient.requestPostByEncryp(paramsMap, DKHSUrl.FlowExchange.recharge, listener.openEncry());

    }

    public static void invitecode(String code, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("code", code);
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.FlowExchange.invitecode, params, listener);

    }

}
