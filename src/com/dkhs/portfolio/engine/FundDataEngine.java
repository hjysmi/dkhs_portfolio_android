/**
 * @Title FundDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-9 下午2:59:04
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

import com.dkhs.portfolio.bean.FundsPriceBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;

/**
 * @ClassName FundDataEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-9 下午2:59:04
 * @version 1.0
 */
public class FundDataEngine extends LoadSelectDataEngine {

    private static final String ORDER_BY_DAY = "percent_day";
    private static final String ORDER_BY_MONTH = "percent_month";
    private static final String ORDER_BY_SEASON = "percent_season";

    public static final String TYPE_MAININDEX = "all";
    public static final String TYPE_INDEX = "zs";
    public static final String TYPE_STOCK = "gp";

    private OrderType mOrderType = OrderType.DAY;
    private String mFundsType = TYPE_MAININDEX;

    public enum OrderType {
        // 排序模式，日排行
        DAY(ORDER_BY_DAY),
        // 排序模式，月排行
        MONTH(ORDER_BY_MONTH),
        // 排序模式，季度排行
        QUARTER(ORDER_BY_SEASON);

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
    public void loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair);
        // DKHSClient.requestByGet(DKHSUrl.StockSymbol.stocklist + "?exchange=1,2&sort=" + orderType, null, params,
        // this);
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.Fund.fundsList, mFundsType, mOrderType.getType()), null,
                params, this);
    }

    @Override
    public void loadData() {
        
        DKHSClient.requestByGet(this, DKHSUrl.Fund.fundsList, mFundsType, mOrderType.getType());
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param jsonData
     * @return
     * @return
     */
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
                    FundsPriceBean stockBean = DataParse.parseObjectJson(FundsPriceBean.class, stockObject);
                    SelectStockBean selectBean = new SelectStockBean();
                    selectBean.id = stockBean.getId();
                    selectBean.name = stockBean.getName();
                    // selectBean.currentValue = stockBean.getCurrent();
                    selectBean.code = stockBean.getCode();
                    if (mOrderType == OrderType.DAY) {

                        selectBean.percentage = stockBean.getPercentDay();
                    } else if (mOrderType == OrderType.MONTH) {

                        selectBean.percentage = stockBean.getPercentMonth();
                    } else if (mOrderType == OrderType.QUARTER) {

                        selectBean.percentage = stockBean.getPercentSeason();
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
