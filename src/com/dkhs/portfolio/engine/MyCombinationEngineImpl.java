package com.dkhs.portfolio.engine;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SubmitSymbol;
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
    public void createCombination(String name, String desciption, List<SubmitSymbol> symbols, IHttpListener listener) {
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(name)) {
            params.addBodyParameter("name", name);
        }
        if (!TextUtils.isEmpty(desciption)) {
            params.addBodyParameter("description", desciption);
        }
        // [{"symbol": 101000001,"percent":0.45},{"symbol": 101000002,"percent":0.35}]
        Gson gson = new Gson();
        String symbolsValue = gson.toJson(symbols);
        params.addBodyParameter("symbols", symbolsValue);

        DKHSClilent.requestByPost(DKHSUrl.Portfolio.portfolio, params, listener);

    }

    /**
     * 删除我的组合
     * 
     * @param listener :服务器响应监听
     */
    public void deleteCombination(String Ids, IHttpListener listener) {

        RequestParams params = new RequestParams();
        // params.addBodyParameter("portfolios ", "1,2,3");
        params.addBodyParameter("portfolios", Ids);

        DKHSClilent.requestByPost(DKHSUrl.Portfolio.delete, params, listener);
        // DKHSClilent.request(HttpMethod.DELETE, DKHSUrl.Portfolio.portfolio + id + "/", null, listener);

    }

    /**
     * 修改组合名称、描述
     * 
     * @param listener :服务器响应监听
     */
    public void updateCombination(String id, String name, String desc, IHttpListener listener) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("name", name);
        params.addBodyParameter("description", desc);

        DKHSClilent.requestByPost(DKHSUrl.Portfolio.update + id + "/", params, listener);

    }

    /**
     * 修改持仓组合
     * 
     * @param listener :服务器响应监听
     */
    public void adjustCombination(int id, List<SubmitSymbol> symbols, IHttpListener listener) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("portfolio", id+"");
        // 调整比例, 格式如：[{"symbol": 101000002,"percent":0.45},{"symbol": 101000004,"percent":0.35}]
        Gson gson = new Gson();
        String symbolsValue = gson.toJson(symbols);
        params.addBodyParameter("symbols", symbolsValue);

        DKHSClilent.requestByPost(DKHSUrl.Portfolio.adjust, params, listener);

    }

    /**
     * 查询持仓明细
     * 
     * @param listener :服务器响应监听
     */
    public void queryCombinationDetail(long id, IHttpListener listener) {
        System.out.println("queryCombination id:" + id);
        String[] params = { String.valueOf(id) };

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
