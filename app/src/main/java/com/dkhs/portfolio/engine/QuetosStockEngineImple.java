/**
 * @Title QuetosStockEngineImple.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午6:11:27
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * @author zjz
 * @version 1.0
 * @ClassName QuetosStockEngineImple
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-18 下午6:11:27
 */
public class QuetosStockEngineImple extends LoadMoreDataEngine {

    private final static String EXCHANGE = "1,2";

    public final static String ORDER_INCREASE = "-percentage";
    public final static String ORDER_DOWN = "percentage";
    public final static String ORDER_TURNOVER = "-turnover_rate";

    private String orderType;

    public QuetosStockEngineImple(ILoadDataBackListener loadListener, String type) {
        super(loadListener);
        this.orderType = type;
    }

    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("exchange", "1,2");
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("symbol_type", "1");
        params.addQueryStringParameter("page_size", "50");
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.stocklist, params, this);
    }

    @Override
    public HttpHandler loadData() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("exchange", "1,2");
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("symbol_type", "1");
        params.addQueryStringParameter("page_size", "50");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.stocklist, params, this);

    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {

        MoreDataBean<StockPriceBean> dataMoreBean = new MoreDataBean.EmptyMoreBean();
        // List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        MoreDataBean<SelectStockBean> parseMoreBean = new MoreDataBean.EmptyMoreBean();

        try {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            dataMoreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<StockPriceBean>>() {
            }.getType());
            parseMoreBean.copyMoreDataBean(dataMoreBean);
            parseMoreBean.setResults(new ArrayList<SelectStockBean>());

            for (StockPriceBean priceBean : dataMoreBean.getResults()) {
                // selectList.add(SelectStockBean.copy(priceBean));
                parseMoreBean.getResults().add(SelectStockBean.copy(priceBean));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMoreBean;

        //
        // List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        // try {
        // JSONObject dataObject = new JSONObject(jsonData);
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
        // // SelectStockBean selectBean = new SelectStockBean();
        // // selectBean.id = stockBean.getId();
        // // selectBean.name = stockBean.getAbbrname();
        // // selectBean.currentValue = stockBean.getCurrent();
        // // selectBean.code = stockBean.getSymbol();
        // // selectBean.percentage = stockBean.getPercentage();
        // // selectBean.isFollowed = stockBean.isFollowed();
        // // selectBean.isStop = stockBean.isStop();
        // // selectBean.symbol_type = stockBean.getSymbol_type();
        // // selectList.add(selectBean);
        // selectList.add(SelectStockBean.copy(stockBean));
        //
        // }
        //
        // }
        //
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        //
        // return selectList;
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
        params.addQueryStringParameter("exchange", "1,2");
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("symbol_type", "1");
        params.addQueryStringParameter("page_size", dataSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.stocklist, params, this);

    }

}
