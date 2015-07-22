package com.dkhs.portfolio.engine;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
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
public class HotTopicEngineImpl extends LoadMoreDataEngine {


    /**
     * 默认显示一页20条数据
     */
    private static final int pageSize = 20;
    private int responseStatus=0;


    private BannerTopicsBean mBannerTopicsBean=new BannerTopicsBean();

    private List<TopicsBean> mFristpageTopicsBeans;

    public HotTopicEngineImpl(ILoadDataBackListener loadListener) {
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
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getLatestTopic, params, this);
    }

    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("pageSize", pageSize + "");


        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getHotTopic, params,new ParseHttpListener<MoreDataBean>() {
            @Override
            protected MoreDataBean parseDateTask(String jsonData) {
                MoreDataBean<TopicsBean> moreBean = null;
                if (!TextUtils.isEmpty(jsonData)) {

                    try {
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<TopicsBean>>() {
                        }.getType());

                        moreBean.getResults();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                HotTopicEngineImpl.this.responseStatus|=4;
                mFristpageTopicsBeans=moreBean.getResults();
                setMoreDataBean(moreBean);
                mWeakHandler.sendEmptyMessage(responseStatus);
                return moreBean;
            }

            @Override
            protected void afterParseData(MoreDataBean object) {

            }
        });
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getStickTopic, params, new ParseHttpListener<MoreDataBean>() {
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
                mBannerTopicsBean.hotTopicsBeans= moreBean.getResults();

                HotTopicEngineImpl.this.responseStatus|=1;
                mWeakHandler.sendEmptyMessage(responseStatus);
                return moreBean;
            }

            @Override
            protected void afterParseData(MoreDataBean object) {

            }
        });
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.Ads.getNewsBannerAds, null, new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return AdBean.class;
            }

            @Override
            protected void afterParseData(Object object) {

                if (object != null) {
                    AdBean adBean = (AdBean) object;
//                    updateAdBanner(adBean);
                    mBannerTopicsBean.adBean=adBean;
                }

                HotTopicEngineImpl.this.responseStatus|=2;
                mWeakHandler.sendEmptyMessage(responseStatus);
            }
        });

    }


    WeakHandler mWeakHandler =new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case 7:

                    MoreDataBean more=new MoreDataBean();
                    List list=new ArrayList();
                    list.add(mBannerTopicsBean);
                    list.add(mFristpageTopicsBeans);
                    more.setResults(list);

                    getLoadListener().loadFinish(getMoreDataBean());
                    responseStatus=0;
                    mBannerTopicsBean=null;
                    mFristpageTopicsBeans=null;
                    break;
            }
            return false;
        }
    });


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
