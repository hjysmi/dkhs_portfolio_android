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

public class OpitionNewsEngineImple extends LoadNewsDataEngine {
	private final static String EXCHANGE = "1,2";
	//人员相关所有新闻
	public final static int NEWSALL = 0;
	//人员各分支新闻
	public final static int NEWSFOREACH = 1;
	//组合研报分支
	public final static int NEWS_GROUP_FOREACH = 2;
	//个股研报分支
	public final static int NEWS_OPITION_FOREACH = 3;
	//与人员相关的研报分支
	public final static int NEWS_GROUP = 4;
	//与人员相关的研报分支
		public final static int NEWS_GROUP_TWO = 5;
		public final static int GROUP_FOR_ONE = 6;
	private int orderType;
	private String model;
	private NewsforImpleEngine vo;

	public OpitionNewsEngineImple(ILoadDataBackListener loadListener, int type,
			NewsforImpleEngine vo) {
		super(loadListener);
		this.orderType = type;
		this.vo = vo;
	}

	@Override
	public void loadMore() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair valuePair = new BasicNameValuePair("page",
				(getCurrentpage() + 1) + "");
		params.add(valuePair);
		switch (orderType) {
		case NEWSALL:
			DKHSClient.requestByGet(
					MessageFormat.format(DKHSUrl.News.optionnews + "&page=" + (getCurrentpage() + 1), vo.getUserid()),
					null, this);
			break;
		case NEWSFOREACH:
			DKHSClient.requestByGet(MessageFormat.format(
					DKHSUrl.News.peroptionnews+ "&page=" + (getCurrentpage() + 1),vo.getSymbol() ,vo.getContentType()), null,
					this);
			break;
		case NEWS_GROUP_FOREACH:
			DKHSClient.requestByGet(
					MessageFormat.format(DKHSUrl.News.reportnewsgroupeach + "&page=" + (getCurrentpage() + 1), vo.getPortfolioId(),vo.getContentType()),
					null, this);
			break;
		case NEWS_OPITION_FOREACH:
			DKHSClient.requestByGet(
					MessageFormat.format(DKHSUrl.News.reportnewsoptioneach + "&page=" + (getCurrentpage() + 1), vo.getSymbol()),
					null, this);
				break;
		case NEWS_GROUP:
			DKHSClient.requestByGet(
					MessageFormat.format(DKHSUrl.News.reportnews + "&page=" + (getCurrentpage() + 1), vo.getUserid(),vo.getContentSubType()),
					null, this);
			break;
		case NEWS_GROUP_TWO:
			DKHSClient.requestByGet(
					MessageFormat.format(DKHSUrl.News.reportnewstwo + "&page=" + (getCurrentpage() + 1), vo.getContentSubType()),
					null, this);
			break;
		case GROUP_FOR_ONE:
			DKHSClient.requestByGet(
					MessageFormat.format(DKHSUrl.News.reportnewsforone + "&page=" + (getCurrentpage() + 1), vo.getSymbol(),vo.getContentSubType()),
					null, this);
			break;
		default:
			break;
		}
	}

	@Override
	public void loadData() {
		try {
			if(null != vo)
			switch (orderType) {
			case NEWSALL:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.optionnews, vo.getUserid()),
						null, this);
				break;
			case NEWSFOREACH:
				DKHSClient.requestByGet(MessageFormat.format(
						DKHSUrl.News.peroptionnews,vo.getSymbol(),vo.getContentType()), null,
						this);
				break;
			case NEWS_GROUP_FOREACH:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnewsgroupeach, vo.getPortfolioId(),vo.getContentType()),
						null, this);
				break;
			case NEWS_OPITION_FOREACH:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnewsoptioneach, vo.getSymbol()),
						null, this);
					break;
			case NEWS_GROUP:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnews, vo.getUserid(),vo.getContentSubType()),
						null, this);
				break;
			case NEWS_GROUP_TWO:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnewstwo, vo.getContentSubType()),
						null, this);
				break;
			case GROUP_FOR_ONE:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnewsforone, vo.getSymbol(),vo.getContentSubType()),
						null, this);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void loadDatas() {
		try {
			if(null != vo)
			switch (orderType) {
			case NEWSALL:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.optionnews, vo.getUserid()),
						null, this);
				break;
			case NEWSFOREACH:
				DKHSClient.requestByGet(MessageFormat.format(
						DKHSUrl.News.peroptionnews,vo.getSymbol(),vo.getContentType()), null,
						this);
				break;
			case NEWS_GROUP_FOREACH:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnewsgroupeach, vo.getPortfolioId(),vo.getContentType()),
						null, this);
				break;
			case NEWS_OPITION_FOREACH:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnewsoptioneachs, vo.getSymbol()),
						null, this);
					break;
			case NEWS_GROUP:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnews, vo.getUserid(),vo.getContentSubType()),
						null, this);
				break;
			case NEWS_GROUP_TWO:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnewstwo, vo.getContentSubType()),
						null, this);
			case GROUP_FOR_ONE:
				DKHSClient.requestByGet(
						MessageFormat.format(DKHSUrl.News.reportnewsforone, vo.getSymbol(),vo.getContentSubType()),
						null, this);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	protected List<OptionNewsBean> parseDateTask(String jsonData) {
		//Log.e("json", jsonData);
		/*StringBuffer sb = new StringBuffer(jsonData);
		jsonData = jsonData.replace("<b style=\"color:red;\">", "")
				.replace("<script>currentpage=1;</script>", "")
				.replaceAll("<[^>]*>", "").replaceAll("\\&[a-zA-Z]{1,10};", "")
				.replaceAll("\\&\\#[0-9]{1,10};", "").replaceAll("　　", "")
				.trim();*/
		List<OptionNewsBean> selectList = new ArrayList<OptionNewsBean>();
		try {
			JSONObject dataObject = new JSONObject(jsonData);
			setTotalcount(dataObject.optInt("total_count"));
			setTotalpage(dataObject.optInt("total_page"));
			setCurrentpage(dataObject.optInt("current_page"));
			JSONArray resultsJsonArray = dataObject.optJSONArray("results");
			selectList =DataParse.parseArrayJson(OptionNewsBean.class, dataObject,"results");
            // if (null != resultsJsonArray && resultsJsonArray.length() > 0) {
            // int length = resultsJsonArray.length();
            //
            // for (int i = 0; i < length; i++) {
            // JSONObject stockObject = resultsJsonArray.optJSONObject(i);
            // OptionNewsBean stockBean = DataParse.parseObjectJson(
            // OptionNewsBean.class, stockObject);
            // selectList.add(stockBean);
            //
            // // results.add(stockBean);
            //
            // }
            //
            // }

		} catch (JSONException e) {
			e.printStackTrace();
		}
		/*
		 * List<OptionNewsBean> list = null; try { jsonData =
		 * jsonData.replace("<b style=\"color:red;\">",
		 * "").replace("<script>currentpage=1;</script>",
		 * "").replaceAll("<[^>]*>", "").replaceAll("\\&[a-zA-Z]{1,10};",
		 * "").replaceAll("\\&\\#[0-9]{1,10};", "").replaceAll("　　", "").trim();
		 * JSONObject dataObject = new JSONObject(jsonData); JSONArray
		 * resultsJsonArray = dataObject.optJSONArray("results"); Type type =
		 * new TypeToken<List<OptionNewsBean>>(){}.getType(); String text =
		 * resultsJsonArray.toString(); text = "{" + text.substring(1,
		 * text.length()-1) + "}"; list = GsonUtil.json2Collection(text, type);
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		return selectList;
	}
}

class GsonUtil {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static Gson getGson() {
		return new GsonBuilder().serializeNulls().setDateFormat(DATE_FORMAT)
				.create();
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
