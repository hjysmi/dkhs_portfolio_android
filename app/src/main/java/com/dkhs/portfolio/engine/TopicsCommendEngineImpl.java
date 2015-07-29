package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.MessageFormat;

/**
 * @author zwm
 * @version 1.0
 * @ClassName TopicsEngineImpl
 * @date 2015/4/23.13:39
 * @Description
 */
public class TopicsCommendEngineImpl extends LoadMoreDataEngine {


    /**
     * 默认显示一页20条数据
     */
    private static final int pageSize = 20;

    private String topicsId;
    private String sort;


    public enum  SortType{
        latest("latest"),earliest("earliest"),best("best");
        private String values;
        SortType(String values){
            this.values=values;
        }
        public String getValues() {
            return values;
        }
    }


    public TopicsCommendEngineImpl(ILoadDataBackListener loadListener, String topicsId) {
        super(loadListener);
        this.topicsId = topicsId;
    }

    public TopicsCommendEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);
    }


    /**
     * 排序规则 latest: 最新 earliest: 最早 best: 最赞， 默认值为latest
     */
    public void loadData(SortType sort) {
        this.sort = sort.getValues();
        loadData();
    }


    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("sort",  sort);
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, MessageFormat.format(DKHSUrl.BBS.getCommend, topicsId), params, this);
    }

    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("pageSize", pageSize + "");
        params.addQueryStringParameter("sort",  sort);
        return DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getLatestTopic, params, this);

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
