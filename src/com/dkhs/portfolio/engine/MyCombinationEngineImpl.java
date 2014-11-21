package com.dkhs.portfolio.engine;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;
import android.view.View;


import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SubmitSymbol;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

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

    public class MyCombinationListent extends ParseHttpListener<MoreDataBean<CombinationBean>> {

        @Override
        protected MoreDataBean<CombinationBean> parseDateTask(String jsonData) {

            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            MoreDataBean<CombinationBean> moreBean = (MoreDataBean) gson.fromJson(jsonData,
                    new TypeToken<MoreDataBean<CombinationBean>>() {
                    }.getType());
            return moreBean;

            // return combinationList;
        }

        @Override
        protected void afterParseData(MoreDataBean<CombinationBean> moreBean) {

        }

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

            StringBuilder sb = new StringBuilder();
            sb.append("[");

            for (int i = 0; i < symbols.size(); i++) {
                Gson gson = new Gson();
                String symbolsValue = gson.toJson(symbols.get(i));
                sb.append(symbolsValue);
                sb.append(",");
            }
            sb = sb.deleteCharAt(sb.length() - 1);

            sb.append("]");
            // System.out.println("symbols:" + sb.toString());
            params.addBodyParameter("symbols", sb.toString());
        }

        DKHSClient.requestByPost(DKHSUrl.Portfolio.create, params, listener);

    }

    /**
     * 删除我的组合
     * 
     * @param listener :服务器响应监听
     */
    public void deleteCombination(String Ids, IHttpListener listener) {

        // RequestParams params = new RequestParams();
        // // params.addBodyParameter("portfolios ", "1,2,3");
        // params.addBodyParameter("portfolios", Ids);

        DKHSClient.request(HttpMethod.DELETE, MessageFormat.format(DKHSUrl.Portfolio.delete, Ids), null, listener);
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

        DKHSClient.request(HttpMethod.PUT, DKHSUrl.Portfolio.update + id + "/", params, listener);

    }

    /**
     * 修改持仓组合
     * 
     * @param listener :服务器响应监听
     */
    public void adjustCombination(String id, List<SubmitSymbol> symbols, IHttpListener listener) {

        RequestParams params = new RequestParams();
        // params.addBodyParameter("portfolio", id + "");
        // 调整比例, 格式如：[{"symbol": 101000002,"percent":0.45},{"symbol": 101000004,"percent":0.35}]

        if (null != symbols && symbols.size() > 0) {

            StringBuilder sb = new StringBuilder();
            sb.append("[");

            for (int i = 0; i < symbols.size(); i++) {
                Gson gson = new Gson();
                String symbolsValue = gson.toJson(symbols.get(i));
                sb.append(symbolsValue);
                sb.append(",");
            }
            sb = sb.deleteCharAt(sb.length() - 1);

            sb.append("]");
            // System.out.println("symbols:" + sb.toString());
            params.addBodyParameter("symbols", sb.toString());
        }

        DKHSClient.requestByPost(MessageFormat.format(DKHSUrl.Portfolio.adjust, id), params, listener);

    }

    /**
     * 查询持仓明细
     * 
     * @param listener :服务器响应监听
     */
    public void queryCombinationDetail(String id, IHttpListener listener) {

        // queryCombinationDetailByDay(id, listener);
        String[] params = { id };
        DKHSClient.requestByGet(DKHSUrl.Portfolio.create, params, listener);
    }

    public void queryCombinationDetailByDay(String id, String date, IHttpListener listener) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair valuePair = new BasicNameValuePair("date", date);
        params.add(valuePair);
        DKHSClient.requestByGet(DKHSUrl.Portfolio.create, new String[] { id, }, params, listener);

    }

    public void changeCombinationIsPublic(String id, String ispublic, IHttpListener listener) {
        RequestParams params = new RequestParams();
        StringBuilder sb = new StringBuilder();
        // sb.append("[");
        sb.append(ispublic);
        // sb.append("]");
        params.addBodyParameter("is_public", sb.toString());
        DKHSClient.requestByPost(MessageFormat.format(DKHSUrl.Portfolio.ispublic, id), params, listener);
    }

    public void setCombinationTOp(String id, IHttpListener listener) {

        DKHSClient.requestByPost(MessageFormat.format(DKHSUrl.Portfolio.set_top, id), null, listener);
    }

}
