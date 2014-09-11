/**
 * @Title DKHSClilent.java
 * @Package com.dkhs.portfolio.net
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-11 上午11:19:14
 * @version V1.0
 */
package com.dkhs.portfolio.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.dkhs.portfolio.common.ConstantValue;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName DKHSClilent
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-11 上午11:19:14
 * @version 1.0
 */
public class DKHSClilent {
    private static HttpUtils mHttpUtils = new HttpUtils();

    public static void post(String url, RequestParams params, final IHttpListener listener) {

    }

    public static void request(HttpMethod method, String url, RequestParams params, final IHttpListener listener) {
        params.addHeader("Authorization", "Bearer " + "0852e9e636399c617126c17a1e6dd5b27abe7511");
        mHttpUtils.send(method, getAbsoluteUrl(url), params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.customTagPrefix = "DKHSClilent";

                LogUtils.d("请求成功:" + responseInfo.result);
                try {
                    listener.onHttpSuccess(new JSONObject(responseInfo.result));
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LogUtils.customTagPrefix = "DKHSClilent"; // 方便调试时过滤 adb logcat 输出
                // LogUtils.allowI = false; //关闭 LogUtils.i(...) 的 adb log 输出
                LogUtils.d(msg);
                listener.onHttpFailure(ConstantValue.HTTP_ERROR_NET, msg);

            }
        });
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        if (relativeUrl.contains("http://")) {
            return relativeUrl;
        } else {
            return DKHSUrl.BASE_URL + relativeUrl;
        }
    }
}
