package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;

/**
 * Created by zhangcm on 2015/9/28.18:23
 */
public class TradeEngineImpl extends LoadMoreDataEngine{

    public TradeEngineImpl(){}

    /**
     * 默认显示一页20条数据
     */
    private  int pageSize = 20;
    private String curUrl;
    private Type curType;

    public TradeEngineImpl(ILoadDataBackListener loadListener){
        super(loadListener);
    }


    @Override
    public HttpHandler loadMore() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", (getCurrentpage() + 1) + "");
        params.addQueryStringParameter("page_size", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, curUrl, params, this);
    }

    @Override
    public HttpHandler refreshDatabySize(int dataSize) {
        return loadData();
    }

    @Override
    protected MoreDataBean parseDateTask(String jsonData) {
        MoreDataBean moreBean = null;
        if (!TextUtils.isEmpty(jsonData)) {
                jsonData = StringDecodeUtil.decodeUnicode(jsonData);
            try {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                moreBean = (MoreDataBean) gson.fromJson(jsonData, curType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return moreBean;
    }

    @Override
    public HttpHandler loadData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("pageSize", pageSize + "");
        return DKHSClient.request(HttpRequest.HttpMethod.GET, curUrl, params, this);
    }

    public void setCurUrl(String url){
        curUrl = url;
    }

    public void setCurType(Type type){
        type = curType;
    }

    /**
     * post身份认证
     */
    public void verifyIdentityAuth(String bank, String bank_card_no, String real_name, String id_card_no, String mobile, String captcha, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("bank", bank);
        params.addBodyParameter("bank_card_no", bank_card_no);
        if(!TextUtils.isEmpty(real_name))
            params.addBodyParameter("real_name", real_name);
        if(!TextUtils.isEmpty(id_card_no))
            params.addBodyParameter("id_card_no", id_card_no);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("captcha", captcha);
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.Funds.verify_identy, params, listener);
    }
    /**
     * post身份认证
     */
    public void getIdentityVerification(IHttpListener listener){
        DKHSClient.requestByGet(listener, DKHSUrl.Funds.get_verifications);
    }
    /**
     * post重置交易密码
     */
    public void resetTradePassword(String bank_card_id, String bank_card_no, String real_name, String id_card_no, String mobile, String captcha,String password, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("bank_card_id", bank_card_id);
        params.addBodyParameter("bank_card_no", bank_card_no);
        if(!TextUtils.isEmpty(real_name))
            params.addBodyParameter("real_name", real_name);
        if(!TextUtils.isEmpty(id_card_no))
            params.addBodyParameter("id_card_no", id_card_no);
        params.addBodyParameter("mobile", mobile);
        if(!TextUtils.isEmpty(captcha))
            params.addBodyParameter("captcha", captcha);
        if(!TextUtils.isEmpty(password))
            params.addBodyParameter("password", password);
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.Funds.reset_trade_password, params, listener);
    }

    public void bindBankCard(String bank_card_id, String bank_card_no, String real_name, String id_card_no, String mobile, String captcha,String password, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("bank_card_id", bank_card_id);
        params.addBodyParameter("bank_card_no", bank_card_no);
        if(!TextUtils.isEmpty(real_name))
            params.addBodyParameter("real_name", real_name);
        if(!TextUtils.isEmpty(id_card_no))
            params.addBodyParameter("id_card_no", id_card_no);
        params.addBodyParameter("mobile", mobile);
        if(!TextUtils.isEmpty(captcha))
            params.addBodyParameter("captcha", captcha);
        if(!TextUtils.isEmpty(password))
            params.addBodyParameter("password", password);
        DKHSClient.request(HttpRequest.HttpMethod.POST, DKHSUrl.Funds.bind_bank_card, params,listener);
    }

    /**
     * 获取银行列表
     */
    public void getBanks(IHttpListener listener) {
        DKHSClient.requestByGet(listener, DKHSUrl.Funds.get_banks);
    }

    public void isTradePasswordSet(IHttpListener listener){
        DKHSClient.requestByGet(listener,DKHSUrl.Funds.is_trade_password_set);
    }

    public void setTradePassword(String password,IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("password",password);
        DKHSClient.requestByPost(DKHSUrl.Funds.set_trade_password, params, listener);
    }
    public void getMyBankCards(IHttpListener listener){
        DKHSClient.requestByGet(listener, DKHSUrl.Funds.get_my_bank_cards);
    }
    public void getMyAssests(IHttpListener listener){
        DKHSClient.requestByGet(listener,DKHSUrl.Funds.get_my_assests);
    }
    public void checkTradePassword(String password, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("password",password);
        DKHSClient.requestByPost(DKHSUrl.Funds.check_trade_password, params, listener);
    }
    public void changeTradePassword(String old_password, String new_password, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("old_password",old_password);
        params.addBodyParameter("new_password",new_password);
        DKHSClient.requestByPost(DKHSUrl.Funds.change_trade_password,params,listener);
    }
    public void buyFund(long fund_id, String bank_card_id,String amount,String password, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("fund_id", String.valueOf(fund_id));
        params.addBodyParameter("bank_card_id",bank_card_id);
        params.addBodyParameter("amount",amount);
        params.addBodyParameter("password",password);
        DKHSClient.requestByPost(DKHSUrl.Funds.buy_fund,params,listener);
    }
    public void sellFund(long fund_id, String bank_card_id,String shares,String password, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("fund_id", String.valueOf(fund_id));
        params.addBodyParameter("bank_card_id",bank_card_id);
        params.addBodyParameter("shares",shares);
        params.addBodyParameter("password",password);
        DKHSClient.requestByPost(DKHSUrl.Funds.sell_fund,params,listener);
    }
    public void checkBank(String bank_card_no, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("bank_card_no",bank_card_no);
        DKHSClient.requestByPost(DKHSUrl.Funds.check_bank_card,params,listener);
    }
    public void checkIdentity(String bank_card_no, IHttpListener listener){
        RequestParams params = new RequestParams();
        params.addBodyParameter("bank_card_no",bank_card_no);
        DKHSClient.requestByPost(DKHSUrl.Funds.checkIdentity,params,listener);
    }

}
