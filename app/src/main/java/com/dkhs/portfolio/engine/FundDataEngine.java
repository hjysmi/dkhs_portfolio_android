/**
 * @Title FundDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-9 下午2:59:04
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
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
 * @ClassName FundDataEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-9 下午2:59:04
 * @version 1.0
 */
public class FundDataEngine extends LoadMoreDataEngine {

    // private static final String ORDER_BY_DAY = "percent_day";
    private static final String ORDER_BY_MONTH = "-percent_month";
    private static final String ORDER_BY_YEAR = "-percent_year";
    private static final String ORDER_BY_TYEAR = "-percent_tyear";
    // private static final String ORDER_BY_SEASON = "percent_season";

    public static final String TYPE_MAININDEX = "all";
    public static final String TYPE_INDEX = "zs";
    public static final String TYPE_STOCK = "gp";

    private OrderType mOrderType = OrderType.MONTH;
    private String mFundsType = TYPE_MAININDEX;

    public enum OrderType {
        // 排序模式，日排行
        // DAY(ORDER_BY_DAY),
        // 排序模式，月排行
        MONTH(ORDER_BY_MONTH), YEAR(ORDER_BY_YEAR),
        // 排序模式，季度排行
        // QUARTER(ORDER_BY_SEASON);
        TYEAR(ORDER_BY_TYEAR);

        private String type;

        OrderType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public FundDataEngine(ILoadDataBackListener loadListener, String fundType) {
        super(loadListener);
        this.mFundsType = fundType;

    }

    public void setOrderType(OrderType orderType) {
        this.mOrderType = orderType;
    }

    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", mFundsType);
        params.addQueryStringParameter("sort", mOrderType.getType());
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Fund.fundsList, params, this);

    }

    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", mFundsType);
        params.addQueryStringParameter("sort", mOrderType.getType());
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Fund.fundsList, params, this);

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
        MoreDataBean<FundPriceBean> dataMoreBean = new MoreDataBean.EmptyMoreBean();
        MoreDataBean<SelectStockBean> parseMoreBean = new MoreDataBean.EmptyMoreBean();

        try {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            dataMoreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<FundPriceBean>>() {
            }.getType());

            parseMoreBean.copyMoreDataBean(dataMoreBean);
            parseMoreBean.setResults(new ArrayList<SelectStockBean>());
            for (FundPriceBean priceBean : dataMoreBean.getResults()) {
                SelectStockBean selectBean = new SelectStockBean();
                selectBean.id = priceBean.getId();
                selectBean.name = priceBean.getAbbrname();
                // selectBean.currentValue = stockBean.getCurrent();
                selectBean.code = priceBean.getCode();
                if (mOrderType == OrderType.YEAR) {

                    selectBean.percentage = priceBean.getPercent_year();
                } else if (mOrderType == OrderType.MONTH) {

                    selectBean.percentage = priceBean.getPercent_month();
                } else if (mOrderType == OrderType.TYEAR) {

                    selectBean.percentage = priceBean.getPercent_tyear();
                }
                parseMoreBean.getResults().add(selectBean);

            }

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
            // FundPriceBean stockBean = DataParse.parseObjectJson(FundPriceBean.class, stockObject);
            // SelectStockBean selectBean = new SelectStockBean();
            // selectBean.id = stockBean.getId();
            // selectBean.name = stockBean.getName();
            // // selectBean.currentValue = stockBean.getCurrent();
            // selectBean.code = stockBean.getCode();
            // if (mOrderType == OrderType.YEAR) {
            //
            // selectBean.percentage = stockBean.getPercentYear();
            // } else if (mOrderType == OrderType.MONTH) {
            //
            // selectBean.percentage = stockBean.getPercentMonth();
            // } else if (mOrderType == OrderType.TYEAR) {
            //
            // selectBean.percentage = stockBean.getPercentTYear();
            // }
            // selectList.add(selectBean);
            //
            // // results.add(stockBean);
            //
            // }
            //
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // return selectList;
        return parseMoreBean;
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
        params.addQueryStringParameter("type", mFundsType);
        params.addQueryStringParameter("sort", mOrderType.getType());
        params.addQueryStringParameter("page_size", dataSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Fund.fundsList, params, this);

    }

}
