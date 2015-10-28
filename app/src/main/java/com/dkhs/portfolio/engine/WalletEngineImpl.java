package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.TreeMap;

/**
 * Created by wuyongsen on 2015/10/12.
 */
public class WalletEngineImpl {
    public static void getWalletBalance( ParseHttpListener listener) {
        RequestParams params = new RequestParams();
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Wallets.rewards_balance, params, listener);
    }

    public static void getMineAccountInfo( ParseHttpListener listener){
        //加密请求我的钱包信息
        DKHSClient.requestGetByEncryp(new TreeMap<String, String>(), DKHSUrl.Wallets.account_info, listener);
    }
}
