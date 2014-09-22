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
import com.dkhs.portfolio.net.DKHSClient;
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

        DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.portfolio, params, listener);

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
        if (null != symbols && symbols.size() > 0) {

            Gson gson = new Gson();
            String symbolsValue = gson.toJson(symbols);
            params.addBodyParameter("symbols", symbolsValue);
        }

        DKHSClient.requestByPost(DKHSUrl.Portfolio.create, params, listener);

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

        DKHSClient.requestByPost(DKHSUrl.Portfolio.delete, params, listener);
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

        DKHSClient.requestByPost(DKHSUrl.Portfolio.update + id + "/", params, listener);

    }

    /**
     * 修改持仓组合
     * 
     * @param listener :服务器响应监听
     */
    public void adjustCombination(int id, List<SubmitSymbol> symbols, IHttpListener listener) {

        RequestParams params = new RequestParams();
        // params.addBodyParameter("portfolio", id + "");
        // 调整比例, 格式如：[{"symbol": 101000002,"percent":0.45},{"symbol": 101000004,"percent":0.35}]
        Gson gson = new Gson();
        String symbolsValue = gson.toJson(symbols);
        params.addBodyParameter("symbols", symbolsValue);

        DKHSClient.requestByPost(DKHSUrl.Portfolio.adjust + id + "/", params, listener);

    }

    /**
     * 查询持仓明细
     * 
     * @param listener :服务器响应监听
     */
    public void queryCombinationDetail(long id, IHttpListener listener) {

        // queryCombinationDetailByDay(id, listener);
        String[] params = { String.valueOf(id) };
        DKHSClient.requestByGet(DKHSUrl.Portfolio.create, params, listener);
    }

    public void queryCombinationDetailByDay(int id, String date, IHttpListener listener) {

        DKHSClient.request(HttpMethod.GET, DKHSUrl.Portfolio.create + id + "/?" + date, null, listener);
    }

}
