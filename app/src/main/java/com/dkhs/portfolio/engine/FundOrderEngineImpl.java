package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FriendsEngineImpl
 * @date 2015/4/23.13:39
 * @Description 基金排行榜列表
 */
public class FundOrderEngineImpl extends LoadMoreDataEngine {


    /**
     * 默认显示一页20条数据
     */
    private static final int pageSize = 20;


    public enum FundTypeBean {


        Default("default"),

        Month("m"),

        Season("3m"),

        HalfYear("6m"),

        OneYear("y"),

        OfficeDay("office"),

        ToYear("ty");

        private String value;

        // 枚举对象构造函数
        private FundTypeBean(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }


    private String userId;

    public FundOrderEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);

    }

    private String type;
    private String sort;
    private int allowTrade;


    public HttpHandler loadDate(String type, String sort) {
        return this.loadDate(type,sort,0);
    }

    public HttpHandler loadDate(String type, String sort,int allowTrade) {
        this.type = type;
        this.sort = sort;
        this.allowTrade = allowTrade;
        return loadData();
    }

    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", pageSize + "");
        params.addQueryStringParameter("allow_trade",String.valueOf(allowTrade));
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Fund.fundsList, params, this);
    }

    @Override
    public HttpHandler loadData() {


        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("pageSize", pageSize + "");
        params.addQueryStringParameter("allow_trade",String.valueOf(allowTrade));
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Fund.fundsList, params, this);

    }

    @Override
    public HttpHandler refreshDatabySize(int dataSize) {


        return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<FundPriceBean> moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {

            try {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<FundPriceBean>>() {
                }.getType());
                System.out.println(">>>>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }


}
