/**
 * @Title NetValueEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-22 下午3:12:05
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName NetValueEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 下午3:12:05
 * @version 1.0
 */
public class NetValueEngine {

    private String mConbinationId;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param id
     */
    public NetValueEngine(String id) {
        mConbinationId = id;
    }

    /**
     * 查询7天的净值数据
     */
    public void requerySevenDay(IHttpListener listener) {
        requeryNetValue(mConbinationId, "0", listener);
    }

    /**
     * 查询一个月的净值数据
     */
    public void requeryOneMonth(IHttpListener listener) {
        requeryNetValue(mConbinationId, "1", listener);
    }

    /**
     * 查询至今的净值数据
     */
    public void requeryHistory(IHttpListener listener) {
        requeryNetValue(mConbinationId, "5", listener);
    }

    public HttpHandler requeryHistory(int count, int page, IHttpListener listener) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page_size", "" + count);
        NameValuePair valuePair2 = new BasicNameValuePair("page", "" + page);
        params.add(valuePair);
        params.add(valuePair2);
        return DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.NetValue.report, mConbinationId), null, params,
                listener);
    }

    public HttpHandler requeryHistoryDetailPosition(int count, int page, IHttpListener listener) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("page_size", "" + count);
        NameValuePair valuePair2 = new BasicNameValuePair("page", "" + page);
        params.add(valuePair);
        params.add(valuePair2);
        return DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.NetValue.queryDetailPosition, mConbinationId),
                null, params, listener);
    }

    public HttpHandler requeryDay(String fromDate, String toDate, IHttpListener listener) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("from_date", fromDate);
        NameValuePair valuePair2 = new BasicNameValuePair("to_date", toDate);
        params.add(valuePair);
        params.add(valuePair2);
        return DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.NetValue.queryDaily, mConbinationId), null, params,
                listener);
    }

    // http://192.168.107.251:8000/api/v1/portfolio/query_daily_netvalue/?portfolio_id=525&types=0
    public void requeryNetValue(String combinationId, String type, IHttpListener listener) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("types", type);
        NameValuePair valuePair2 = new BasicNameValuePair("funcid", "1");
        params.add(valuePair);
        params.add(valuePair2);
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.NetValue.queryDaily, combinationId), null, params,
                listener);
    }

    public void requeryToday(IHttpListener todayListener) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.NetValue.queryToday, mConbinationId), null, null,
                todayListener);
    }

}
