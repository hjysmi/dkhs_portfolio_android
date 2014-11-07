/**
 * @Title FundsOrderEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName FundsOrderEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午5:07:15
 * @version 1.0
 */
public class UserCombinationEngineImpl extends LoadMoreDataEngine {

    public UserCombinationEngineImpl(ILoadDataBackListener loadListener) {
        super(loadListener);

    }

    @Override
    public void loadMore() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair2 = new BasicNameValuePair("page", (getCurrentpage() + 1) + "");
        params.add(valuePair2);
        DKHSClient.requestByGet(DKHSUrl.Portfolio.portfolio, null, params, this);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loadData() {

        DKHSClient.requestByGet(DKHSUrl.Portfolio.portfolio, null, null, this);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param jsonData
     * @return
     * @return
     */
    @Override
    protected MoreDataBean parseDateTask(String jsonData) {

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        MoreDataBean<CombinationBean> moreBean = (MoreDataBean) gson.fromJson(jsonData,
                new TypeToken<MoreDataBean<CombinationBean>>() {
                }.getType());
        return moreBean;

    }

}
