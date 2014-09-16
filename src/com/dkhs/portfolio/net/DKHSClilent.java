/**
 * @Title DKHSClilent.java
 * @Package com.dkhs.portfolio.net
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-11 上午11:19:14
 * @version V1.0
 */
package com.dkhs.portfolio.net;

import org.apache.http.conn.ConnectTimeoutException;
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
        if (null == params) {
            params = new RequestParams();
        }
        params.addHeader("Authorization", "Bearer " + "af6825011ea958732dfdcc8b6ba10bef5f25249a");
        // params.addHeader("Authorization", "Bearer " + "0852e9e636399c617126c17a1e6dd5b27abe8811");
        String requestUrl = getAbsoluteUrl(url);
        LogUtils.d("requestUrl:" + requestUrl);
        LogUtils.d("RequestParams:" + params);
        mHttpUtils.send(method, requestUrl, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.customTagPrefix = "DKHSClilent";

                String result = StringDecodeUtil.fromUnicode(responseInfo.result);
                LogUtils.d("请求成功:" + result);

                listener.onHttpSuccess(result);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.println("error code:" + error.getExceptionCode());
                LogUtils.customTagPrefix = "DKHSClilent"; // 方便调试时过滤 adb logcat 输出
                // LogUtils.allowI = false; //关闭 LogUtils.i(...) 的 adb log 输出
                LogUtils.e("请求失败:" + msg);

                listener.onHttpFailure(error.getExceptionCode(), msg);

            }
        });
    }

    public static void requestByPost(String url, RequestParams params, final IHttpListener listener) {
        
        request(HttpMethod.POST, getAbsoluteUrl(url), params, listener);
    }

    public static void requestByGet(String url, String[] params, final IHttpListener listener) {

        StringBuilder sbParams = new StringBuilder(url);

        for (String value : params) {
            sbParams.append(value);
            sbParams.append("/");
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
