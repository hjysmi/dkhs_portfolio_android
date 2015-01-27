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

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;

/**
 * @ClassName NetValueEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 下午3:12:05
 * @version 1.0
 */
public class NetValueReportEngine extends LoadMoreDataEngine {

    private String mConbinationId;

    // 类型（0->1周，1->1个月(30 day)，2->3个月(90 day)，3->6个月(180 day)，4->至今，即查询所有）
    private String type;

    public NetValueReportEngine(String id, ILoadDataBackListener loadListener) {
        super(loadListener);
        this.mConbinationId = id;
    }

    /**
     * 查询7天的报表
     */
    public void requerySevenDayReport() {
        // requeryReport(1, 7);
        requeryReport("0");
    }

    /**
     * 查询一个月的报表
     */
    public void requeryMonthReport() {
        requeryReport("1");
    }

    /**
     * 查询至今的报表
     */
    public void requeryHistoryReport(int page) {
        requeryReport("5");
    }

    public void requeryReport(int page, int pageSize) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // NameValuePair valuePair = new BasicNameValuePair("portfolio_id", mConbinationId + "");
        // NameValuePair valuePair = new BasicNameValuePair("portfolio_id", 508 + "");
        NameValuePair valuePair2 = new BasicNameValuePair("page", page + "");
        NameValuePair valuePair3 = new BasicNameValuePair("page_size", pageSize + "");
        // params.add(valuePair);
        params.add(valuePair2);
        params.add(valuePair3);
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.NetValue.report, mConbinationId), null, params, this);

    }

    public void requeryReport(String type) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        NameValuePair valuePair = new BasicNameValuePair("types", type);

        params.add(valuePair);
        DKHSClient.requestByGet(MessageFormat.format(DKHSUrl.NetValue.report, mConbinationId), null, params, this);
    }

    @Override
    public HttpHandler loadMore() {
        return null;
    }

    @Override
    public HttpHandler loadData() {
        return null;
        // requerySevenDayReport();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param object
     * @return
     */
    // @Override
    // protected void afterParseData(Object object) {
    // // TODO Auto-generated method stub
    //
    // }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param jsonData
     * @return
     * @return
     */
    @Override
    protected MoreDataBean parseDateTask(String jsonData) {

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        MoreDataBean<NetValueReportBean> moreBean = (MoreDataBean) gson.fromJson(jsonData,
                new TypeToken<MoreDataBean<NetValueReportBean>>() {
                }.getType());
        return moreBean;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param dataSize
     * @return
     */
    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        // TODO Auto-generated method stub
        return null;

    }

}
