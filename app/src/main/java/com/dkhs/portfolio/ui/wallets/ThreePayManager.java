package com.dkhs.portfolio.ui.wallets;

import android.app.Activity;
import android.text.TextUtils;

import com.dkhs.portfolio.bean.PaymentBean;
import com.dkhs.portfolio.engine.WalletsEngine;

/**
 * Created by zjz on 2015/10/9.
 * 第三方支付的管理
 */
public class ThreePayManager {

    private Activity mCotnext;

    private PaymentBean mPaymentBean;

    private IThreePay mThreePay;
    private IThreePayCallback payCallback;

    public ThreePayManager(Activity context, IThreePayCallback payCallback) {
        this.mCotnext = context;
        this.payCallback = payCallback;
    }


    public void pay(PaymentBean paymentBean) {
        mPaymentBean = paymentBean;
        if (null != mPaymentBean && !TextUtils.isEmpty(mPaymentBean.getPayType())) {
            if (mPaymentBean.getPayType().equals(WalletsEngine.Alipay)) {
                mThreePay = new AlipayImpl(mCotnext, this.payCallback);
            } else if (mPaymentBean.getPayType().equals(WalletsEngine.WeiXin)) {
                mThreePay = new WeixinpayImpl();
            } else {
                mThreePay = new CardPayImpl();
            }
            mThreePay.recharge(mPaymentBean);
        } else {
            throw new IllegalArgumentException("请输入正确的支付类型");
        }


    }
}
