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


    private int page = 1;
    /**
     * 默认显示一页99条数据
     */
    private static final int pageSize = 20;


    public enum FundTypeBean{


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

    private String  type;
    private String  sort;


    public HttpHandler loadDate(String type,String sort){
        this.type=type;
        this.sort=sort;
        return  loadData();
    }

    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", pageSize + "");
        return     DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Fund.fundsList, params, this);
     }

    @Override
    public HttpHandler loadData() {

        page=1;
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("page", page+"");
        params.addQueryStringParameter("pageSize", pageSize+"");
        return     DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Fund.fundsList, params, this);

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }


}
