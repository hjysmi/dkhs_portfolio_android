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

public class OpitionNewsEngineImple extends LoadNewsDataEngine{
	private final static String EXCHANGE = "1,2";

    public final static String ORDER_INCREASE = "-percentage";
    public final static String ORDER_DOWN = "percentage";
    public final static String ORDER_TURNOVER = "-turnover_rate";
    public final static String ACE = "";
    public final static String DESC = "-";
    private String orderType;
    private String model;
    public OpitionNewsEngineImple(ILoadDataBackListener loadListener, String type,String model) {
        super(loadListener);
        this.orderType = type;
        this.model = model;
    }

    @Override
    public void loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair);
        if(null == model){
	        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.News.optionnews + "&page=" + (getCurrentpage() + 1),
	        		orderType), null, this);
        }else{
        	DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.News.peroptionnews + "&page=" + (getCurrentpage() + 1),
	        		orderType,model), null, this);
        }
    }

    @Override
    public void loadData() {
    	if(null == model){
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.News.optionnews,
        		orderType), null, this);
    	}else{
    		DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.News.peroptionnews,
            		orderType,model), null, this);
    	}

    }

    @Override
    protected List<OptionNewsBean> parseDateTask(String jsonData) {
    	Log.e("json", jsonData);
    	jsonData = jsonData.replace("<b style=\"color:red;\">", "").replace("<script>currentpage=1;</script>", "").replaceAll("<[^>]*>", "").replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("\\&\\#[0-9]{1,10};", "").replaceAll("　　", "").trim();
        List<OptionNewsBean> selectList = new ArrayList<OptionNewsBean>();
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
                    OptionNewsBean stockBean = DataParse.parseObjectJson(OptionNewsBean.class, stockObject);
                    selectList.add(stockBean);

                    // results.add(stockBean);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    	/*List<OptionNewsBean> list = null;
		try {
			jsonData = jsonData.replace("<b style=\"color:red;\">", "").replace("<script>currentpage=1;</script>", "").replaceAll("<[^>]*>", "").replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("\\&\\#[0-9]{1,10};", "").replaceAll("　　", "").trim();
			 JSONObject dataObject = new JSONObject(jsonData);
			JSONArray resultsJsonArray = dataObject.optJSONArray("results");
			Type type = new TypeToken<List<OptionNewsBean>>(){}.getType();
			String text = resultsJsonArray.toString();
			text = "{" + text.substring(1, text.length()-1) + "}";
			list = GsonUtil.json2Collection(text, type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        return selectList;
    }
}
class GsonUtil {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static Gson getGson() {
		return new GsonBuilder().serializeNulls().setDateFormat(DATE_FORMAT).create();
	}

	public String Object2Json2(Object obj) {
		return getGson().toJson(obj);
	}

	public static <T> String t2Json2(T t) {
		return getGson().toJson(t);
	}

	public static <T> T json2T(String jsonString, Class<T> clazz) {
		return getGson().fromJson(jsonString, clazz);
	}

	public static <T> List<T> json2Collection(String jsonStr, Type type) {
		return getGson().fromJson(jsonStr, type);
	}

}
