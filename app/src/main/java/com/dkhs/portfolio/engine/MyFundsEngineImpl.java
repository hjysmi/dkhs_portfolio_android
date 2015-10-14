package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.FundTradeBean;
import com.dkhs.portfolio.bean.MoreDataBean;
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
 * Created by zhangcm on 2015/9/21.13:56
 */
public class MyFundsEngineImpl extends LoadMoreDataEngine{

    /**
     * 默认显示一页20条数据
     */
    private static final int pageSize = 20;
    private int direction;
    public MyFundsEngineImpl(ILoadDataBackListener loadListener,int direction) {
        super(loadListener);
        this.direction = direction;
    }

    public MyFundsEngineImpl() {}

    @Override
    public HttpHandler loadMore() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("direction", direction +"");
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Funds.get_funds_trades, params, this);
    }

    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("direction", direction +"");
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("pageSize", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Funds.get_funds_trades, params, this);

    }

    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
            return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<FundTradeBean> moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {

            try {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<FundTradeBean>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }

    public void getMyFunds(IHttpListener listener){
        DKHSClient.requestByGet(listener,DKHSUrl.Funds.get_my_funds);
    }
    public void getMyFundInfo(String id, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("fund_id",id);
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Funds.get_my_fundinfo,params,listener);
    }
    public void getFundsTradesInfo(String id, IHttpListener listener){
        DKHSClient.requestByGet(listener,DKHSUrl.Funds.get_funds_trades_info, id);
    }

}
