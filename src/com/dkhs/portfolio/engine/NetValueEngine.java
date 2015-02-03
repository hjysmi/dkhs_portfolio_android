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

    public class TodayNetValue {
        private float begin;
        private float last_netvalue;
        private float end;
        private List<TodayNetBean> chartlist;
        private float maxOffetValue;
        private int trade_status;

        public int getTrade_status() {
            return trade_status;
        }

        public void setTrade_status(int trade_status) {
            this.trade_status = trade_status;
        }

        public float getBegin() {
            return begin;
        }

        public void setBegin(float begin) {
            this.begin = begin;
        }

        public float getEnd() {
            return end;
        }

        public void setEnd(float end) {
            this.end = end;
        }

        public List<TodayNetBean> getChartlist() {
            return chartlist;
        }

        public void setChartlist(List<TodayNetBean> chartlist) {
            this.chartlist = chartlist;
        }

        public float getMaxOffetValue() {
            return maxOffetValue;
        }

        public void setMaxOffetValue(float maxOffetValue) {
            this.maxOffetValue = maxOffetValue;
        }

        public float getLast_netvalue() {
            return last_netvalue;
        }

        public void setLast_netvalue(float last_netvalue) {
            this.last_netvalue = last_netvalue;
        }

    }

    public class TodayNetBean {
        private String timestamp;
        private float netvalue;
        private float percentage;
        private float change;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public float getNetvalue() {
            return netvalue;
        }

        public void setNetvalue(float netvalue) {
            this.netvalue = netvalue;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public float getChange() {
            return change;
        }

        public void setChange(float change) {
            this.change = change;
        }

    }

}
