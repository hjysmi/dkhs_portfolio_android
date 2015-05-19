package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.MessageFormat;

/**
 * @author zwm
 * @version 2.0
 * @ClassName CombinationRngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/5/19.
 */
public class CombinationEngine {


    public void getCombinationBean(String id,IHttpListener listener){
        DKHSClient.request(HttpRequest.HttpMethod.GET, MessageFormat.format(DKHSUrl.Portfolio.portfolio_detail, id), null, listener);
    }
}
