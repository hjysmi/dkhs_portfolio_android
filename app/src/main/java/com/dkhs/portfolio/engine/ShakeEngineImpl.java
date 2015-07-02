package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * @author zwm
 * @version 2.0
 * @ClassName AdEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/18.
 */
public class ShakeEngineImpl {


    public static  void getShakeInfo(  IHttpListener listener) {
        DKHSClient.request(HttpRequest.HttpMethod.GET,DKHSUrl.Shake.getShakeInfo, null, listener);
    }



}
