/**
 * @Title CompareEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-9 下午6:15:07
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.MessageFormat;

/**
 * @author zjz
 * @version 1.0
 * @ClassName CompareEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-9 下午6:15:07
 */
public class CompareEngine {

    public void compare(IHttpListener listener, String ids, String startDay, String endDay) {
        DKHSClient.requestByGet(listener, DKHSUrl.Fund.compare, ids, startDay, endDay);
    }

    public void compareByPeriod(IHttpListener listener, String ids, String periodValue) {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("period", periodValue);
        DKHSClient.request(HttpRequest.HttpMethod.GET, MessageFormat.format(DKHSUrl.Fund.compareByPeriod, ids), params, listener);

    }

}
