package com.dkhs.portfolio.engine;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MarketCenterStockEngineImple extends LoadMoreDataEngine {
    private final static String EXCHANGE = "1,2";

    public final static String ORDER_INCREASE = "-percentage";
    public final static String ORDER_DOWN = "percentage";
    public final static String ORDER_TURNOVER = "-turnover_rate";

    // public final static String ORDER_TURNOVER = "amplitude";
    public final static String ACE = "percentage";
    public final static String DESC = "-percentage";
    public final static String CURRENT = "";
    private String orderType;
    ILoadDataBackListener loadListener;
    private int Status;
    private int mPagesize = 50;

    public MarketCenterStockEngineImple(ILoadDataBackListener loadListener, String type, int pageSize) {
        super(loadListener);
        this.orderType = type;
        this.loadListener = loadListener;
        this.mPagesize = pageSize;
    }

    public MarketCenterStockEngineImple(ILoadDataBackListener loadListener, String type) {
        super(loadListener);
        this.orderType = type;
        this.loadListener = loadListener;
    }

    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", mPagesize + "");
        params.addQueryStringParameter("is_midx", "1");
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.marketcenter, params, this);

        // List<NameValuePair> params = new ArrayList<NameValuePair>();
        // NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        // params.add(valuePair);
        // return DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.marketcenter + "&page="
        // + (getCurrentpage() + 1), orderType, mPagesize), null, this);
    }

    @Override
    public HttpHandler loadData() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", mPagesize + "");
        params.addQueryStringParameter("is_midx", "1");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.marketcenter, params, this);

        // return DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.marketcenter, orderType, mPagesize),
        // null, this);

    }

    public HttpHandler loadDataFromCurrent(int num) {
        // return DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.marketcenter, orderType, num), null,
        // this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("sort", orderType);
        // params.addQueryStringParameter("page_size",);
        params.addQueryStringParameter("is_midx", "1");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.marketcenter, params, this);

    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<StockPriceBean> dataMoreBean = new MoreDataBean.EmptyMoreBean();
        MoreDataBean<SelectStockBean> parseMoreBean = new MoreDataBean.EmptyMoreBean();
        try {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            dataMoreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<StockPriceBean>>() {
            }.getType());
            parseMoreBean.copyMoreDataBean(dataMoreBean);
            parseMoreBean.setResults(new ArrayList<SelectStockBean>());

            for (StockPriceBean stockBean : dataMoreBean.getResults()) {
                // selectList.add(SelectStockBean.copy(priceBean));
                SelectStockBean s = SelectStockBean.copy(stockBean);
                s.setStatus(dataMoreBean.getStatu());
                parseMoreBean.getResults().add(s);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseMoreBean;

        // List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        // JSONObject dataObject = null;
        // try {
        // dataObject = new JSONObject(jsonData);
        // setTotalcount(dataObject.optInt("total_count"));
        // setTotalpage(dataObject.optInt("total_page"));
        // setCurrentpage(dataObject.optInt("current_page"));
        // setStatu(dataObject.optInt("trade_status"));
        // JSONArray resultsJsonArray = dataObject.optJSONArray("results");
        // if (null != resultsJsonArray && resultsJsonArray.length() > 0) {
        // int length = resultsJsonArray.length();
        //
        // for (int i = 0; i < length; i++) {
        // JSONObject stockObject = resultsJsonArray.optJSONObject(i);
        // StockPriceBean stockBean = DataParse.parseObjectJson(StockPriceBean.class, stockObject);
        // SelectStockBean s = SelectStockBean.copy(stockBean);
        // s.setStatus(dataObject.optInt("trade_status"));
        // selectList.add(s);
        //
        // // results.add(stockBean);
        //
        // }
        //
        // }
        //
        // } catch (JSONException e) {
        // e.printStackTrace();
        // } finally {
        // // loadListener.setStatu(dataObject.optInt("trade_status"));
        // }
        //
        // return selectList;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param dataSize
     * @return
     */
    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        // TODO Auto-generated method stub
        return null;
        // params.addQueryStringParameter("page_size", dataSize + "");

    }

}
