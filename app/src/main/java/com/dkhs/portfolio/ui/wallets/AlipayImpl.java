package com.dkhs.portfolio.ui.wallets;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.dkhs.portfolio.bean.PaymentBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by zjz on 2015/10/9.
 */
public class AlipayImpl implements IThreePay {

    private static final int SDK_PAY_FLAG = 11;
    private static final int SDK_CHECK_FLAG = 22;

    private Activity mCotnext;
    private IThreePayCallback iThreePayCallback;

    public AlipayImpl(Activity context, IThreePayCallback payCallback) {
        this.mCotnext = context;
        this.iThreePayCallback = payCallback;
    }

    @Override
    public void recharge(final PaymentBean paymentBean) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mCotnext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(paymentBean.getAlipay_order_info());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private WeakHandler mHandler = new WeakHandler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    AliPayResult payResult = new AliPayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        LogUtils.d("支付成功");
                        if (null != iThreePayCallback) {
                            iThreePayCallback.rechargeSuccess();
                        }
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            LogUtils.d("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

                            LogUtils.d("支付失败");
                            if (null != iThreePayCallback) {
                                iThreePayCallback.rechargeFail();
                            }

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };
}
