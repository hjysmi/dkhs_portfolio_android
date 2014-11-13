/**
 * @Title FundsOrderEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName FundsOrderEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version 1.0
 */
public class UserCombinationEngineImpl extends LoadMoreDataEngine {

    private String userId;

    public UserCombinationEngineImpl(ILoadDataBackListener loadListener, String usrId) {
        super(loadListener);
        this.userId = usrId;
    }

    @Override
    public void loadMore() {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("user_id", userId);
        }
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        // List<NameValuePair> params = new ArrayList<NameValuePair>();
        // NameValuePair valuePair2 = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        // params.add(valuePair2);
        DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.portfolio, params, this);
    }

    public void loadAllData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
        DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.portfolio, params, this);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loadData() {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("user_id", userId);
        }
        DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.portfolio, params, this);
        // DKHSClient.requestByGet(DKHSUrl.Portfolio.portfolio, null, null, this);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param jsonData
     * @return
     * @return
     */
    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<CombinationBean> moreBean = null;
        try {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<CombinationBean>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return moreBean;

    }

}
