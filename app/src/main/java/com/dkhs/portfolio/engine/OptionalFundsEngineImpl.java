/**
 * @Title StockEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 上午10:27:07
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName StockEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-18 上午10:27:07
 */
public class OptionalFundsEngineImpl extends LoadMoreDataEngine {
    // ILoadDataBackListener loadListener;

    private String mUserId;

    public OptionalFundsEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);
    }

    public OptionalFundsEngineImpl(ILoadDataBackListener loadListener, String userId) {
        super(loadListener);
        this.mUserId = userId;
    }


    // private List<StockPriceBean> results = new ArrayList<StockPriceBean>();

    // /**
    // * 查询自选股
    // */
    // public void getOptionalList(IHttpListener listener) {
    //
    // }
    private static final String DEF_ORDER_TYPE = "";
    private String orderType = DEF_ORDER_TYPE;

    public void setLoadType(String orderType) {
        this.orderType = orderType;
    }

    private boolean isLoading;

    @Override
    public HttpHandler loadData() {

        if (isLoading) {
            return null;
        }
        isLoading = true;
        if (!TextUtils.isEmpty(mUserId)) {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("page", "1");
            if (!orderType.equals(DEF_ORDER_TYPE)) {
                params.addQueryStringParameter("sort", orderType);
            }
            params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
            params.addQueryStringParameter("symbol_type", 3 + "");
            params.addQueryStringParameter("user_id", mUserId);

            return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);
        } else if (PortfolioApplication.hasUserLogin()) {

            RequestParams params = new RequestParams();
            params.addQueryStringParameter("page", "1");
            if (!orderType.equals(DEF_ORDER_TYPE)) {
                params.addQueryStringParameter("sort", orderType);
            }
            params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
            params.addQueryStringParameter("symbol_type", 3 + "");
            if (!TextUtils.isEmpty(mUserId)) {
                params.addQueryStringParameter("user_id", mUserId);
            }
            return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);
        } else if (null != getLoadListener()) {
            List<SelectStockBean> dataList = new VisitorDataEngine().getOptionalFundsSort();
            StringBuilder sbIds = new StringBuilder();
            if (null != dataList) {
                for (SelectStockBean stock : dataList) {
                    sbIds.append(stock.code);
                    sbIds.append(",");
                }

                if (null != sbIds && sbIds.length() > 1) {

                    Log.i("OptionalFundsEngineImpl", "ids:" + sbIds.substring(0, sbIds.length() - 1));

                    RequestParams params = new RequestParams();
                    if (!orderType.equals(DEF_ORDER_TYPE)) {
                        params.addQueryStringParameter("sort", orderType);
                    }
                    params.addQueryStringParameter("symbol_type", 3 + "");
                    params.addQueryStringParameter("page_size", dataList.size() + "");
                    params.addQueryStringParameter("symbols", sbIds.substring(0, sbIds.length() - 1));
                    return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);
                } else {
                    getLoadListener().loadFinish(new MoreDataBean.EmptyMoreBean());
                    isLoading = false;
                }
            } else {
                getLoadListener().loadFinish(new MoreDataBean.EmptyMoreBean());

                isLoading = false;

            }
        }

        return null;


    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {

        isLoading = false;

        MoreDataBean<FundPriceBean> dataMoreBean = new MoreDataBean.EmptyMoreBean();
        MoreDataBean<SelectStockBean> parseMoreBean = new MoreDataBean.EmptyMoreBean();

        try {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            dataMoreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<FundPriceBean>>() {
            }.getType());
            //
            parseMoreBean.copyMoreDataBean(dataMoreBean);
            parseMoreBean.setResults(new ArrayList<SelectStockBean>());

            for (FundPriceBean priceBean : dataMoreBean.getResults()) {
                parseMoreBean.getResults().add(SelectStockBean.copy(priceBean));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseMoreBean;

    }

    /**
     * @param errCode
     * @param errMsg
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onFailure(int errCode, String errMsg) {
        // TODO Auto-generated method stub
        super.onFailure(errCode, errMsg);
        isLoading = false;
    }


    public static HttpHandler setIndex(ParseHttpListener<List<SelectStockBean>> listener, String json) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("symbols_position", json);
        params.addBodyParameter("symbol_type", String.valueOf(3));
        return DKHSClient.request(HttpMethod.POST, DKHSUrl.StockSymbol.index, params, listener);
    }

    public static HttpHandler loadAllData(IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("symbol_type", 3 + "");
        params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
//        if (!TextUtils.isEmpty(mUserId)) {
//            params.addQueryStringParameter("user_id", mUserId);
//        }
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, listener);

    }

    public static HttpHandler loadAllDataByUserId(IHttpListener listener, String userId) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("symbol_type", 3 + "");
        params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("user_id", userId);
        }
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, listener);

    }


    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public HttpHandler loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");

        params.add(valuePair);
        NameValuePair typeValue = new BasicNameValuePair("symbol_type", 3 + "");

        params.add(typeValue);
        if (!TextUtils.isEmpty(mUserId)) {
            NameValuePair valuePair_uId = new BasicNameValuePair("user_id", mUserId);
            params.add(valuePair_uId);
        }

        if (TextUtils.isEmpty(orderType)) {
            NameValuePair valuePair2 = new BasicNameValuePair("sort", "followed_at");
            params.add(valuePair2);
            return DKHSClient.requestByGet(DKHSUrl.StockSymbol.optional, null, params, this);

        } else {
            NameValuePair valuePair2 = new BasicNameValuePair("sort", orderType);
            params.add(valuePair2);
            return DKHSClient.requestByGet(DKHSUrl.StockSymbol.optional, null, params, this);

        }

    }

    /**
     * @param dataSize
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("page_size", dataSize + "");
        params.addQueryStringParameter("symbol_type", 3 + "");
        if (!TextUtils.isEmpty(mUserId)) {
            params.addQueryStringParameter("user_id", mUserId);
        }
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);

    }


}
