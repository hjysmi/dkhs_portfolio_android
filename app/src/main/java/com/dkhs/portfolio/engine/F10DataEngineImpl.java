package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.MessageFormat;

/**
 * Created by zjz on 2015/5/13.
 */
public class F10DataEngineImpl {


    /**
     * 获取股本信息
     *
     * @param symbol 股票的code，如sz000001
     */
    public void getStockHoder(String symbol, IHttpListener listener) {
        DKHSClient.request(HttpRequest.HttpMethod.GET, MessageFormat.format(DKHSUrl.StockSymbol.F10_Holder, symbol), null, listener);
    }

    /**
     * 获取简况信息
     *
     * @param symbol 股票的code，如sz000001
     */
    public void getIntroduction(String symbol, IHttpListener listener) {
        DKHSClient.request(HttpRequest.HttpMethod.GET, MessageFormat.format(DKHSUrl.StockSymbol.F10_INTRODUCTION, symbol), null, listener);
    }

}
