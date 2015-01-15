/**
 * @Title DKHSRequestCallBack.java
 * @Package com.dkhs.portfolio.net
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-1-6 下午5:19:58
 * @version V1.0
 */
package com.dkhs.portfolio.net;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName DKHSRequestCallBack
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-1-6 下午5:19:58
 * @version 1.0
 */
public class DKHSRequestCallBack {
    private IHttpListener mHttpListener;
    public RequestCallBack<String> requestCallBack = new RequestCallBack<String>() {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            if (null != mHttpListener) {
                mHttpListener.requestCallBack();
            }
            LogUtils.customTagPrefix = "DKHSClilent";

            String result = StringDecodeUtil.fromUnicode(responseInfo.result);
            LogUtils.d("请求成功:" + result);
            if (null != mHttpListener && !mHttpListener.isStopRequest()) {
                mHttpListener.onHttpSuccess(result);
            }

        }

        @Override
        public void onFailure(HttpException error, String msg) {
            if (null != mHttpListener) {
                mHttpListener.requestCallBack();
            }
            // System.out.println("error code:" + error.getExceptionCode());
            LogUtils.customTagPrefix = "DKHSClilent"; // 方便调试时过滤 adb logcat 输出
            // LogUtils.allowI = false; //关闭 LogUtils.i(...) 的 adb log 输出
            LogUtils.e("请求失败:" + msg);

            if (null != mHttpListener && !mHttpListener.isStopRequest()) {
                mHttpListener.onHttpFailure(error.getExceptionCode(), msg);
            }

        }
    };

    public DKHSRequestCallBack(final IHttpListener listener) {
        // this.mHttpListener = listener;
        this.mHttpListener = new IHttpListener() {

            @Override
            public void stopRequest(boolean isStop) {
                // TODO Auto-generated method stub
                listener.stopRequest(isStop);

            }

            @Override
            public void requestCallBack() {
                // TODO Auto-generated method stub
                listener.requestCallBack();
            }

            @Override
            public void onHttpSuccess(String jsonObject) {
                // TODO Auto-generated method stub
                listener.onHttpSuccess(jsonObject);

            }

            @Override
            public void onHttpFailure(int errCode, Throwable err) {

                listener.onHttpFailure(errCode, err);
            }

            @Override
            public void onHttpFailure(int errCode, String errMsg) {
                // TODO Auto-generated method stub
                listener.onHttpFailure(errCode, errMsg);

            }

            @Override
            public boolean isStopRequest() {
                // TODO Auto-generated method stub
                return listener.isStopRequest();
            }

            // @Override
            // public void cancelRequest() {
            // listener.cancelRequest();
            // if(null!=requestCallBack){
            // requestCallBack.can
            // }
            // }

            @Override
            public void beforeRequest() {
                // TODO Auto-generated method stub

            }
        };
    }

}
