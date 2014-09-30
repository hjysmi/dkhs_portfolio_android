/**
 * @Title QuotesEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-26 下午4:59:15
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName QuotesEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-26 下午4:59:15
 * @version 1.0
 */
public class QuotesEngineImpl {

    public void quotes(String stockCode, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("symbols", stockCode);
        DKHSClient.requestByPost(DKHSUrl.StockSymbol.quotes, params, listener);
    }

    public void symbolfollow(long id, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("symbol", id + "");
        params.addBodyParameter("buy_in", "0");
        params.addBodyParameter("sell_out", "0");
        DKHSClient.requestByPost(DKHSUrl.StockSymbol.symbolfollow, params, listener);
    }

    public void delfollow(long id, IHttpListener listener) {
        DKHSClient.request(HttpMethod.POST, DKHSUrl.StockSymbol.unfollow + id + "/unfollow", null, listener);
    }

    public void queryTimeShare(String stockCode, IHttpListener listener) {
        DKHSClient.requestByGet(DKHSUrl.StockSymbol.sfthumbnail + stockCode, null, listener);

    }
}
