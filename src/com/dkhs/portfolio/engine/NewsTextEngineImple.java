package com.dkhs.portfolio.engine;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class NewsTextEngineImple extends LoadNewsTextEngine{
	private final static String EXCHANGE = "1,2";

    public final static String ORDER_INCREASE = "-percentage";
    public final static String ORDER_DOWN = "percentage";
    public final static String ORDER_TURNOVER = "-turnover_rate";
    public final static String ACE = "";
    public final static String DESC = "-";
    private String id;
    public NewsTextEngineImple(ILoadDataBackListener loadListener,String id) {
        super(loadListener);
        this.id = id;
    }

    @Override
    public void loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair);
        if(null != id){
	        DKHSClient.requestByGet(DKHSUrl.News.newstext +id, null, this);
        } 
    }

    @Override
    public void loadData() {
    	 if(null != id){
    		 DKHSClient.requestByGet(DKHSUrl.News.newstext +id, null, this);
         } 

    }

    @Override
    protected OptionNewsBean parseDateTask(String jsonData) {
    	Log.e("json", jsonData);
    	//jsonData = jsonData.replace("<b style=\"color:red;\">", "").replace("<script>currentpage=1;</script>", "").replaceAll("<[^>]*>", "").replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("\\&\\#[0-9]{1,10};", "").replaceAll("　　", "").trim();
    	jsonData = jsonData.replaceAll("<br/>", "\n").trim().replaceAll(" ", "");
    	OptionNewsBean stockBean = null;
        try {
        	JSONObject dataObject = new JSONObject(jsonData);
        	stockBean = DataParse.parseObjectJson(OptionNewsBean.class, dataObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stockBean;
    }
}
