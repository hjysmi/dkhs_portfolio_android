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
import com.dkhs.portfolio.engine.FundDataEngine.OrderType;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName QuetosStockEngineImple
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-18 下午6:11:27
 */
public class MainIndexEngineImple extends LoadMoreDataEngine {

    // public static final String ORDER_BY_DAY = "chng_pct_day";
    public static final String ORDER_BY_YEAR = "-chng_pct_year";
    public static final String ORDER_BY_TYEAR = "-chng_pct_year_sofar";
    public static final String ORDER_BY_MONTH = "-chng_pct_month";
    // public static final String ORDER_BY_SEASON = "chng_pct_three_month";

    private String orderType = ORDER_BY_MONTH;

    public MainIndexEngineImple(ILoadDataBackListener loadListener) {
        super(loadListener);

    }

    public void setOrderType(OrderType orderType) {
        switch (orderType) {
            case YEAR:
                this.orderType = ORDER_BY_YEAR;
                break;
            case MONTH:
                this.orderType = ORDER_BY_MONTH;
                break;
            case TYEAR:
                this.orderType = ORDER_BY_TYEAR;
                break;

            default:
                break;
        }

    }

    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("symbol_type", "5");
        params.addQueryStringParameter("is_midx", "1");
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Fund.mainIndexList, params, this);

    }

    @Override
    public HttpHandler loadData() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("symbol_type", "5");
        params.addQueryStringParameter("is_midx", "1");
        params.addQueryStringParameter("sort", orderType);
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Fund.mainIndexList, params, this);

    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<StockPriceBean> dataMoreBean = new MoreDataBean.EmptyMoreBean();
        MoreDataBean<SelectStockBean> parseMoreBean = new MoreDataBean.EmptyMoreBean();

        List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        try {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            dataMoreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<StockPriceBean>>() {
            }.getType());
            parseMoreBean.copyMoreDataBean(dataMoreBean);
            parseMoreBean.setResults(new ArrayList<SelectStockBean>());

            for (StockPriceBean stockBean : dataMoreBean.getResults()) {
                // selectList.add(SelectStockBean.copy(priceBean));
                SelectStockBean selectBean = SelectStockBean.copy(stockBean);
                if (orderType.equalsIgnoreCase(ORDER_BY_YEAR)) {

                    selectBean.percentage = stockBean.getYearPercentage();
                } else if (orderType.equalsIgnoreCase(ORDER_BY_MONTH)) {

                    selectBean.percentage = stockBean.getMonthPercentage();
                } else if (orderType.equalsIgnoreCase(ORDER_BY_TYEAR)) {
                    selectBean.percentage = stockBean.getTyearPercentage();
                }

                parseMoreBean.getResults().add(selectBean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseMoreBean;

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
        // SelectStockBean selectBean = new SelectStockBean();
        // selectBean.id = stockBean.getId();
        // selectBean.name = stockBean.getAbbrname();
        // selectBean.currentValue = stockBean.getCurrent();
        // selectBean.code = stockBean.getSymbol();
        // // selectBean.percentage = stockBean.getPercentage();
        // selectBean.isFollowed = stockBean.isFollowed();
        //
        // if (orderType.equalsIgnoreCase(ORDER_BY_YEAR)) {
        //
        // selectBean.percentage = stockBean.getYearPercentage();
        // } else if (orderType.equalsIgnoreCase(ORDER_BY_MONTH)) {
        //
        // selectBean.percentage = stockBean.getMonthPercentage();
        // } else if (orderType.equalsIgnoreCase(ORDER_BY_TYEAR)) {
        // selectBean.percentage = stockBean.getTyearPercentage();
        // }
        //
        // selectList.add(selectBean);
        //
        // // results.add(stockBean);
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
        params.addQueryStringParameter("symbol_type", "5");
        params.addQueryStringParameter("is_midx", "1");
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", dataSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Fund.mainIndexList, params, this);

    }

}
