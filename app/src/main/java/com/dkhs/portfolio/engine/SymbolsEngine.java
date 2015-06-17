package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * @author zwm
 * @version 2.0
 * @ClassName SymbolsEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/5.
 */
public class SymbolsEngine {


    public void getFundManagerInfo(String pk, IHttpListener listener) {

        RequestParams params = new RequestParams();

        DKHSClient.request(HttpRequest.HttpMethod.GET, String.format(DKHSUrl.Fund.managerInfo, pk), null, listener);

    }
}
