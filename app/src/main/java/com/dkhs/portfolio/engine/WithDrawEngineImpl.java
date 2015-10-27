package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by wuyongsen on 2015/10/19.
 */
public class WithDrawEngineImpl {
    private final static String AMOUNT = "amount";
    private final static String ACCOUNT = "vendor_user_account";
    private final static String NAME = "vendor_user_name";
    private final static String CAPTCHA = "captcha";
    private final static String VENDOR = "vendor";
    public static void withDraw(String amount,String account,String name,
                                String captcha,ParseHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter(AMOUNT,amount);
        params.addBodyParameter(ACCOUNT,account);
        params.addBodyParameter(NAME,name);
        params.addBodyParameter(CAPTCHA,captcha);
        params.addBodyParameter(VENDOR,"alipay");
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.Wallet.withdraw, params, listener);
    }
}
