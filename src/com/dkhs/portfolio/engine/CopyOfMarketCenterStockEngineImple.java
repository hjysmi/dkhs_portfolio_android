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
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;

public class CopyOfMarketCenterStockEngineImple extends LoadSelectDataEngine {
    private final static String EXCHANGE = "1,2";

    public final static String ORDER_INCREASE = "-percentage";
    public final static String ORDER_DOWN = "percentage";
    public final static String ORDER_TURNOVER = "-turnover_rate";
    public final static String ACE = "percentage";
    public final static String DESC = "-percentage";
    public final static String CURRENT = "";
    private String orderType;
    ILoadDataBackListener loadListener;
    private int Status;
    public CopyOfMarketCenterStockEngineImple(ILoadDataBackListener loadListener, String type) {
        super(loadListener);
        this.orderType = type;
        this.loadListener = loadListener;
    }
    
    @Override
    public void loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair);
        DKHSClient.requestByGet(
                MessageFormat.format(DKHSUrl.StockSymbol.marketcenter + "&page=" + (getCurrentpage() + 1), orderType,10),
                null, this);
    }

    @Override
    public void loadData() {
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.marketcenter, orderType,10), null, this);

    }
    public void loadDataFromCurrent(int num) {
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.marketcenter, orderType,num), null, this);

    }
    @Override
    protected List<SelectStockBean> parseDateTask(String jsonData) {
        List<SelectStockBean> selectList = new ArrayList<SelectStockBean>();
        JSONObject dataObject = null;
        try {
            dataObject = new JSONObject(jsonData);
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
                    SelectStockBean s = SelectStockBean.copy(stockBean);
                    s.setStatus(dataObject.optInt("trade_status"));
                    selectList.add(s);

                    // results.add(stockBean);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }finally{
        	//loadListener.setStatu(dataObject.optInt("trade_status"));
        }

        return selectList;
    }

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
    
}
