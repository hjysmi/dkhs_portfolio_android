/**
 * @Title QuotesEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-26 下午4:59:15
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.bean.AlertSetBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName QuotesEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-26 下午4:59:15
 * @version 1.0
 */
public class QuotesEngineImpl {

    private static final String TAG = "QuotesEngineImpl";

    public void quotes(String stockCode, IHttpListener listener) {
        RequestParams params = new RequestParams();
        // params.addBodyParameter("symbols", stockCode);
        DKHSClient.request(HttpMethod.GET, MessageFormat.format(DKHSUrl.StockSymbol.quotes, stockCode), null, listener);
    }

    public void quotesNotTip(String stockCode, IHttpListener listener) {
        RequestParams params = new RequestParams();
        // params.addBodyParameter("symbols", stockCode);
        DKHSClient.requestNotTip(HttpMethod.GET, MessageFormat.format(DKHSUrl.StockSymbol.quotes, stockCode), null,
                listener);
    }

    public void symbolfollow(long id, IHttpListener listener) {
        RequestParams params = new RequestParams();
        // params.addBodyParameter("symbol", id + "");
        // params.addBodyParameter("buy_in", "0");
        // params.addBodyParameter("sell_out", "0");
        DKHSClient.requestByPost(MessageFormat.format(DKHSUrl.StockSymbol.symbolfollow, id + ""), params, listener);
    }

    public void symbolAlert(String id, boolean isAlert, IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("is_alert", Boolean.toString(isAlert));
        // params.addBodyParameter("symbol", id + "");
        // params.addBodyParameter("buy_in", "0");
        // params.addBodyParameter("sell_out", "0");
        DKHSClient.requestByPost(MessageFormat.format(DKHSUrl.StockSymbol.symbolfollow, id), params, listener);
    }

    public void symbolFollows(String ids, IHttpListener listener) {
        RequestParams params = new RequestParams();
        DKHSClient.requestByPost(MessageFormat.format(DKHSUrl.StockSymbol.symbolfollow, ids), params, listener);
    }

    public void delfollow(long id, IHttpListener listener) {
        DKHSClient
                .request(HttpMethod.POST, MessageFormat.format(DKHSUrl.StockSymbol.unfollow, id + ""), null, listener);
    }

    public void queryTimeShare(String stockCode, IHttpListener listener) {
        // RequestParams params = new RequestParams();
        // params.addBodyParameter("period", "1");

        DKHSClient
                .requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.sfthumbnail, stockCode), null, listener, false);

    }

    public void queryMoreTimeShare(String stockCode, String current, IHttpListener listener) {
        // RequestParams params = new RequestParams();
        // params.addBodyParameter("period", "1");
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.sfthumbnail, stockCode) + "&fromtime="
                + current, null, listener, false);

    }

    /**
     *
     * 设置股票提醒
     */
    public void stockRemind(String stockId, float priceUp, float priceDown, float percent, boolean setNotice,
            boolean setYanbao, IHttpListener listener) {

        AlertSetBean alertSetBean = new AlertSetBean(priceUp, priceDown, percent, setNotice, setYanbao);

        RequestParams params = new RequestParams();
        params.addBodyParameter("is_alert", "1");
        params.addBodyParameter("alert_settings", new Gson().toJson(alertSetBean));
        DKHSClient
                .request(HttpMethod.POST, MessageFormat.format(DKHSUrl.StockSymbol.remimd, stockId), params, listener);
    }

    /**
     *
     * 取消股票提醒
     */
    public void delStockRemind(String stockId, IHttpListener listener) {
        AlertSetBean alertSetBean = new AlertSetBean(0, 0, 0, false, false);
        RequestParams params = new RequestParams();
        params.addBodyParameter("is_alert", "0");
        params.addBodyParameter("alert_settings", new Gson().toJson(alertSetBean));
        DKHSClient
                .request(HttpMethod.POST, MessageFormat.format(DKHSUrl.StockSymbol.remimd, stockId), params, listener);
    }

    /**
     * 设置七日年化收益率更新提醒,0为取消订阅（货币、理财型基金的提醒设置）
     */
    public void fundRemind7Day(String fundId, boolean isNoticeChange, IHttpListener listener) {

        JSONObject jsonObject = new JSONObject();
        if (isNoticeChange) {
            try {
                jsonObject.put("fund_year_yld", isNoticeChange ? 1 : 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("is_alert", "1");
        params.addBodyParameter("alert_settings", jsonObject.toString());
        DKHSClient
                .request(HttpMethod.POST, MessageFormat.format(DKHSUrl.StockSymbol.remimd, fundId), params, listener);
    }

    /**
     * 设置单位净值更新提醒,0为取消订阅（其他基金的提醒设置）
     */
    public void fundRemindNetvalue(String fundId, boolean isNoticeChange, IHttpListener listener) {

        JSONObject jsonObject = new JSONObject();
        if (isNoticeChange) {
            try {
                jsonObject.put("fund_net_value", isNoticeChange ? 1 : 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("is_alert", "1");
        params.addBodyParameter("alert_settings", jsonObject.toString());
        DKHSClient
                .request(HttpMethod.POST, MessageFormat.format(DKHSUrl.StockSymbol.remimd, fundId), params, listener);
    }

    /**
     * 获取k线图数据
     *
     * @param type 类型 d，w，m
     * @param stockid 股票id
     * @param listener
     */
    public void queryKLine(String type, String stockid, String isHis, IHttpListener listener) {
        // String url = DKHSUrl.BASE_URL + DKHSUrl.StockSymbol.kline_pre +stockid+
        // DKHSUrl.StockSymbol.kline_after+"?period="+type;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("period", type);
        NameValuePair valuePair2 = new BasicNameValuePair("is_realtime", isHis);
        params.add(valuePair);
        params.add(valuePair2);
        DKHSClient
                .requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.kline, stockid), null, params, listener, false);
    }

    public void queryKLine(String type, String stockid, String isHis, IHttpListener listener, String div) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("period", type);
        NameValuePair valuePair2 = new BasicNameValuePair("is_realtime", isHis);
        NameValuePair valuePair3 = new BasicNameValuePair("div", div);
        params.add(valuePair);
        params.add(valuePair2);
        params.add(valuePair3);
        DKHSClient
                .requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.kline, stockid), null, params, listener, false);
    }

    public void queryKLine(String type, String stockid, String isHis, IHttpListener listener, String div, int page) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("period", type);
        NameValuePair valuePair2 = new BasicNameValuePair("is_realtime", isHis);
        NameValuePair valuePair3 = new BasicNameValuePair("div", div);
        NameValuePair valuePair4 = new BasicNameValuePair("page", page + "");
        params.add(valuePair);
        params.add(valuePair2);
        params.add(valuePair3);
        params.add(valuePair4);
        DKHSClient
                .requestByGet(MessageFormat.format(DKHSUrl.StockSymbol.kline, stockid), null, params, listener, false);
    }
}
