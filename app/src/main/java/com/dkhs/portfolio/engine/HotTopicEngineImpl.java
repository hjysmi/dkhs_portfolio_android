package com.dkhs.portfolio.engine;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
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
    private int responseStatus = 0;
    private int mSortType = 0;

    private BannerTopicsBean mBannerTopicsBean = new BannerTopicsBean();

    private List<TopicsBean> mFirstPageTopicsBeans;
    WeakHandler mWeakHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 7:
                    MoreDataBean more = getMoreDataBean();
                    if (more != null) {
                        setTotalcount(more.getTotalCount());
                        setTotalpage(more.getTotalPage());
                        setCurrentpage(more.getCurrentPage());
                        setStatu(more.getStatu());
                        List list = new ArrayList();
                        if (mBannerTopicsBean.adBean != null || (mBannerTopicsBean.hotTopicsBeans != null && mBannerTopicsBean.hotTopicsBeans.size() > 0)) {
                            list.add(mBannerTopicsBean);
                        }
                        if (mFirstPageTopicsBeans != null)
                            list.addAll(mFirstPageTopicsBeans);
                        if (list != null && list.size() > 0) {
                            more.setResults(list);
                            getLoadListener().loadFinish(more);
                        }

                        responseStatus = 0;
                        mBannerTopicsBean = new BannerTopicsBean();
                        mFirstPageTopicsBeans = null;
                    }
                    break;
            }
            return false;
        }
    });


    public HotTopicEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);
    }

    public HttpHandler loadDate() {
        return loadData();
    }

    @Override
    public HttpHandler loadMore() {
        if (mSortType == 0) {
            RequestParams params = new RequestParams();
//        params.addQueryStringParameter("type", type);
            params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
            params.addQueryStringParameter("recommend_level", "1");
            params.addQueryStringParameter("page_size", pageSize + "");
            return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getHotTopic, params, this);
        } else {
            RequestParams params = new RequestParams();
//        params.addQueryStringParameter("type", type);
            params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
            params.addQueryStringParameter("recommend_level", "");
            params.addQueryStringParameter("page_size", pageSize + "");
            return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getLatestTopic, params, this);
        }

    }

    public void loadData(int sort) {
        mSortType = sort;
        loadData();
    }

    public int getSortType() {
        return mSortType;
    }

    @Override
    public HttpHandler loadData() {
        String cacheTopics;
        String url = "";
        RequestParams params = new RequestParams();
        if (mSortType == 0) {
            url = DKHSUrl.BBS.getHotTopic;
            params.addQueryStringParameter("page", "1");
            params.addQueryStringParameter("pageSize", pageSize + "");
            params.addQueryStringParameter("recommend_level", "1");
        } else {
            url = DKHSUrl.BBS.getLatestTopic;
            params.addQueryStringParameter("page", "1");
            params.addQueryStringParameter("pageSize", pageSize + "");
            params.addQueryStringParameter("recommend_level", "");
        }
        DKHSClient.request(HttpRequest.HttpMethod.GET, url, params, new ParseHttpListener<MoreDataBean>() {
            @Override
            protected MoreDataBean parseDateTask(String jsonData) {
                return parse2Topics(jsonData);
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
                HotTopicEngineImpl.this.onFailure(errCode, errMsg);
                onFinish();
            }

            public void onFinish() {
                HotTopicEngineImpl.this.responseStatus = responseStatus | 4;
                mWeakHandler.sendEmptyMessage(responseStatus);

            }

            @Override
            public void onSuccess(String jsonObject) {
                super.onSuccess(jsonObject);
                if (mSortType == 0) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_HOT_TOPICS, jsonObject);
                } else {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_LATEST_TOPICS, jsonObject);
                }
            }
        });

        AdEngineImpl.getStatusesBanner(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return AdBean.class;
            }

            @Override
            protected void afterParseData(Object object) {

                if (object != null) {
                    AdBean adBean = (AdBean) object;
//                    updateAdBanner(adBean);
                    mBannerTopicsBean.adBean = adBean;
                }
                onFinish();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
                onFinish();
            }

            public void onFinish() {
                HotTopicEngineImpl.this.responseStatus = responseStatus | 2;
                mWeakHandler.sendEmptyMessage(responseStatus);

            }

            @Override
            public void onSuccess(String jsonObject) {
                super.onSuccess(jsonObject);
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_TOPIC_BANNER, jsonObject);
            }
        });

        RequestParams params2 = new RequestParams();
        params2.addQueryStringParameter("page", "1");
        params2.addQueryStringParameter("pageSize", pageSize + "");
        params2.addQueryStringParameter("recommend_level", "2");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getStickTopic, params2, new ParseHttpListener<MoreDataBean>() {
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
                if (moreBean != null)
                    mBannerTopicsBean.hotTopicsBeans = moreBean.getResults();
                onFinish();
                return moreBean;
            }

            @Override
            protected void afterParseData(MoreDataBean object) {

            }


            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
                onFinish();
            }

            public void onFinish() {

                HotTopicEngineImpl.this.responseStatus = responseStatus | 1;
                mWeakHandler.sendEmptyMessage(responseStatus);

            }

            @Override
            public void onSuccess(String jsonObject) {
                super.onSuccess(jsonObject);
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_STICK_TOPIC, jsonObject);
            }
        });


    }

    @Nullable
    private MoreDataBean parse2Topics(String jsonData) {
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
    public HttpHandler refreshDatabySize(int dataSize) {
        return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        return parse2Topics(jsonData);
    }

    public void loadCacheData() {
        String cacheRewards;
        if (mSortType == 0) {
            cacheRewards = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_HOT_TOPICS);
        } else {
            cacheRewards = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_LATEST_TOPICS);
        }
        if (TextUtils.isEmpty(cacheRewards)) {
            return;
        }
        MoreDataBean rewards = parse2Topics(cacheRewards);
        if (rewards != null) {
            mFirstPageTopicsBeans = rewards.getResults();
            setMoreDataBean(rewards);
        }

        String cacheBanner = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_TOPIC_BANNER);
        if (TextUtils.isEmpty(cacheBanner)) {
            return;
        }

        AdBean adBean = DataParse.parseObjectJson(AdBean.class, cacheBanner);
        if (adBean != null) {
            mBannerTopicsBean.adBean = adBean;
        }

        String cacheStickTopics = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_STICK_TOPIC);
        if (TextUtils.isEmpty(cacheStickTopics)) {
            return;
        }
        MoreDataBean stickTopics = parse2Topics(cacheStickTopics);
        if (stickTopics != null) {
            mBannerTopicsBean.hotTopicsBeans = stickTopics.getResults();
        }

        HotTopicEngineImpl.this.responseStatus = responseStatus | 7;
        mWeakHandler.sendEmptyMessage(responseStatus);

    }


}
