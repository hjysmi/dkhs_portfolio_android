/**
 * @Title QuetosStockEngineImple.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午6:11:27
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
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
public class QuetosStockEngineImple extends LoadSelectDataEngine {

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
    public void loadMore() {
        // int pageIndex = getCurrentpage() + 1;
        // DKHSClient.requestByGet(DKHSUrl.StockSymbol.stocklist, new String[] { EXCHANGE, orderType },new String[]
        // this);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair);
        DKHSClient.requestByGet(DKHSUrl.StockSymbol.stocklist, new String[] { EXCHANGE, orderType }, params, this);
    }

    @Override
    public void loadData() {

        DKHSClient.requestByGet(DKHSUrl.StockSymbol.stocklist, new String[] { EXCHANGE, orderType }, this);

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
                    selectBean.percentage = stockBean.getPercentage();
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
