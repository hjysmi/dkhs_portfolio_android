/**
 * @Title DKHSClilent.java
 * @Package com.dkhs.portfolio.net
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-11 上午11:19:14
 * @version V1.0
 */
package com.dkhs.portfolio.net;

import java.text.MessageFormat;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.NoAccountMainActivity;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName DKHSClilent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-11 上午11:19:14
 * @version 1.0
 */
public class DKHSClient {

    // static HttpUtils mHttpUtils = new HttpUtils();

    public static void request(HttpMethod method, String url, RequestParams params, final IHttpListener listener) {

        requestServer(new HttpUtils(), method, url, params, listener);

    }

    public static void requestLong(HttpMethod method, String url, RequestParams params, final IHttpListener listener) {

        requestServer(new HttpUtils(10*60 * 1000), method, url, params, listener);

    }

    private static void requestServer(HttpUtils mHttpUtils, HttpMethod method, String url, RequestParams params,
            final IHttpListener listener) {
        // HttpUtils mHttpUtils = new HttpUtils();
        if (NetUtil.checkNetWork()) {

            if (null == params) {
                params = new RequestParams();
            }

            if (!url.contains(DKHSUrl.User.login) && !url.contains(DKHSUrl.User.register)) {
                if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                    params.addHeader("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
                    LogUtils.d("token:" + GlobalParams.ACCESS_TOCKEN);

                } else {

                    // mHttpUtils = new HttpUtils();
                    try {
                        UserEntity user = DbUtils.create(PortfolioApplication.getInstance())
                                .findFirst(UserEntity.class);

                        if (user != null && !TextUtils.isEmpty(user.getAccess_token())) {
                            user = UserEntityDesUtil.decode(user, "ENCODE", ConstantValue.DES_PASSWORD);
                            GlobalParams.ACCESS_TOCKEN = user.getAccess_token();
                            GlobalParams.MOBILE = user.getMobile();
                            if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                                params.addHeader("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
                            } else {
                                LogUtils.e("Authorization token is null,Exit app");
                                // PortfolioApplication.getInstance().exitApp();
                                PromptManager.showToast("Authorization token is null,请重新登录");
                            }
                        }

                    } catch (DbException e) {
                        e.printStackTrace();
                        PromptManager.showToast("Authorization token is null,请重新登录");
                        LogUtils.e("Authorization token is null,Exit app");
                        // PortfolioApplication.getInstance().exitApp();
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
            mHttpUtils.send(method, requestUrl, params, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (null != listener) {
                        listener.requestCallBack();
                    }
                    LogUtils.customTagPrefix = "DKHSClilent";

                    String result = StringDecodeUtil.fromUnicode(responseInfo.result);
                    LogUtils.d("请求成功:" + result);
                    if (null != listener && !listener.isStopRequest()) {
                        listener.onHttpSuccess(result);
                    }

                }

                @Override
                public void onFailure(HttpException error, String msg) {
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
            if (null != listener) {
                listener.requestCallBack();
                listener.onHttpFailure(123, "网络未连接");
            }
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                // On UI thread.
                PromptManager.showNoNetWork();
            } else {
                // Not on UI thread.
            }
        }
    }

    public static void requestByPost(String url, RequestParams params, final IHttpListener listener) {

        request(HttpMethod.POST, getAbsoluteUrl(url), params, listener);
    }

    public static void requestByGet(String urlPrefix, String[] urlPath, final IHttpListener listener) {

        // StringBuilder sbParams = new StringBuilder(url);
        //
        // if (null != params) {
        //
        // for (String value : params) {
        // sbParams.append(value);
        // sbParams.append("/");
        // }
        //
        // }
        requestByGet(urlPrefix, urlPath, null, listener);
    }

    public static void requestByGet(final IHttpListener listener, String urlPrefix, Object... params) {

        // StringBuilder sbParams = new StringBuilder(url);
        //
        // if (null != params) {
        //
        // for (String value : params) {
        // sbParams.append(value);
        // sbParams.append("/");
        // }
        //
        // }
        requestByGet(MessageFormat.format(urlPrefix, params), null, null, listener);
    }

    public static void requestByGet(String urlPrefix, String[] urlPath, List<NameValuePair> params,
            final IHttpListener listener) {

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

        request(HttpMethod.GET, getAbsoluteUrl(sbParams.toString()), null, listener);
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
            /*if (PortfolioPreferenceManager.isRequestByTestServer()) {
                
            } else {
                return DKHSUrl.BASE_DEV_URL + relativeUrl;

            }*/
        	return null;
        }
    }
}
