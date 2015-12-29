package com.dkhs.portfolio.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RewardsPreLoadEvent;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

public class PreLoadService extends Service {
    public PreLoadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new LoadRewardsRunnable()).start();
        return super.onStartCommand(intent, flags, startId);

    }

    class LoadRewardsRunnable implements Runnable {
        @Override
        public void run() {
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("page", "1");
            params.addQueryStringParameter("pageSize", 20 + "");
            params.addQueryStringParameter("recommend_level", "");
            params.addQueryStringParameter("reward_order", "0");
            params.addQueryStringParameter("content_type", "40");
            DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getRewardList,params,new ParseHttpListener<MoreDataBean>(){
                @Override
                protected MoreDataBean parseDateTask(String jsonData) {
                    return null;
                }

                @Override
                protected void afterParseData(MoreDataBean object) {

                }

                @Override
                public void onSuccess(String jsonObject) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_REWARDS, jsonObject);
                    BusProvider.getInstance().post(new RewardsPreLoadEvent());
                    super.onSuccess(jsonObject);
                }
            });
        }
    }
}
