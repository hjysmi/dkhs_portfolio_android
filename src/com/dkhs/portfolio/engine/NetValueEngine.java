/**
 * @Title NetValueEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-22 下午3:12:05
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;

/**
 * @ClassName NetValueEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 下午3:12:05
 * @version 1.0
 */
public class NetValueEngine {

    private int mConbinationId;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param id
     */
    public NetValueEngine(int id) {
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
        requeryNetValue(mConbinationId, "2", listener);
    }

    // http://192.168.107.251:8000/api/v1/portfolio/query_daily_netvalue/?portfolio_id=525&types=0
    public void requeryNetValue(int combinationId, String type, IHttpListener listener) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // NameValuePair valuePair = new BasicNameValuePair("portfolio_id", combinationId + "");
        // NameValuePair valuePair = new BasicNameValuePair("portfolio_id", 508 + "");
        NameValuePair valuePair2 = new BasicNameValuePair("types", type);
        // params.add(valuePair);
        params.add(valuePair2);
        DKHSClient.requestByGet(DKHSUrl.NetValue.queryDaily + combinationId + "/netvalue_daily_chart/", null, params,
                listener);
    }

    public void requeryToday(IHttpListener todayListener) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // test
        // NameValuePair valuePair = new BasicNameValuePair("portfolio_id", "508");
        NameValuePair valuePair = new BasicNameValuePair("portfolio_id", mConbinationId + "");
        params.add(valuePair);
        DKHSClient.requestByGet(DKHSUrl.NetValue.queryToday + mConbinationId + "/netvalue_realtime_chart", null, null,
                todayListener);
    }

    public class TodayNetValue {
        private float begin;
        private float end;
        private List<TodayNetBean> chartlist;

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

    }

    public class TodayNetBean {
        private String timestamp;
        private float netvalue;

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

    }

}
