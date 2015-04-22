/**
 * @Title FollowComEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-10 下午1:17:33
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.text.MessageFormat;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName FollowComEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-3-10 下午1:17:33
 * @version 1.0
 */
public class FollowComEngineImpl {

    public void followCombinations(String ids, IHttpListener listener) {

        DKHSClient.request(HttpMethod.POST, MessageFormat.format(DKHSUrl.Portfolio.addFollow, ids), null, listener);

    }

    public void defFollowCombinations(String ids, IHttpListener listener) {

        DKHSClient.request(HttpMethod.POST, MessageFormat.format(DKHSUrl.Portfolio.delFollow, ids), null, listener);

    }

    public void sortCombinations(String ids, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("portfolios_position", ids);
        DKHSClient.request(HttpMethod.POST, DKHSUrl.Portfolio.sort, params, listener);

    }

}
