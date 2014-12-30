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
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class OpitionCenterStockEngineImple extends LoadSelectDataEngine {

    private final static String VALUE_EXCHANGE = "1,2";
    private final static String VALUE_SYMBOL_TYPE = "1";
    private final static String VALUE_SYMBOL_STYPE = "101";

    public final static String ORDER_INCREASE = "-percentage";
    public final static String ORDER_DOWN = "percentage";
    public final static String ORDER_TURNOVER = "-turnover_rate";
    public final static String ORDER_TURNOVER_DOWN = "turnover_rate";
    public final static String ORDER_AMPLITU = "-amplitude";
    public final static String ORDER_AMPLITU_DOWN = "amplitude";

    private String orderType;
    private StockViewType mStockType;
    private String mSectorId;
    private int mPageSize = 50;

    public OpitionCenterStockEngineImple(ILoadDataBackListener loadListener, StockViewType type) {
        super(loadListener);
        this.mStockType = type;
        getOrderType(type);
    }

    public OpitionCenterStockEngineImple(ILoadDataBackListener loadListener, StockViewType type, String sectorId) {
        super(loadListener);
        this.mStockType = type;
        this.mSectorId = sectorId;
        getOrderType(type);
    }

    public OpitionCenterStockEngineImple(ILoadDataBackListener loadListener, StockViewType type, int pagesize) {
        super(loadListener);
        this.mStockType = type;
        this.mPageSize = pagesize;
        getOrderType(type);
    }

    private void getOrderType(StockViewType type) {
        switch (type) {
            case MARKET_STOCK_TURNOVER:
            case STOCK_HANDOVER: {
                orderType = ORDER_TURNOVER;
            }
                break;
            case STOCK_DRAWDOWN:
            case MARKET_STOCK_DOWNRATIO: {
                orderType = ORDER_DOWN;
            }
                break;
            case STOCK_INCREASE:
            case MARKET_STOCK_UPRATIO: {
                orderType = ORDER_INCREASE;
            }
                break;
            case MARKET_STOCK_AMPLIT: {
                orderType = ORDER_AMPLITU;
            }
                break;
            case MARKET_STOCK_AMPLIT_ACE: {
                orderType = ORDER_AMPLITU_DOWN;
            }
                break;
            case MARKET_STOCK_TURNOVER_ACE: {
                orderType = ORDER_TURNOVER_DOWN;
            }
                break;
            case MARKET_PLATE_LIST: {
                orderType = ORDER_INCREASE;
            }
                break;
            case MARKET_PLATE_LIST_ACE: {
                orderType = ORDER_DOWN;
            }
                break;

            default:
                break;
        }
    }

    @Override
    public void loadMore() {

        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mSectorId)) {
            params.addQueryStringParameter("sector_id", mSectorId);

        }
        params.addQueryStringParameter("exchange", VALUE_EXCHANGE);
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", mPageSize + "");
        params.addQueryStringParameter("symbol_type", VALUE_SYMBOL_TYPE);
        params.addQueryStringParameter("symbol_stype", VALUE_SYMBOL_STYPE);
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.opitionmarket, params, this);

    }

    @Override
    public void loadData() {

        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mSectorId)) {
            params.addQueryStringParameter("sector_id", mSectorId);

        }
        params.addQueryStringParameter("exchange", VALUE_EXCHANGE);
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", mPageSize + "");
        params.addQueryStringParameter("symbol_type", VALUE_SYMBOL_TYPE);
        params.addQueryStringParameter("symbol_stype", VALUE_SYMBOL_STYPE);

        DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.opitionmarket, params, this);

    }

    // public void loadDataFromCurrent(int num) {
    // // DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.opitionmarket, orderType, num), null, this);
    //
    // }

    @Override
    protected List<SelectStockBean> parseDateTask(String jsonData) {
        List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        try {
            JSONObject dataObject = new JSONObject(jsonData);
            setTotalcount(dataObject.optInt("total_count"));
            setTotalpage(dataObject.optInt("total_page"));
            setCurrentpage(dataObject.optInt("current_page"));
            setStatu(dataObject.optInt("trade_status"));
            JSONArray resultsJsonArray = dataObject.optJSONArray("results");
            if (null != resultsJsonArray && resultsJsonArray.length() > 0) {
                int length = resultsJsonArray.length();

                for (int i = 0; i < length; i++) {
                    JSONObject stockObject = resultsJsonArray.optJSONObject(i);
                    StockPriceBean stockBean = DataParse.parseObjectJson(StockPriceBean.class, stockObject);

                    if (!TextUtils.isEmpty(orderType)) {

                        if (orderType.contains(ORDER_TURNOVER_DOWN)) {
                            stockBean.setPercentage(stockBean.getTurnover_rate());
                        } else if (orderType.contains(ORDER_AMPLITU_DOWN)) {
                            stockBean.setPercentage(stockBean.getAmplitude());
                        }
                    }

                    selectList.add(SelectStockBean.copy(stockBean));

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
     * @param dataSize
     * @return
     */
    @Override
    public void refreshDatabySize(int dataSize) {
        if (dataSize == 0) {
            dataSize = 10;
        }
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mSectorId)) {
            params.addQueryStringParameter("sector_id", mSectorId);

        }
        params.addQueryStringParameter("exchange", VALUE_EXCHANGE);
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", dataSize + "");
        params.addQueryStringParameter("symbol_type", VALUE_SYMBOL_TYPE);
        params.addQueryStringParameter("symbol_stype", VALUE_SYMBOL_STYPE);

        DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.opitionmarket, params, this);

    }
}
