package com.dkhs.portfolio.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialBannerBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialFinancingBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialLineBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.FundHomeEngineImpl;
import com.dkhs.portfolio.engine.MarketCenterStockEngineImple;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.PreLoadEvent;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

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
        new Thread(new LoadStockMarketRunnable()).start();
        new Thread(new MarketFundsRunnable()).start();
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
            DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.BBS.getRewardList, params, new ParseHttpListener<MoreDataBean>() {
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
                    BusProvider.getInstance().post(new PreLoadEvent(PreLoadEvent.TYPE_REWARD));
                    super.onSuccess(jsonObject);
                }
            });
        }
    }

    class LoadStockMarketRunnable implements Runnable {
        @Override
        public void run() {
            MarketCenterStockEngineImple.loadAllMarkets(new ParseHttpListener<MoreDataBean>() {
                @Override
                public void onSuccess(String jsonObject) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_STOCK_ALL_MARKET_JSON, jsonObject);
                    BusProvider.getInstance().post(new PreLoadEvent(PreLoadEvent.TYPE_MARKET_STOCK));
                    super.onSuccess(jsonObject);
                }

                @Override
                protected MoreDataBean parseDateTask(String jsonData) {
                    return null;
                }

                @Override
                protected void afterParseData(MoreDataBean object) {

                }
            });
        }
    }

    class MarketFundsRunnable implements Runnable {
        int requestcount = 0;

        private void needRefresh() {
            if (++requestcount == 5) {
                requestcount = 0;
                BusProvider.getInstance().post(new PreLoadEvent(PreLoadEvent.TYPE_MARKET_FUND));
            }
        }

        @Override
        public void run() {
            FundHomeEngineImpl engine = new FundHomeEngineImpl();
            engine.getMarketInfo(new ParseHttpListener<List<StockQuotesBean>>() {
                @Override
                protected List<StockQuotesBean> parseDateTask(String jsonData) {
                    return null;
                }

                @Override
                protected void afterParseData(List<StockQuotesBean> object) {

                }

                @Override
                public void onSuccess(String jsonObject) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_MAIN_VALUE_JSON, jsonObject);
                    needRefresh();
                    super.onSuccess(jsonObject);
                }
            });
            engine.getRecommendBanners(new ParseHttpListener<List<RecommendFundSpecialBannerBean>>() {
                @Override
                protected List<RecommendFundSpecialBannerBean> parseDateTask(String jsonData) {
                    return null;
                }

                @Override
                protected void afterParseData(List<RecommendFundSpecialBannerBean> object) {

                }

                @Override
                public void onSuccess(String jsonObject) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_BANNERS_JSON, jsonObject);
                    needRefresh();
                    super.onSuccess(jsonObject);
                }
            });
            engine.getRecommendSpecials(new ParseHttpListener<List<RecommendFundSpecialLineBean>>() {
                @Override
                protected List<RecommendFundSpecialLineBean> parseDateTask(String jsonData) {
                    return null;
                }

                @Override
                protected void afterParseData(List<RecommendFundSpecialLineBean> object) {

                }

                @Override
                public void onSuccess(String jsonObject) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_LINES_JSON, jsonObject);
                    needRefresh();
                    super.onSuccess(jsonObject);
                }
            });
            engine.getRecommendSpecialFinancings(new ParseHttpListener<List<RecommendFundSpecialFinancingBean>>() {
                @Override
                protected List<RecommendFundSpecialFinancingBean> parseDateTask(String jsonData) {
                    return null;
                }

                @Override
                protected void afterParseData(List<RecommendFundSpecialFinancingBean> object) {
                }

                @Override
                public void onSuccess(String jsonObject) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_FINANCINGS_JSON, jsonObject);
                    needRefresh();
                    super.onSuccess(jsonObject);
                }
            });
            engine.getRecommendFundManager(new ParseHttpListener<List<FundManagerBean>>() {


                @Override
                protected List<FundManagerBean> parseDateTask(String jsonData) {
                    return null;
                }

                @Override
                protected void afterParseData(List<FundManagerBean> object) {

                }

                @Override
                public void onSuccess(String jsonObject) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_FUNDMANAGERS_JSON, jsonObject);
                    needRefresh();
                    super.onSuccess(jsonObject);
                }
            });


        }
    }
}
