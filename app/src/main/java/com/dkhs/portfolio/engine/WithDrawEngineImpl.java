package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.TreeMap;

/**
 * Created by wuyongsen on 2015/10/19.
 */
public class WithDrawEngineImpl {
    private final static String AMOUNT = "amount";
    private final static String ACCOUNT = "vendor_user_account";
    private final static String NAME = "vendor_user_name";
    private final static String CAPTCHA = "captcha";
    private final static String VENDOR = "vendor";

    public static void withDraw(String amount, String account, String name,
                                String captcha, ParseHttpListener listener) {
        //加密请求
        TreeMap<String, String> paramsMap = new TreeMap<>();
        paramsMap.put(AMOUNT, amount);
        paramsMap.put(ACCOUNT, account);
        paramsMap.put(NAME, name);
        paramsMap.put(CAPTCHA, captcha);
        paramsMap.put(VENDOR, "alipay");
        DKHSClient.requestPostByEncryp(null, DKHSUrl.Wallets.withdraw, listener.openEncry());
    }
}
