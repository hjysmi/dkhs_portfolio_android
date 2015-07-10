package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.ui.fragment.FundManagerRankingsFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FundManagerRankingsEngineImpl
 * @date 2015/4/23.13:39
 * @Description 基金经理排行榜列表
 */
public class FundManagerRankingsEngineImpl extends LoadMoreDataEngine {


    /**
     * 默认显示一页20条数据
     */
    private static final int pageSize = 20;


    public FundManagerRankingsEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);
    }

    private String type;
    private String sort;


    public HttpHandler loadDate(String type, String sort) {
        this.type = type;
        this.sort = sort;
        return loadData();
    }

    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Fund.fundsManagerList, params, this);
    }

    @Override
    public HttpHandler loadData() {


        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("pageSize", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Fund.fundsManagerList, params, this);

    }

    @Override
    public HttpHandler refreshDatabySize(int dataSize) {


        return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<FundManagerBean> moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {

            try {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<FundManagerBean>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }


}
