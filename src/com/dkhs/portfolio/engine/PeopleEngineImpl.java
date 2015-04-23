package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.UserEntity;
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
 * @Description 获取关注的人,和粉丝网络请求类
 */
public class PeopleEngineImpl extends LoadMoreDataEngine {

    private final UserEntity user;
    private   int page=1;
    /**
     * 默认显示一页20条数据
     */
    private   static  final  int pageSize=20;


    private TYPE type;

    public PeopleEngineImpl(ILoadDataBackListener loadListener, TYPE type){
        super(loadListener);
        this.type=type;
         user = UserEngineImpl.getUserEntity();
    }

    @Override
    public HttpHandler loadMore() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", page+"");
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");




        return DKHSClient.request(HttpRequest.HttpMethod.GET,  String.format(type.getUrl(),user.getId()), params, this);
    }

    @Override
    public HttpHandler loadData() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", page+"");
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, String.format(type.getUrl(),user.getId()), params, this);

    }

    @Override
    public HttpHandler refreshDatabySize(int dataSize) {


        return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean<CombinationBean> moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {

            try {

                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<PeopleBean>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }

  static public   enum  TYPE{
        FRIENDS(DKHSUrl.Portfolio.get_frients), FOLLOWERS(DKHSUrl.Portfolio.get_followers);

        private TYPE(String url){
            this.url = url;
        }
        private String url;

        public String getUrl() {
            return url;
        }
    }
}
