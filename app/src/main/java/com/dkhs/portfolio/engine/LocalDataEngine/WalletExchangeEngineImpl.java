package com.dkhs.portfolio.engine.LocalDataEngine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
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
 * @ClassName TopicsEngineImpl
 * @date 2015/4/23.13:39
 * @Description
 */
public class WalletExchangeEngineImpl extends LoadMoreDataEngine {


    @Override
    public int getTotalcount() {
        return super.getTotalcount();
    }

    /**
     * 默认显示一页20条数据
     */
    private static final int pageSize = 20;





    public WalletExchangeEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);
    }




    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", pageSize + "");
            return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Wallet.wallet_exchange, params, this);
    }

    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Wallet.wallet_exchange, params, this);
    }

    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<TopicsBean> moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {
            try {
                    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                    moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<CommentBean>>() {
                    }.getType());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }



}
