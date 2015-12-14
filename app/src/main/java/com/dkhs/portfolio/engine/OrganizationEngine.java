package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by xuetong on 2015/12/11.
 */
public class OrganizationEngine {


    public void getOrg(IHttpListener listener, String type) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("verified_type", type);
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Org_profiles.Org_profiles_list, params, listener);
    }
}
