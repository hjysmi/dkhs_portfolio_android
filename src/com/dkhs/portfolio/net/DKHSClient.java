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
import android.text.TextUtils;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.NoAccountMainActivity;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
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

    static HttpUtils mHttpUtils = new HttpUtils();

    public static void request(HttpMethod method, String url, RequestParams params, final IHttpListener listener) {
        
        if (null == params) {
            params = new RequestParams();
        }

        if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
            params.addHeader("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
            LogUtils.d("token:" + GlobalParams.ACCESS_TOCKEN);

        } else if (!url.contains(DKHSUrl.User.login)) {
            mHttpUtils = new HttpUtils();
            try {
                UserEntity user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);

                if (user != null && !TextUtils.isEmpty(user.getAccess_token())) {
                    user = UserEntityDesUtil.decode(user, "ENCODE", ConstantValue.DES_PASSWORD);
                    GlobalParams.ACCESS_TOCKEN = user.getAccess_token();
                    GlobalParams.MOBILE = user.getMobile();
                    if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                        params.addHeader("Authorization", "Bearer " + GlobalParams.ACCESS_TOCKEN);
                    } else {
                        LogUtils.e("Authorization token is null,Exit app");
                        PortfolioApplication.getInstance().exitApp();
                    }
                }

            } catch (DbException e) {
                e.printStackTrace();
                LogUtils.e("Authorization token is null,Exit app");
                PortfolioApplication.getInstance().exitApp();
            }

        }

        String requestUrl = getAbsoluteUrl(url);

        LogUtils.d("requestUrl:" + requestUrl);

        LogUtils.d("RequestParams:" + params);
        // mHttpUtils.configDefaultHttpCacheExpiry(0);
        // 设置缓存0秒，0秒内直接返回上次成功请求的结果。

        mHttpUtils.configCurrentHttpCacheExpiry(0);
        listener.beforeRequest();
        mHttpUtils.send(method, requestUrl, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                listener.requestCallBack();
                LogUtils.customTagPrefix = "DKHSClilent";

                String result = StringDecodeUtil.fromUnicode(responseInfo.result);
                LogUtils.d("请求成功:" + result);
                if (null != listener && !listener.isStopRequest()) {
                    listener.onHttpSuccess(result);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                listener.requestCallBack();
                // System.out.println("error code:" + error.getExceptionCode());
                LogUtils.customTagPrefix = "DKHSClilent"; // 方便调试时过滤 adb logcat 输出
                // LogUtils.allowI = false; //关闭 LogUtils.i(...) 的 adb log 输出
                LogUtils.e("请求失败:" + msg);

                if (null != listener && !listener.isStopRequest()) {
                    listener.onHttpFailure(error.getExceptionCode(), msg);
                }

            }
        });
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

    private static String getAbsoluteUrl(String relativeUrl) {
        if (relativeUrl.contains("http://")) {
            return relativeUrl;
        } else {
            return DKHSUrl.BASE_URL + relativeUrl;
        }
    }
}
