package com.dkhs.portfolio.engine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.AppBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.UpdateDialog;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

public class AppUpdateEngine {


    public Context mContext;

    private static boolean isNeedCheck = true;

    public AppUpdateEngine(Context context) {
        mContext = context;
    }

    public void checkVersion() {

        getAppVersion("portfolio_android", updateAppVersionListener);
    }

    public static void getAppVersion(String appcode, ParseHttpListener<Object> listener) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("app_code", appcode);
        // DKHSClient.requestByGet(DKHSUrl.News.newstext +id, null, this);
        DKHSClient.request(HttpRequest.HttpMethod.GET, DKHSUrl.User.get_version + appcode, null, listener);
    }

    ParseHttpListener updateAppVersionListener = new ParseHttpListener<AppBean>() {

        @Override
        protected AppBean parseDateTask(String jsonData) {

            return DataParse.parseObjectJson(AppBean.class, jsonData);
        }

        @Override
        protected void afterParseData(AppBean object) {

            if (null != object) {
                try {
                    isNeedCheck = false;
                    final AppBean bean = object;
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_UPDATE_INFO, new Gson().toJson(object));
                    PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                    String version = info.versionName;

                    if (object.isNewVersion(version)) {

                        showUpdateDialog(object);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void showUpdateDialog(AppBean object) {

        //版本是否过滤
        if (!object.getVersion().equals(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_VERSIONY))) {
            UpdateDialog alert = new UpdateDialog(mContext);
            alert.showByAppBean(object);
        }
    }


}
