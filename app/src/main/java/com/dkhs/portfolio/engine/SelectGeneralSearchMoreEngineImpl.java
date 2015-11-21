package com.dkhs.portfolio.engine;

import android.content.Context;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreType;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by zhangcm on 2015/11/20.
 */
public class SelectGeneralSearchMoreEngineImpl extends LoadMoreDataEngine{
    /**
     * 默认显示一页20条数据
     */
    private static final int pageSize = 20;

    private SearchMoreType searchMoreType;
    private Context mContext;
    public SelectGeneralSearchMoreEngineImpl(ILoadDataBackListener loadListener,SearchMoreType searchMoreType,Context context) {
        super(loadListener);
        this.searchMoreType = searchMoreType;
        this.mContext = context;
    }
    private String searchString;
    public void setSearchString(String searchString){
        this.searchString = searchString;
    }
    @Override
    public HttpHandler loadMore() {
        if(TextUtils.isEmpty(searchString))
            return null;
        RequestParams params = new RequestParams();
        String url = getSearchUrl(params);
        if(TextUtils.isEmpty(url))
            return null;
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", pageSize + "");
        params.addQueryStringParameter("q",searchString);
        return DKHSClient.request(HttpRequest.HttpMethod.GET, url, params, this);
    }

    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("pageSize", pageSize + "");
        if(TextUtils.isEmpty(searchString))
            return null;
        params.addQueryStringParameter("q",searchString);
        String url = getSearchUrl(params);
        if(TextUtils.isEmpty(url))
            return null;
        return DKHSClient.request(HttpRequest.HttpMethod.GET, url, params, this.setLoadingDialog(mContext,false));

    }

    private String getSearchUrl(RequestParams params) {
        String url = null;
        switch (searchMoreType){
            case MORE_STOCK:
                url = DKHSUrl.Search.search_symools;
                params.addQueryStringParameter("symbol_type", "1,5");
                break;
            case MORE_FUND:
                params.addQueryStringParameter("symbol_type", "3");
                url = DKHSUrl.Search.search_symools;
                break;
            case MORE_FUND_MANAGER:
                url = DKHSUrl.Search.search_fund_managers;
                break;
            case MORE_USER:
                url = DKHSUrl.Search.search_users;
                break;
            case MORE_COMBINATION:
                url = DKHSUrl.Search.search_portfolios;
                break;
            case MORE_REWARD:
                url = DKHSUrl.Search.search_statues;
                params.addQueryStringParameter("content_type","40");
                break;
            case MORE_TOPIC:
                params.addQueryStringParameter("content_type","0");
                url = DKHSUrl.Search.search_statues;
                break;
        }
        return url;
    }

    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<Object> moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {

            try {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                switch (searchMoreType){
                    case MORE_STOCK:
                    case MORE_FUND:
                        moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<QuotesBean>>() {
                        }.getType());
                        break;
                    case MORE_FUND_MANAGER:
                        moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<FundManagerBean>>() {
                        }.getType());
                        break;
                    case MORE_USER:
                        moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<UserEntity>>() {
                        }.getType());
                        break;
                    case MORE_COMBINATION:
                        moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<CombinationBean>>() {
                        }.getType());
                        break;
                    case MORE_REWARD:
                    case MORE_TOPIC:
                        moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<TopicsBean>>() {
                        }.getType());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }
}
