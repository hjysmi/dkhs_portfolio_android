/**
 * @Title FundsOrderEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.Collections;
import java.util.List;

import android.text.TextUtils;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
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
public class FollowComListEngineImpl extends LoadMoreDataEngine {

    // 净值、涨幅、置顶序号
    public static final String ORDER_CUMULATIVE_UP = "chng_pct_day";
    public static final String ORDER_NET_VALUE_UP = "net_value";
    public static final String ORDER_CUMULATIVE_DOWN = "-chng_pct_day";
    public static final String ORDER_NET_VALUE_DOWN = "-net_value";
    public static final String ORDER_DEFALUT = "";

    private String userId;
    private String orderType = ORDER_DEFALUT;

    public FollowComListEngineImpl(ILoadDataBackListener loadListener, String usrId) {
        super(loadListener);
        this.userId = usrId;
    }

    public void setOrderType(String type) {
        this.orderType = type;
    }

    @Override
    public HttpHandler loadMore() {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("user_id", userId);
        }
        if (!TextUtils.isEmpty(orderType)) {
            params.addQueryStringParameter("sort", orderType);
        }
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        // List<NameValuePair> params = new ArrayList<NameValuePair>();
        // NameValuePair valuePair2 = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        // params.add(valuePair2);
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.following, params, this);
    }

    public HttpHandler loadAllData() {

        if (PortfolioApplication.hasUserLogin()) {

            RequestParams params = new RequestParams();
            params.addQueryStringParameter("page", "1");
            if (!TextUtils.isEmpty(orderType)) {
                params.addQueryStringParameter("sort", orderType);
            }
            params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
            return DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.following, params, this);

        } else {
            List<CombinationBean> comList = new VisitorDataEngine().getCombinationBySort();
            StringBuilder sbIds = new StringBuilder();
            if (null != comList && !comList.isEmpty()) {
                for (CombinationBean comBean : comList) {
                    sbIds.append(comBean.getId());
                    sbIds.append(",");
                }
                // sbIds = sbIds.substring(0, sbIds.length()-1);
                // System.out.println("datalist size:" + dataList.size());
                // getiLoadListener().loadFinish(dataList);
                if (null != sbIds && sbIds.length() > 1) {

                    System.out.println("ids:" + sbIds.substring(0, sbIds.length() - 1));

                    RequestParams params = new RequestParams();
                    if (!orderType.equals(ORDER_DEFALUT)) {
                        params.addQueryStringParameter("sort", orderType);
                    }
                    params.addQueryStringParameter("pks", sbIds.substring(0, sbIds.length() - 1));
                    return DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.following, params, this);
                }
            } else {
                MoreDataBean<CombinationBean> moreDatebean = new MoreDataBean<CombinationBean>();
                moreDatebean.setResults(Collections.EMPTY_LIST);
                getLoadListener().loadFinish(moreDatebean);
                // getiLoadListener().loadFail(null);
            }
        }
        return null;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("user_id", userId);
        }
        if (!TextUtils.isEmpty(orderType)) {
            params.addQueryStringParameter("sort", orderType);
        }
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.following, params, this);
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

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param dataSize
     * @return
     */
    @Override
    public HttpHandler refreshDatabySize(int dataSize) {

        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("user_id", userId);
        }
        if (!TextUtils.isEmpty(orderType)) {
            params.addQueryStringParameter("sort", orderType);
        }
        params.addQueryStringParameter("page_size", dataSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.following, params, this);
    }

}
