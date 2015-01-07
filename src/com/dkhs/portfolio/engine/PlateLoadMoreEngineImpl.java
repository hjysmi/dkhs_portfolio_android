/**
 * @Title FundsOrderEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName FundsOrderEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version 1.0
 */
public class PlateLoadMoreEngineImpl extends LoadMoreDataEngine {

    public PlateLoadMoreEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);

    }

    private String mOrderType;
    private int mpageSize = 50;

    public PlateLoadMoreEngineImpl(ILoadDataBackListener loadListener, String ordertype) {
        super(loadListener);
        this.mOrderType = ordertype;

    }

    @Override
    public void loadMore() {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mOrderType)) {
            params.addQueryStringParameter("sort", mOrderType);

        }
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", mpageSize + "");
        DKHSClient.request(HttpMethod.GET, DKHSUrl.Plate.hotPlate, params, this);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loadData() {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mOrderType)) {
            params.addQueryStringParameter("sort", mOrderType);

        }
        params.addQueryStringParameter("page_size", mpageSize + "");
        DKHSClient.request(HttpMethod.GET, DKHSUrl.Plate.hotPlate, params, this);
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {

        MoreDataBean<SectorBean> moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {

            try {

                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

                moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<SectorBean>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;

    }

    private int pageSize = 50;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param dataSize
     * @return
     */
    @Override
    public void refreshDatabySize(int pageIndex) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mOrderType)) {
            params.addQueryStringParameter("sort", mOrderType);

        }
        params.addQueryStringParameter("page_size", pageSize + "");
        params.addQueryStringParameter("page", pageIndex + "");
        DKHSClient.request(HttpMethod.GET, DKHSUrl.Plate.hotPlate, params, this);

    }

}
