/**
 * @Title QuetosStockEngineImple.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午6:11:27
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

import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.engine.FundDataEngine.OrderType;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;

/**
 * @ClassName QuetosStockEngineImple
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-18 下午6:11:27
 * @version 1.0
 */
public class MainIndexEngineImple extends LoadSelectDataEngine {

    public static final String ORDER_BY_DAY = "chng_pct_day";
    public static final String ORDER_BY_MONTH = "chng_pct_month";
    public static final String ORDER_BY_SEASON = "chng_pct_three_month";

    private String orderType = ORDER_BY_DAY;

    public MainIndexEngineImple(ILoadDataBackListener loadListener) {
        super(loadListener);

    }

    public void setOrderType(OrderType orderType) {
        switch (orderType) {
            case DAY:
                this.orderType = ORDER_BY_DAY;
                break;
            case MONTH:
                this.orderType = ORDER_BY_MONTH;
                break;
            case QUARTER:
                this.orderType = ORDER_BY_SEASON;
                break;

            default:
                break;
        }

    }

    @Override
    public void loadMore() {
        // int pageIndex = getCurrentpage() + 1;
        // DKHSClient.requestByGet(DKHSUrl.StockSymbol.stocklist, new String[] { EXCHANGE, orderType },new String[]
        // this);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair);
        // DKHSClient.requestByGet(DKHSUrl.StockSymbol.stocklist + "?exchange=1,2&sort=" + orderType, null, params,
        // this);
        // DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.stocklist+"&page="+, "1,2", orderType),
        // null, params,
        // this);

        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.Fund.mainIndexList, orderType) + "&page="
                + (getCurrentpage() + 1), null, this);
    }

    @Override
    public void loadData() {

        // DKHSClient.requestByGet(DKHSUrl.StockSymbol.stocklist+"?exchange=1,2&sort="+orderType, null, this);
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.Fund.mainIndexList, orderType), null, this);

    }

    @Override
    protected List<SelectStockBean> parseDateTask(String jsonData) {
        List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        try {
            JSONObject dataObject = new JSONObject(jsonData);
            setTotalcount(dataObject.optInt("total_count"));
            setTotalpage(dataObject.optInt("total_page"));
            setCurrentpage(dataObject.optInt("current_page"));
            JSONArray resultsJsonArray = dataObject.optJSONArray("results");
            if (null != resultsJsonArray && resultsJsonArray.length() > 0) {
                int length = resultsJsonArray.length();

                for (int i = 0; i < length; i++) {
                    JSONObject stockObject = resultsJsonArray.optJSONObject(i);
                    StockPriceBean stockBean = DataParse.parseObjectJson(StockPriceBean.class, stockObject);
                    SelectStockBean selectBean = new SelectStockBean();
                    selectBean.id = stockBean.getId();
                    selectBean.name = stockBean.getAbbrname();
                    selectBean.currentValue = stockBean.getCurrent();
                    selectBean.code = stockBean.getSymbol();
                    // selectBean.percentage = stockBean.getPercentage();
                    selectBean.isFollowed = stockBean.isFollowed();

                    if (orderType.equalsIgnoreCase(ORDER_BY_DAY)) {

                        selectBean.percentage = stockBean.getDayPercentage();
                    } else if (orderType.equalsIgnoreCase(ORDER_BY_MONTH)) {

                        selectBean.percentage = stockBean.getMonthPercentage();
                    } else if (orderType.equalsIgnoreCase(ORDER_BY_SEASON)) {

                        selectBean.percentage = stockBean.getSeasonPercentage();
                    }

                    selectList.add(selectBean);

                    // results.add(stockBean);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return selectList;
    }

}