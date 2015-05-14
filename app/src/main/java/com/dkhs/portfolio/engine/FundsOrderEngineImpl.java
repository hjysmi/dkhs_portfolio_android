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

import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName FundsOrderEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version 1.0
 */
public class FundsOrderEngineImpl extends LoadMoreDataEngine {

    public FundsOrderEngineImpl(ILoadDataBackListener loadListener, String orderType) {
        super(loadListener);
        this.mOrderType = orderType;
    }

    public static final String ORDER_DAY = "-chng_pct_day";
    public static final String ORDER_WEEK = "-chng_pct_week";
    public static final String ORDER_MONTH = "-chng_pct_month";
    // public static final String ORDER_SEASON = "-chng_pct_three_month";
    public static final String ORDER_ALL = "-net_value";

    private String mOrderType = ORDER_WEEK;
    private int pageIndex = 1;
    private int pageSize = 99;

    @Override
    public HttpHandler loadMore() {
        // List<NameValuePair> params = new ArrayList<NameValuePair>();
        // NameValuePair valuePair = new BasicNameValuePair("sort", mOrderType);
        // params.add(valuePair);
        // NameValuePair valuePair2 = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        // params.add(valuePair2);
        // return DKHSClient.requestByGet(DKHSUrl.Portfolio.rankingList, null, params, this);
        //

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("sort", mOrderType);
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.rankingList, params, this);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public HttpHandler loadData() {
        // List<NameValuePair> params = new ArrayList<NameValuePair>();
        // NameValuePair valuePair = new BasicNameValuePair("sort", mOrderType);
        // params.add(valuePair);
        // return DKHSClient.requestByGet(DKHSUrl.Portfolio.rankingList, null, params, this);
        //
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("sort", mOrderType);
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.rankingList, params, this);

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
        if (!TextUtils.isEmpty(jsonData)) {

            try {

                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

                moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<CombinationBean>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param dataSize
     * @return
     */
    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        // List<NameValuePair> params = new ArrayList<NameValuePair>();
        // NameValuePair valuePair = new BasicNameValuePair("sort", mOrderType);
        // params.add(valuePair);
        // NameValuePair valuePair2 = new BasicNameValuePair("page_size", dataSize + "");
        // params.add(valuePair2);
        // return DKHSClient.requestByGet(DKHSUrl.Portfolio.rankingList, null, params, this);
        //

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("sort", mOrderType);
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.rankingList, params, this);

    }

}