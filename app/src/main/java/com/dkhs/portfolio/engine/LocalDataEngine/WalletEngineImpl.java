package com.dkhs.portfolio.engine.LocalDataEngine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by wuyongsen on 2015/10/12.
 */
public class WalletEngineImpl {
    public static void getWalletBalance( ParseHttpListener listener) {
        RequestParams params = new RequestParams();
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Wallet.account_info, params, listener);
    }
}
