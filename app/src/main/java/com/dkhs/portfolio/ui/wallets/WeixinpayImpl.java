package com.dkhs.portfolio.ui.wallets;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dkhs.portfolio.bean.PaymentBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.Constants;

/**
 * Created by zjz on 2015/10/9.
 */
public class WeixinpayImpl implements IThreePay {
    PayReq req = new PayReq();
    private Activity mCotnext;
    private IThreePayCallback iThreePayCallback;
    private StringBuilder sb = new StringBuilder();
    IWXAPI msgApi;

    public WeixinpayImpl(Activity context,IThreePayCallback callback){
        mCotnext = context;
        iThreePayCallback = callback;
        msgApi = WXAPIFactory.createWXAPI(mCotnext, null);
    }

    @Override
    public void recharge(final PaymentBean paymentBean) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //TODO调用微信sdk
                PaymentBean.WeiXinOrderInfo info = paymentBean.getWeixinpay_order_info();
                Log.d("wys","info"+info.sign);
                if(info != null){
                    sendPayReq(info);
                }
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });




    private void sendPayReq(PaymentBean.WeiXinOrderInfo info) {
        req.appId = info.appid;
        req.partnerId = info.partnerid;
        req.prepayId = info.prepayid;
        req.packageValue = info.packageValue;
        req.nonceStr = info.noncestr;
        req.timeStamp = info.timestamp;
        req.sign = info.sign;
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }
}
