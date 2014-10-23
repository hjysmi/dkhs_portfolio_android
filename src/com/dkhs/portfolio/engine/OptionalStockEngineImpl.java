/**
 * @Title StockEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 上午10:27:07
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

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
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;

/**
 * @ClassName StockEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-18 上午10:27:07
 * @version 1.0
 */
public class OptionalStockEngineImpl extends LoadSelectDataEngine {

    public OptionalStockEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);

    }

    private int totalcount;

    private int totalpage;

    private int currentpage;
    private List<StockPriceBean> results = new ArrayList<StockPriceBean>();

    // /**
    // * 查询自选股
    // */
    // public void getOptionalList(IHttpListener listener) {
    //
    // }
    private String orderType;

    public void setLoadType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * 
     * 查询自选股
     * 
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loadData() {
        if (TextUtils.isEmpty(orderType)) {

            DKHSClient.requestByGet(DKHSUrl.StockSymbol.optional, null, this);
        } else {
            DKHSClient.requestByGet(DKHSUrl.StockSymbol.optional + "?sort=" + orderType, null, this);

        }
    }

    @Override
    protected List<SelectStockBean> parseDateTask(String jsonData) {
        List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        try {
            JSONObject dataObject = new JSONObject(jsonData);
            totalcount = dataObject.optInt("total_count");
            totalpage = dataObject.optInt("total_page");
            currentpage = dataObject.optInt("current_page");
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
                    selectBean.percentage = stockBean.getPercentage();
                    selectBean.percentage = stockBean.getPercentage();
                    selectBean.change = stockBean.getChange();
                    selectList.add(selectBean);

                    results.add(stockBean);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return selectList;

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        NameValuePair valuePair2 = new BasicNameValuePair("sort", orderType);
        params.add(valuePair);
        params.add(valuePair2);

        if (TextUtils.isEmpty(orderType)) {

            DKHSClient.requestByGet(DKHSUrl.StockSymbol.optional, null, params, this);

        } else {
            DKHSClient.requestByGet(DKHSUrl.StockSymbol.optional, null, params, this);

        }

    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public List<StockPriceBean> getResults() {
        return results;
    }

    public void setResults(List<StockPriceBean> results) {
        this.results = results;
    }

}
