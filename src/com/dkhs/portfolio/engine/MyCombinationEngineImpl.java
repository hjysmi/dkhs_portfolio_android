package com.dkhs.portfolio.engine;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.User;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.UserEngine;
import com.dkhs.portfolio.net.DKHSClilent;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

public class MyCombinationEngineImpl {

    /**
     * 查询我的组合列表
     * 
     * @param listener :服务器响应监听
     */
    public void getCombinationList(IHttpListener listener) {
        RequestParams params = new RequestParams();

        DKHSClilent.request(HttpMethod.GET, DKHSUrl.Portfolio.portfolio, params, listener);

    }

    /**
     * 创建我的组合
     * 
     * @param listener :服务器响应监听
     */
    public void createCombination(IHttpListener listener) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", ConstantValue.CLIENT_ID);
        params.addBodyParameter("description", ConstantValue.CLIENT_ID);
        // [{"symbol": 101000001,"percent":0.45},{"symbol": 101000002,"percent":0.35}]
        params.addBodyParameter("symbols", ConstantValue.CLIENT_ID);

        DKHSClilent.request(HttpMethod.POST, DKHSUrl.Portfolio.portfolio, params, listener);

    }

    /**
     * 删除我的组合
     * 
     * @param listener :服务器响应监听
     */
    public void deleteCombination(long id, IHttpListener listener) {

        // {"id": 14, "name": "我的组合4", "description": "", "created_at": "2014-09-11T06:00:38Z", "net_value": 0.0}

        DKHSClilent.request(HttpMethod.DELETE, DKHSUrl.Portfolio.portfolio + id + "/", null, listener);

    }

    /**
     * 删除我的组合
     * 
     * @param listener :服务器响应监听
     */
    public void queryCombinationDetail(long id, IHttpListener listener) {
        System.out.println("queryCombination id:" + id);
        String[] params = { String.valueOf(id) };

        // DKHSClilent.request(HttpMethod.GET, DKHSUrl.Portfolio.portfolio + id + "/", params, listener);
        DKHSClilent.requestByGet(DKHSUrl.Portfolio.portfolio, params, listener);

    }

    public void login(User user, final IHttpListener listener) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();

        params.addHeader("Authorization", "Bearer " + "GlobalParams.ACCESS_TOCKEN");

        params.addBodyParameter("client_id", ConstantValue.CLIENT_ID);
        params.addBodyParameter("client_secret", ConstantValue.CLIENT_SECERET);
        params.addBodyParameter("grant_type", "password");
        params.addBodyParameter("username", user.getUsername());
        params.addBodyParameter("password", user.getPassword());
        utils.send(HttpRequest.HttpMethod.POST, ConstantValue.GET_ACCESSTOKEN_URL, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // sendMsg(handler, ConstantValue.HTTP_ERROR_NET);
                        LogUtils.customTagPrefix = "UserEngineImpl"; // 方便调试时过滤 adb logcat 输出
                        // LogUtils.allowI = false; //关闭 LogUtils.i(...) 的 adb log 输出
                        LogUtils.d(msg);
                        listener.onHttpFailure(ConstantValue.HTTP_ERROR_NET, msg);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // TODO Auto-generated method stub
                        // AccessTokenEntity entity =
                        // JsonUtil.parseJsonObject(responseInfo.result,
                        // AccessTokenEntity.class);
                        // long expires_in=entity.getExpires_in();
                        // entity.setExpires_in(expires_in
                        // + System.currentTimeMillis());
                        // sendMsg(handler, ConstantValue.HTTP_OK, entity);
                        System.out.println("请求成功");

                        listener.onHttpSuccess(responseInfo.result);

                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                });
    }

    public static void testRequest() {
        MyCombinationEngineImpl combinEngine = new MyCombinationEngineImpl();
        User user = new User("Test", "test");
        ParseHttpListener<CombinationBean> parseListener = new ParseHttpListener<CombinationBean>() {

            @Override
            protected CombinationBean parseDateTask(String jsonData) {

                // return DataParse.parseObjectJson(CombinationBean.class, jsonData);
                return null;
            }

            @Override
            protected void afterParseData(CombinationBean object) {
                LogUtils.d("Request success");

            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                // TODO Auto-generated method stub
                super.onFailure(errCode, errMsg);
            }

        };
        combinEngine.login(user, parseListener);
    }

}
