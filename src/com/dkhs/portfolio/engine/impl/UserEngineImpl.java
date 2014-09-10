package com.dkhs.portfolio.engine.impl;

import android.os.Handler;

import com.dkhs.portfolio.bean.User;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.UserEngine;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

public class UserEngineImpl extends UserEngine {

    @Override
    public void login(User user, final Handler handler) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("client_id", ConstantValue.CLIENT_ID);
        params.addBodyParameter("client_secret", ConstantValue.CLIENT_SECERET);
        params.addBodyParameter("grant_type", "password");
        params.addBodyParameter("username", user.getUsername());
        params.addBodyParameter("password", user.getPassword());
        utils.send(HttpRequest.HttpMethod.POST, ConstantValue.GET_ACCESSTOKEN_URL, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        sendMsg(handler, ConstantValue.HTTP_ERROR_NET);
                        LogUtils.customTagPrefix = "UserEngineImpl"; // 方便调试时过滤 adb logcat 输出
                        // LogUtils.allowI = false; //关闭 LogUtils.i(...) 的 adb log 输出
                        LogUtils.d(msg);
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
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                });
    }

    @Override
    public void register(final String username, String mobile, String gender, final String password,
            final Handler handler) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("password", password);
        params.addBodyParameter("mobile", mobile);
        utils.send(HttpRequest.HttpMethod.POST, ConstantValue.REGISTER_URL, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                sendMsg(handler, ConstantValue.HTTP_ERROR_NET);
                LogUtils.customTagPrefix = "UserEngineImpl"; // 方便调试时过滤 adb logcat 输出
                // LogUtils.allowI = false; //关闭 LogUtils.i(...) 的 adb log 输出
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.customTagPrefix = "UserEngineImpl"; // 方便调试时过滤 adb logcat 输出
                LogUtils.i(responseInfo.result);
                // BaseEntity entity= JsonUtil.parseJsonObject(responseInfo.result, BaseEntity.class);
                // if(entity.isStatus()){//请求数据成功
                // // sendMsg(handler, ConstantValue.HTTP_OK, entity.getData());
                // login(new User(username, password), handler);
                // }else{//请求失败
                // sendMsg(handler, ConstantValue.HTTP_ERROR, entity.getData());
                // }
            }

            @Override
            public void onStart() {
                super.onStart();
            }
        });
    }

}
