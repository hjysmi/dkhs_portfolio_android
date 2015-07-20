/**
 * @Title FundsOrderEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FundsOrderEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-29 下午5:07:15
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
    public HttpHandler loadMore() {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mOrderType)) {
            params.addQueryStringParameter("sort", mOrderType);

        }
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", mpageSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Plate.hotPlate, params, this);
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mOrderType)) {
            params.addQueryStringParameter("sort", mOrderType);

        }
        params.addQueryStringParameter("page_size", mpageSize + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Plate.hotPlate, params, this);
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


    /**
     * @param dataSize
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public HttpHandler refreshDatabySize(int pageIndex) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mOrderType)) {
            params.addQueryStringParameter("sort", mOrderType);

        }
        params.addQueryStringParameter("page_size", "50");
        params.addQueryStringParameter("page", pageIndex + "");
        return DKHSClient.request(HttpMethod.GET, DKHSUrl.Plate.hotPlate, params, this);

    }

}
