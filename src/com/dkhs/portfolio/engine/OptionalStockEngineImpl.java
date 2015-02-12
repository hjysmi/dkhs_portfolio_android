/**
 * @Title StockEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 上午10:27:07
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.StockUitls;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName StockEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-18 上午10:27:07
 * @version 1.0
 */
public class OptionalStockEngineImpl extends LoadSelectDataEngine {
    ILoadDataBackListener loadListener;

    public OptionalStockEngineImpl(ILoadDataBackListener loadListener, boolean isShowIndex) {
        super(loadListener);
        this.isShowIndex = isShowIndex;
        this.loadListener = loadListener;
    }

    private boolean isShowIndex;

    private int totalcount;

    private int totalpage;

    private int currentpage;
    // private List<StockPriceBean> results = new ArrayList<StockPriceBean>();

    // /**
    // * 查询自选股
    // */
    // public void getOptionalList(IHttpListener listener) {
    //
    // }
    private String orderType = "followed_at";

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
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);
    }

    @Override
    protected List<SelectStockBean> parseDateTask(String jsonData) {
        isLoading = false;
        List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        try {
            JSONObject dataObject = new JSONObject(jsonData);
            totalcount = dataObject.optInt("total_count");
            totalpage = dataObject.optInt("total_page");
            currentpage = dataObject.optInt("current_page");
            setStatu(dataObject.optInt("trade_status"));
            JSONArray resultsJsonArray = dataObject.optJSONArray("results");
            if (null != resultsJsonArray && resultsJsonArray.length() > 0) {
                int length = resultsJsonArray.length();

                for (int i = 0; i < length; i++) {
                    JSONObject stockObject = resultsJsonArray.optJSONObject(i);
                    StockPriceBean stockBean = DataParse.parseObjectJson(StockPriceBean.class, stockObject);

                    if (!isShowIndex) {

                        if (StockUitls.SYMBOLTYPE_STOCK.equalsIgnoreCase(stockBean.getSymbol_type())
                                && !stockBean.isStop()) {
                            // results.add(stockBean);
                            // selectList.add(selectBean);
                            selectList.add(SelectStockBean.copy(stockBean));
                        }
                    } else {
                        // selectList.add(selectBean);
                        selectList.add(SelectStockBean.copy(stockBean));
                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // if (orderType.equals("followed_at")) {
        // selectList = forIndex(selectList);
        // }
        return selectList;

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param errCode
     * @param errMsg
     * @return
     */
    @Override
    public void onFailure(int errCode, String errMsg) {
        // TODO Auto-generated method stub
        super.onFailure(errCode, errMsg);
        isLoading = false;
    }

    // public List<SelectStockBean> forIndex(List<SelectStockBean> datalist) {
    // List<SelectStockBean> tmp = new ArrayList<SelectStockBean>();
    // SelectStockBean sb;
    // int position;
    // while (datalist.size() > 0) {
    // for (int i = 0; i < datalist.size(); i++) {
    // sb = datalist.get(i);
    // position = i;
    // for (int j = i; j < datalist.size(); j++) {
    // if (sb.index < datalist.get(j).index) {
    // sb = datalist.get(j);
    // position = j;
    // }
    // }
    // datalist.remove(position);
    // tmp.add(sb);
    // break;
    // }
    // }
    // return tmp;
    // }

    public static HttpHandler setIndex(ParseHttpListener<List<SelectStockBean>> listener, String json) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("symbols_position", json);
        return DKHSClient.request(HttpMethod.POST, DKHSUrl.StockSymbol.index, params, listener);
    }

    public static HttpHandler loadAllData(IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, listener);

    }

    // public void loadAllData() {
    // RequestParams params = new RequestParams();
    // params.addQueryStringParameter("page", "1");
    // params.addQueryStringParameter("page_size", Integer.MAX_VALUE + "");
    // DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);
    //
    // }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public HttpHandler loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair);

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
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param dataSize
     * @return
     */
    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("page_size", dataSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.optional, params, this);

    }

    // public List<StockPriceBean> getResults() {
    // return results;
    // }
    //
    // public void setResults(List<StockPriceBean> results) {
    // this.results = results;
    // }

}
