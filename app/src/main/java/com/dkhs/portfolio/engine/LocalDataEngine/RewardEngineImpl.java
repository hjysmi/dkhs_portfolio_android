package com.dkhs.portfolio.engine.LocalDataEngine;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName HotTopicEngineImpl
 * @date 2015/4/23.13:39
 * @Description
 */
public class RewardEngineImpl extends LoadMoreDataEngine {


    /**
     * 默认显示一页20条数据
     */
    private static final int pageSize = 20;
    private int responseStatus = 0;

    private BannerTopicsBean mBannerTopicsBean = new BannerTopicsBean();

    private List<TopicsBean> mFirstPageTopicsBeans;
    WeakHandler mWeakHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

                    MoreDataBean more = getMoreDataBean();
                    if (more != null) {
                        setTotalcount(more.getTotalCount());
                        setTotalpage(more.getTotalPage());
                        setCurrentpage(more.getCurrentPage());
                        setStatu(more.getStatu());
                        List list = new ArrayList();
                        if (mFirstPageTopicsBeans != null)
                            list.addAll(mFirstPageTopicsBeans);
                        more.setResults(list);
                        getLoadListener().loadFinish(more);
                        responseStatus = 0;
                        mBannerTopicsBean = new BannerTopicsBean();
                        mFirstPageTopicsBeans = null;
                    }

            return false;
        }
    });



    public RewardEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);
    }

    public HttpHandler loadDate() {
        return loadData();
    }

    @Override
    public HttpHandler loadMore() {
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("recommend_level", "");
        params.addQueryStringParameter("page_size", pageSize + "");
        params.addQueryStringParameter("content_type","40");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getRewardList, params, this);

    }


    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("pageSize", pageSize + "");
        params.addQueryStringParameter("recommend_level", "");
        params.addQueryStringParameter("content_type","40");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getRewardList, params, new ParseHttpListener<MoreDataBean>() {
            @Override
            protected MoreDataBean parseDateTask(String jsonData) {
                MoreDataBean<TopicsBean> moreBean = null;
                if (!TextUtils.isEmpty(jsonData)) {

                    try {
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<TopicsBean>>() {
                        }.getType());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return moreBean;
            }

            @Override
            protected void afterParseData(MoreDataBean object) {
                if (object != null) {
                    mFirstPageTopicsBeans = object.getResults();
                    setMoreDataBean(object);
                    onFinish();
                }
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
                RewardEngineImpl.this.onFailure(errCode, errMsg);
                onFinish();
            }

            public void onFinish() {
                RewardEngineImpl.this.responseStatus = responseStatus | 4;
                mWeakHandler.sendEmptyMessage(responseStatus);

            }
        });


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
                moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<TopicsBean>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }


}
