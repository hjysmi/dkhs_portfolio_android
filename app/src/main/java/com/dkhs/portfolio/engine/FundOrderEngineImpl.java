package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.FundBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
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
 * @Description 获取基金排行榜
 */
public class FundOrderEngineImpl extends LoadMoreDataEngine {


    private int page = 1;
    /**
     * 默认显示一页99条数据
     */
    private static final int pageSize = 99;


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
        return     DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Fund.fundOrder, params, this);
     }

    @Override
    public HttpHandler loadData() {

        page=1;
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("page", page+"");
        params.addQueryStringParameter("pageSize", pageSize+"");
        return     DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Fund.fundOrder, params, this);

    }

    @Override
    public HttpHandler refreshDatabySize(int dataSize) {



        return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<FundBean> moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {

            try {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<FundBean>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }


}
