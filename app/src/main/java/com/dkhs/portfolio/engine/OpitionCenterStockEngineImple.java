package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

public class OpitionCenterStockEngineImple extends LoadMoreDataEngine {

    public final static String VALUE_EXCHANGE = "1,2";
    public final static String VALUE_EXCHANGE_SAHNG = "2";
    public final static String VALUE_SYMBOL_TYPE = "1";
    public final static String VALUE_SYMBOL_STYPE = "101";
    public final static String VALUE_SYMBOL_SELECT = "6";
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
    private int mPageIndex = 1;
    private String symboltype = VALUE_SYMBOL_TYPE;
    private String exchange = VALUE_EXCHANGE;
    private String symbol_stype = VALUE_SYMBOL_STYPE;
    private String list_sector;

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

    public OpitionCenterStockEngineImple(ILoadDataBackListener loadListener, StockViewType type, int pagesize,
                                         String list_sector, String symbol_stype, String exchange) {
        super(loadListener);
        this.mStockType = type;
        this.mPageSize = pagesize;
        this.list_sector = list_sector;
        this.symbol_stype = symbol_stype;
        this.exchange = exchange;
        getOrderType(type);
    }

    private void getOrderType(StockViewType type) {
        switch (type) {
            case MARKET_STOCK_TURNOVER:
            case STOCK_HANDOVER_CLICKABLE:
            case STOCK_HANDOVER: {
                orderType = ORDER_TURNOVER;
            }
            break;
            case STOCK_DRAWDOWN:
            case STOCK_DRAWDOWN_CLICKABLE:
            case MARKET_STOCK_DOWNRATIO: {
                orderType = ORDER_DOWN;
            }
            break;
            case STOCK_INCREASE:
            case STOCK_INCREASE_CLICKABLE:
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
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mSectorId)) {
            params.addQueryStringParameter("sector_id", mSectorId);

        }
        params.addQueryStringParameter("exchange", exchange);
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", mPageSize + "");
        params.addQueryStringParameter("symbol_type", symboltype);
        if (!TextUtils.isEmpty(symbol_stype))
            params.addQueryStringParameter("symbol_stype", symbol_stype);
        if (!TextUtils.isEmpty(list_sector)) {
            params.addQueryStringParameter("list_sector", list_sector);
        }
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.opitionmarket, params, this);

    }

    @Override
    public HttpHandler loadData() {

        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mSectorId)) {
            params.addQueryStringParameter("sector_id", mSectorId);

        }
        params.addQueryStringParameter("page", mPageIndex + "");
        params.addQueryStringParameter("exchange", exchange);
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", mPageSize + "");
        params.addQueryStringParameter("symbol_type", symboltype);
        if (!TextUtils.isEmpty(symbol_stype))
            params.addQueryStringParameter("symbol_stype", symbol_stype);
        if (!TextUtils.isEmpty(list_sector)) {
            params.addQueryStringParameter("list_sector", list_sector);
        }
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.opitionmarket, params, this);

    }

    public HttpHandler loadByPage(int pageIndex) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mSectorId)) {
            params.addQueryStringParameter("sector_id", mSectorId);

        }
        params.addQueryStringParameter("exchange", exchange);
        params.addQueryStringParameter("sort", orderType);
        params.addQueryStringParameter("page_size", mPageSize + "");
        params.addQueryStringParameter("symbol_type", symboltype);
        if (!TextUtils.isEmpty(symbol_stype))
            params.addQueryStringParameter("symbol_stype", symbol_stype);
        if (!TextUtils.isEmpty(list_sector)) {
            params.addQueryStringParameter("list_sector", list_sector);
        }
        params.addQueryStringParameter("page", pageIndex + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.opitionmarket, params, this);
    }

    // public void loadDataFromCurrent(int num) {
    // // DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.opitionmarket, orderType, num), null, this);
    //
    // }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<StockPriceBean> dataMoreBean = new MoreDataBean.EmptyMoreBean();
        MoreDataBean<SelectStockBean> parseMoreBean = new MoreDataBean.EmptyMoreBean();

        // List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        try {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            dataMoreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<StockPriceBean>>() {
            }.getType());
            parseMoreBean.copyMoreDataBean(dataMoreBean);
            parseMoreBean.setResults(new ArrayList<SelectStockBean>());

            for (StockPriceBean stockBean : dataMoreBean.getResults()) {
                // selectList.add(SelectStockBean.copy(priceBean));
                if (!TextUtils.isEmpty(orderType)) {

                    if (orderType.contains(ORDER_TURNOVER_DOWN)) {
                        stockBean.setPercentage(stockBean.getTurnover_rate());
                    } else if (orderType.contains(ORDER_AMPLITU_DOWN)) {
                        stockBean.setPercentage(stockBean.getAmplitude());
                    }
                }

                SelectStockBean selectBean = SelectStockBean.copy(stockBean);
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
        //
        // if (!TextUtils.isEmpty(orderType)) {
        //
        // if (orderType.contains(ORDER_TURNOVER_DOWN)) {
        // stockBean.setPercentage(stockBean.getTurnover_rate());
        // } else if (orderType.contains(ORDER_AMPLITU_DOWN)) {
        // stockBean.setPercentage(stockBean.getAmplitude());
        // }
        // }
        //
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

        return DKHSClient.request(HttpMethod.GET, DKHSUrl.StockSymbol.opitionmarket, params, this);

    }

    public int getmPageIndex() {
        return mPageIndex;
    }

    public void setmPageIndex(int mPageIndex) {
        this.mPageIndex = mPageIndex;
    }
}
