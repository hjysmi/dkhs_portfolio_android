/**
 * @Title CacheHelper.java
 * @Package com.dkhs.portfolio.net
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-1-20 上午10:29:56
 * @version V1.0
 */
package com.dkhs.portfolio.net;

import org.apache.http.NameValuePair;

import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UrlStoreBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName CacheHelper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-1-20 上午10:29:56
 * @version 1.0
 */
public class CacheHelper {

    private String mQueryUrl;
    private UrlStoreBean mStoreBean;
    private boolean isCacheUrl;

    public CacheHelper(HttpMethod method, String url, RequestParams params) {
        isCacheUrl(url);
        if (isCacheUrl) {
            mQueryUrl = getQueryUrl(method, url, params);
        }
    }

    public void isCacheUrl(String url) {
        // isCacheUrl =
        for (String storeurl : DKHSUrl.storeURLList) {
            if (url.equals(storeurl)) {
                isCacheUrl = true;
            }
        }
    }

    public void queryURLStore(final String autho, final IHttpListener listener) {
        new Thread() {
            public void run() {
                DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
                try {
                    UrlStoreBean storeBean = db.findFirst(Selector.from(UrlStoreBean.class)
                            .where("url", "=", mQueryUrl).and("authorization", "=", autho));
                    if (null != storeBean && !TextUtils.isEmpty(storeBean.getResponseJson())) {
                        if (null != listener && !listener.isStopRequest()) {
                            Log.e("CacheHelper", "listener.onHttpSuccess ");

                            listener.onHttpSuccess(storeBean.getResponseJson());
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    private String getQueryUrl(HttpMethod method, String url, RequestParams params) {
        StringBuilder sbUrlKey = new StringBuilder(url);
        if (null != params) {

            if (method == HttpMethod.GET && null != params.getQueryStringParams()) {
                for (NameValuePair valuePair : params.getQueryStringParams()) {
                    sbUrlKey.append(valuePair.getName());
                    sbUrlKey.append("=");
                    sbUrlKey.append(valuePair.getValue());
                }
            }
            if (method == HttpMethod.POST && null != params.getBodyParams()) {
                for (NameValuePair valuePair : params.getBodyParams()) {
                    sbUrlKey.append(valuePair.getName());
                    sbUrlKey.append("=");
                    sbUrlKey.append(valuePair.getValue());
                }
            }
        }
        return sbUrlKey.toString();
    }

    public void storeURLResponse(final String autho, final String repsonJson) {
        new Thread() {
            public void run() {
                UrlStoreBean storeBean = new UrlStoreBean();
                storeBean.setAuthorization(autho);
                storeBean.setUrl(mQueryUrl);
                storeBean.setResponseJson(repsonJson);
                DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
                try {
                    // db.delete(storeBean);
                    // db.replace(storeBean);
                    db.saveOrUpdate(storeBean);
                    // db.update(storeBean, "responseJson");
                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            };
        }.start();
    }

    public boolean isCacheUrl() {
        return isCacheUrl;
    }

    public void setCacheUrl(boolean isCacheUrl) {
        this.isCacheUrl = isCacheUrl;
    }

}
