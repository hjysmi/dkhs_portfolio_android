package com.dkhs.portfolio.bean;

/**
 * Created by zjz on 2015/10/9.
 */
public class PaymentBean {


    private String payType;
    private String alipay_order_info;

    public String getAlipay_order_info() {
        return alipay_order_info;
    }

    public void setAlipay_order_info(String alipay_order_info) {
        this.alipay_order_info = alipay_order_info;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }


}
