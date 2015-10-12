package com.dkhs.portfolio.ui.wallets;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.dkhs.portfolio.bean.PaymentBean;
import com.dkhs.portfolio.engine.WalletsEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.BaseFragment;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjz on 2015/10/9.
 */
public class RechargeFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.et_play_num)
    private EditText etPayNum;
    @ViewInject(R.id.btn_recharge)
    private Button btnRecharge;
    @ViewInject(R.id.iv_select_alipay)
    private View ivSelectAli;
    @ViewInject(R.id.iv_select_weixin)
    private View ivSelectWeixin;
    @ViewInject(R.id.iv_select_card)
    private View ivSelectCard;

    @ViewInject(R.id.rl_wechatpay)
    private View viewWexin;
    @ViewInject(R.id.rl_alipay)
    private View viewAlipay;
    @ViewInject(R.id.rl_cardpay)
    private View viewCard;

    private String payType;

    private ThreePayManager mPayManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPayManager = new ThreePayManager(getActivity(), rechargeCallback);
    }

    IThreePayCallback rechargeCallback = new IThreePayCallback() {
        @Override
        public void rechargeSuccess() {
            getActivity().finish();
        }

        @Override
        public void rechargeFail() {
            PromptManager.showToast("充值失败!");
        }
    };

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_recharge;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etPayNum.addTextChangedListener(percentTextWatch);
//        btnRecharge.setOnClickListener(this);
    }

    String strBefore;
    TextWatcher percentTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            strBefore = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            String textString = s.toString();
            if (textString.equals(strBefore)) {
                return;
            }

            int editStart = etPayNum.getSelectionStart();
            if (!isAllowPercentInputText(textString)) {
                etPayNum.setText(strBefore);
                etPayNum.setSelection(strBefore.length());

            } else {
                strBefore = s.toString();
                etPayNum.setText(s);
                etPayNum.setSelection(editStart);
            }

        }
    };


    private boolean isAllowPercentInputText(String str) {

        if (TextUtils.isEmpty(str)) {
            return true;
        }
        String compText = "^(\\d{0,9})|(\\d{0,9}?(\\.\\d{0,2}))$";
        Pattern p = Pattern.compile(compText);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @OnClick({R.id.btn_recharge, R.id.rl_alipay, R.id.rl_wechatpay, R.id.rl_cardpay})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_recharge) {


            String amountText = etPayNum.getText().toString();
            if (TextUtils.isEmpty(amountText)) {
                PromptManager.showToast("请输入有效金额");
                return;
            }
            float amout = Float.valueOf(amountText);
//        if (amout < 1) {
//            PromptManager.showToast("充值金额不能小于1元！");
//            return;
//        }

            payType = getPayType();
            payment(amout);
        } else if (id == R.id.rl_alipay) {
            setAllEnable();
            viewAlipay.setClickable(false);
            ivSelectAli.setVisibility(View.VISIBLE);
        } else if (id == R.id.rl_wechatpay) {
            setAllEnable();
            viewWexin.setClickable(false);
            ivSelectWeixin.setVisibility(View.VISIBLE);
        } else if (id == R.id.rl_cardpay) {
            setAllEnable();
            viewCard.setClickable(false);
            ivSelectCard.setVisibility(View.VISIBLE);
        }


    }

    private void setAllEnable() {
        viewAlipay.setClickable(true);
        ivSelectAli.setVisibility(View.GONE);
        viewCard.setClickable(true);
        ivSelectCard.setVisibility(View.GONE);
        viewWexin.setClickable(true);
        ivSelectWeixin.setVisibility(View.GONE);
    }

    private String getPayType() {
        if (ivSelectAli.isShown())
            return WalletsEngine.Alipay;
        if (ivSelectWeixin.isShown())
            return WalletsEngine.WeiXin;
        if (ivSelectCard.isShown())
            return WalletsEngine.YiBao;
        return null;
    }


    private void payment(float amout) {
        WalletsEngine.payment(amout, payType, new ParseHttpListener<PaymentBean>() {
            @Override
            protected PaymentBean parseDateTask(String jsonData) {
                PaymentBean bean = DataParse.parseObjectJson(PaymentBean.class, jsonData);
                return bean;
            }

            @Override
            protected void afterParseData(PaymentBean object) {
                if (null != object) {
                    object.setPayType(payType);
                    mPayManager.alipay(object);
                }
            }
        });
    }


}
