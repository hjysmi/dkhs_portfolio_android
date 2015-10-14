package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by zjz on 2015/10/9.
 */
public class WalletsEngine {

    public final static String WeiXin = "weixinpay";
    public final static String Alipay = "alipay";
    public final static String YiBao = "yibao";

    ////  vendor (string, 第三方支付渠道，weixinpay,微信／alipay,支付宝／yibao,易宝)
    public static void payment(float amount, String vendor, ParseHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("amount", amount + "");
        params.addBodyParameter("vendor", vendor);
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.Wallets.payment, params, listener);
    }
}
