package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

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


    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }


    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        // TODO Auto-generated method stub
        return null;

    }


    public static HttpHandler loadAllMarkets(IHttpListener listener) {
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.allMarkets, null, listener);
    }

}
