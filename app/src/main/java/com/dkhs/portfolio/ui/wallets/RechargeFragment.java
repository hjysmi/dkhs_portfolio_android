package com.dkhs.portfolio.ui.wallets;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.PayResEvent;
import com.dkhs.portfolio.ui.fragment.BaseFragment;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjz on 2015/10/9.
 */
public class RechargeFragment extends BaseFragment implements View.OnClickListener {

    private static final int ENABLE_STATUS = 0;
    private static final int DISABLE_STATUS = 1;
    public static final String CHARGE_AMOUNT = "charge_amount";

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

    private BigDecimal rechargeAmount;

    public static RechargeFragment newInstance(String amount){
        RechargeFragment fragment = new RechargeFragment();
        Bundle args = new Bundle();
        args.putString(CHARGE_AMOUNT, amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPayManager = new ThreePayManager(getActivity(), rechargeCallback);
    }

    IThreePayCallback rechargeCallback = new IThreePayCallback() {
        @Override
        public void rechargeSuccess() {
            PromptManager.showSuccessToast(R.string.recharge_success);
            getActivity().finish();
            BusProvider.getInstance().post(new PayResEvent(0));
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
        Bundle bundle = getArguments();
        String chargeStr  = bundle.getString(CHARGE_AMOUNT);
        if(!TextUtils.isEmpty(chargeStr)){
            rechargeAmount = new BigDecimal(chargeStr);
        }else{
            rechargeAmount = new BigDecimal("0");
        }
        if(!rechargeAmount.equals(new BigDecimal("0"))){//默认充值金额＝悬赏金额－帐户余额， 且必须大于1
            if(rechargeAmount.compareTo(new BigDecimal("1")) == 1){
                etPayNum.setText(String.valueOf(rechargeAmount));
            }else{
                etPayNum.setText("1");
            }
            changeBtnStatus(ENABLE_STATUS);
        }else {
            changeBtnStatus(DISABLE_STATUS);
        }
        etPayNum.addTextChangedListener(percentTextWatch);
        etPayNum.setFilters(new InputFilter[]{lengthfilter});
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
            //金额为空时　充值按钮置灰不可点击
            if(TextUtils.isEmpty(textString)){
                changeBtnStatus(DISABLE_STATUS);
            }else{
                changeBtnStatus(ENABLE_STATUS);
            }
            if (textString.equals(strBefore)) {
                return;
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

    private static final int DECIMAL_DIGITS = 2;
    /**
     * 限制小数位数
     */
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }else if(splitArray.length == 1){
                String intValue = splitArray[0];
                int diff = intValue.length() + 1 - 9;
                if(diff > 0){
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

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
            if (amout < 1 ) {
                PromptManager.showToast("充值金额不能小于1元！");
                return;
            }

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
            public void beforeRequest() {
                super.beforeRequest();
            }

            @Override
            protected PaymentBean parseDateTask(String jsonData) {
                PaymentBean bean = DataParse.parseObjectJson(PaymentBean.class, jsonData);
                return bean;
            }

            @Override
            protected void afterParseData(PaymentBean object) {
                if (null != object) {
                    object.setPayType(payType);
                    mPayManager.pay(object);
                }
            }
        }.setLoadingDialog(getActivity(),false));
    }

    private void changeBtnStatus(int status){
        if(status == DISABLE_STATUS){
            btnRecharge.setEnabled(false);
            btnRecharge.setClickable(false);
        }else{
            btnRecharge.setEnabled(true);
            btnRecharge.setClickable(true);
        }
    }
}
