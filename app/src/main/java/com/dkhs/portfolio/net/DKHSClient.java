/**
 * @Title DKHSClilent.java
 * @Package com.dkhs.portfolio.net
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-11 上午11:19:14
 * @version V1.0
 */
package com.dkhs.portfolio.net;

import android.os.Looper;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * @ClassName DKHSClilent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-11 上午11:19:14
 * @version 1.0
 */
public class DKHSClient {

    // static HttpUtils mHttpUtils = new HttpUtils();

    public static HttpHandler
            request(HttpMethod method, String url, RequestParams params, final IHttpListener listener) {

        return requestServer(new HttpUtils(), method, url, params, listener, true);

    }
    public static void  requestSync(HttpMethod method, String url, RequestParams params, final IHttpListener listener) {

         requestSyncServer(new HttpUtils(), method, url, params, listener, true);

    }

    public static HttpHandler request(HttpMethod method, String url, RequestParams params,
            final IHttpListener listener, boolean isShowTip) {

        return requestServer(new HttpUtils(), method, url, params, listener, isShowTip);

    }

    public static HttpHandler requestNotTip(HttpMethod method, String url, RequestParams params,
            final IHttpListener listener) {

        return requestServer(new HttpUtils(), method, url, params, listener, false);

    }

    public static HttpHandler requestLong(HttpMethod method, String url, RequestParams params,
            final IHttpListener listener) {

        return requestServer(new HttpUtils(10 * 60 * 1000), method, url, params, listener, false);

    }

    private static HttpHandler requestServer(HttpUtils mHttpUtils, HttpMethod method, String url, RequestParams params,
                                               final IHttpListener listener, boolean isShowTip) {
        final CacheHelper cacheHelper = new CacheHelper(method, url, params);

        if (NetUtil.checkNetWork()) {

            if (null == params) {
                params = new RequestParams();
            }

            if (!url.contains(DKHSUrl.User.login) && !url.contains(DKHSUrl.User.register)) {
                if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                    params.addHeader("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
                    LogUtils.d("token:" + GlobalParams.ACCESS_TOCKEN);

                } else {

                    UserEntity user = UserEngineImpl.getUserEntity();
                    if (user != null && !TextUtils.isEmpty(user.getAccess_token())) {
                        GlobalParams.ACCESS_TOCKEN = user.getAccess_token();
                        GlobalParams.MOBILE = user.getMobile();
                        if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                            params.addHeader("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
                        } else {
                            LogUtils.e("Authorization token is null,Exit app");
                            // PortfolioApplication.getInstance().exitApp();
                        }
                    }

                }

            }

            String requestUrl = getAbsoluteUrl(url);

            LogUtils.d("requestUrl:" + requestUrl);

            LogUtils.d("RequestParams:" + new Gson().toJson(params));
            // mHttpUtils.configDefaultHttpCacheExpiry(0);
            // 设置缓存0秒，0秒内直接返回上次成功请求的结果。

            mHttpUtils.configCurrentHttpCacheExpiry(0);
            if (null != listener) {
                listener.beforeRequest();
            }
            // mHttpUtils.sendSync(method, requestUrl)
            return mHttpUtils.send(method, requestUrl, params, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (null != listener) {
                        listener.requestCallBack();
                    }
                    LogUtils.customTagPrefix = "DKHSClilent";

                    String result = StringDecodeUtil.fromUnicode(responseInfo.result);
//                    LogUtils.d("请求成功:" + DebugJsonFormatUtil.format(result));
                    LogUtils.d("请求成功:" + result);
                    if (null != listener && !listener.isStopRequest()) {
                        listener.onHttpSuccess(result);
                    }

                    if (cacheHelper.isCacheUrl()) {
                        cacheHelper.storeURLResponse(GlobalParams.ACCESS_TOCKEN, result);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    if (cacheHelper.isCacheUrl()) {
                        cacheHelper.queryURLStore(GlobalParams.ACCESS_TOCKEN, listener);
                    }
                    if (null != listener) {
                        listener.requestCallBack();
                    }
                    // System.out.println("error code:" + error.getExceptionCode());
                    LogUtils.customTagPrefix = "DKHSClilent"; // 方便调试时过滤 adb logcat 输出
                    // LogUtils.allowI = false; //关闭 LogUtils.i(...) 的 adb log 输出
                    LogUtils.e("请求失败:" + msg);

                    if (null != listener && !listener.isStopRequest()) {
                        listener.onHttpFailure(error.getExceptionCode(), msg);
                    }

                }
            });
        } else {

            if (cacheHelper.isCacheUrl()) {
                cacheHelper.queryURLStore(GlobalParams.ACCESS_TOCKEN, listener);
            }

            if (null != listener) {
                listener.requestCallBack();
                listener.onHttpFailure(123, "网络未连接");
            }
            if (Looper.getMainLooper().getThread() == Thread.currentThread() && isShowTip) {
                // On UI thread.
                PromptManager.showNoNetWork();
            } else {
                // Not on UI thread.
            }
            return null;
        }
    }


    private static void requestSyncServer(HttpUtils mHttpUtils, HttpMethod method, String url, RequestParams params,
                                             final IHttpListener listener, boolean isShowTip) {
        final CacheHelper cacheHelper = new CacheHelper(method, url, params);

        if (NetUtil.checkNetWork()) {

            if (null == params) {
                params = new RequestParams();
            }

            if (!url.contains(DKHSUrl.User.login) && !url.contains(DKHSUrl.User.register)) {
                if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                    params.addHeader("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
                    LogUtils.d("token:" + GlobalParams.ACCESS_TOCKEN);

                } else {

                    UserEntity user = UserEngineImpl.getUserEntity();
                    if (user != null && !TextUtils.isEmpty(user.getAccess_token())) {
                        GlobalParams.ACCESS_TOCKEN = user.getAccess_token();
                        GlobalParams.MOBILE = user.getMobile();
                        if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                            params.addHeader("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
                        } else {
                            LogUtils.e("Authorization token is null,Exit app");
                            // PortfolioApplication.getInstance().exitApp();
                        }
                    }

                }

            }

            String requestUrl = getAbsoluteUrl(url);

            LogUtils.d("requestUrl:" + requestUrl);

            LogUtils.d("RequestParams:" + new Gson().toJson(params));
            // mHttpUtils.configDefaultHttpCacheExpiry(0);
            // 设置缓存0秒，0秒内直接返回上次成功请求的结果。

            mHttpUtils.configCurrentHttpCacheExpiry(0);
            if (null != listener) {
                listener.beforeRequest();
            }
            // mHttpUtils.sendSync(method, requestUrl)
            try {
                ResponseStream responseStream= mHttpUtils.sendSync(method, requestUrl, params);
              String s=  responseStream.readString();
                listener.onHttpSuccess(s);
            } catch (HttpException e) {
                listener.onHttpFailure(-1,e);
                e.printStackTrace();
            } catch (IOException e) {
                listener.onHttpFailure(-2,e);
                e.printStackTrace();
            }
        } else {

            if (cacheHelper.isCacheUrl()) {
                cacheHelper.queryURLStore(GlobalParams.ACCESS_TOCKEN, listener);
            }

            if (null != listener) {
                listener.requestCallBack();
                listener.onHttpFailure(123, "网络未连接");
            }
            if (Looper.getMainLooper().getThread() == Thread.currentThread() && isShowTip) {
                // On UI thread.
                PromptManager.showNoNetWork();
            } else {
                // Not on UI thread.
            }

        }
    }

    public static HttpHandler requestByPost(String url, RequestParams params, final IHttpListener listener) {

        return request(HttpMethod.POST, getAbsoluteUrl(url), params, listener);
    }

    public static HttpHandler requestByGet(String urlPrefix, String[] urlPath, final IHttpListener listener) {

        return requestByGet(urlPrefix, urlPath, null, listener, true);
    }

    public static HttpHandler requestByGet(String urlPrefix, String[] urlPath, final IHttpListener listener,
            boolean isShowTip) {

        return requestByGet(urlPrefix, urlPath, null, listener, isShowTip);
    }

    public static HttpHandler requestByGet(final IHttpListener listener, String urlPrefix, Object... params) {

        return requestByGet(MessageFormat.format(urlPrefix, params), null, null, listener, true);
    }

    public static HttpHandler requestByGet(String urlPrefix, String[] urlPath, List<NameValuePair> params,
            final IHttpListener listener) {
        return requestByGet(urlPrefix, urlPath, params, listener, true);
    }

    public static HttpHandler requestByGet(String urlPrefix, String[] urlPath, List<NameValuePair> params,
            final IHttpListener listener, boolean isShowTip) {

        StringBuilder sbParams = new StringBuilder(urlPrefix);

        if (null != urlPath) {

            for (String value : urlPath) {
                if (!TextUtils.isEmpty(value)) {
                    sbParams.append(value);
                    sbParams.append("/");

                }

            }
        }
        if (params != null) {
            sbParams.append("?");
            for (NameValuePair p : params) {
                sbParams.append(p.getName()).append('=').append(p.getValue()).append('&');
            }
            // sbParams.setCharAt(0, '?');// 将第一个的 &替换为 ？
        }

        return request(HttpMethod.GET, getAbsoluteUrl(sbParams.toString()), null, listener, isShowTip);
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        if (relativeUrl.contains("http://") || relativeUrl.contains("https://")) {
            return relativeUrl;
        } else {
            switch (PortfolioPreferenceManager.getIntValue(PortfolioPreferenceManager.KEY_APP_URL)) {
                case 0:
                    return DKHSUrl.BASE_DEV_URL + relativeUrl;
                case 1:
                    return DKHSUrl.BASE_TEST_URL + relativeUrl;
                case 2:
                    return DKHSUrl.BASE_DEV_MAIN + relativeUrl;
                case 3:
                    return DKHSUrl.BASE_DEV_TAG + relativeUrl;
                default:
                    break;
            }
            /*
             * if (PortfolioPreferenceManager.isRequestByTestServer()) {
             * 
             * } else {
             * return DKHSUrl.BASE_DEV_URL + relativeUrl;
             * 
             * }
             */
            return null;
        }
    }
}
