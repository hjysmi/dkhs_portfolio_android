package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zjz on 2015/10/9.
 */
public class PaymentBean {


    private String payType;
    private String alipay_order_info;
    private WeiXinOrderInfo weixinpay_order_info;

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

    public WeiXinOrderInfo getWeixinpay_order_info() {
        return weixinpay_order_info;
    }

    public void setWeixinpay_order_info(WeiXinOrderInfo weixinpay_order_info) {
        this.weixinpay_order_info = weixinpay_order_info;
    }

    public  static class WeiXinOrderInfo{
        @SerializedName("package")
        public  String packageValue;
        public String timestamp;
        public String sign;
        public  String partnerid;
        public String appid;
        public  String prepayid;
        public  String noncestr;
    }

}
