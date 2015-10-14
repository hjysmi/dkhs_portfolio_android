package com.dkhs.portfolio.ui.wallets;

import com.dkhs.portfolio.bean.PaymentBean;

/**
 * Created by zjz on 2015/10/9.
 * 第三方支付的抽象接口
 */
public interface IThreePay {

    public void recharge(PaymentBean paymentBean);

}
