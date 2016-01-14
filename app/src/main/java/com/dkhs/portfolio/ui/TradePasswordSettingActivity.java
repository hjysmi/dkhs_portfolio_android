package com.dkhs.portfolio.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.MyAlertDialog;
import com.dkhs.portfolio.utils.ActivityCode;
import com.dkhs.portfolio.utils.PromptManager;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yang.gesturepassword.GesturePasswordManager;

import org.json.JSONObject;

/**
 * Created by zhangcm on 2015/9/17.16:15
 */
public class TradePasswordSettingActivity extends ModelAcitivity{

    public static String TAG = "TradePasswordSettingActivity";

    @ViewInject(R.id.gpv)
    private GridPasswordView gpv;

    @ViewInject(R.id.btn_set_trade_password)
    private Button btn_set_trade_password;

    @ViewInject(R.id.tv_trade_pwd_tip1)
    private TextView tv_trade_pwd_tip1;

    @ViewInject(R.id.tv_trade_pwd_tip2)
    private TextView tv_trade_pwd_tip2;



    private static final String LAYOUT_TYPE = "layout_type";
    private static final int TYPE_FIRST_SET_PWD = 1;
    private static final int TYPE_RESET_PWD = 2;
    private static final int TYPE_FORGET_PWD = 3;
    private int curLayoutType;
    public static Intent firstSetPwdIntent(Context context, String bankId, String bankCardNo, String realName, String idCardNo, String mobile, String captcha){
        Intent intent = new Intent(context, TradePasswordSettingActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_FIRST_SET_PWD);
        intent.putExtra("bank_card_id", bankId);
        intent.putExtra("bank_card_no", bankCardNo);
        intent.putExtra("real_name", realName);
        intent.putExtra("id_card_no", idCardNo);
        intent.putExtra("mobile", mobile);
        intent.putExtra("captcha", captcha);
        return intent;
    }
    public static Intent resetPwdIntent(Context context){
        Intent intent = new Intent(context, TradePasswordSettingActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_RESET_PWD);
        return intent;
    }
    public static Intent forgetPwdIntent(Context context, String bankId, String bankCardNo, String realName, String idCardNo, String mobile, String captcha) {
        Intent intent = new Intent(context, TradePasswordSettingActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_FORGET_PWD);
        intent.putExtra("bank_card_id", bankId);
        intent.putExtra("bank_card_no", bankCardNo);
        intent.putExtra("real_name", realName);
        intent.putExtra("id_card_no", idCardNo);
        intent.putExtra("mobile", mobile);
        intent.putExtra("captcha", captcha);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trade_password_setting);
        ViewUtils.inject(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        setTitle(R.string.setting_trade_password);
        setResult(ActivityCode.TRADE_PASSWORD_SETTING_RESULT.ordinal());
        initViews();
    }

    private String mobile;
    private String bankCardId;
    private String bankCardNo;
    private String realName;
    private String idCardNo;
    private String captcha;
    private void handleExtras(Bundle extras) {
        curLayoutType = extras.getInt(LAYOUT_TYPE, 0);
        if(curLayoutType == TYPE_FORGET_PWD || curLayoutType == TYPE_FIRST_SET_PWD){
            mobile = extras.getString("mobile");
            bankCardId = extras.getString("bank_card_id");
            bankCardNo = extras.getString("bank_card_no");
            realName = extras.getString("real_name");
            idCardNo = extras.getString("id_card_no");
            captcha = extras.getString("captcha");
        }
    }

    private String firstPwd;
    private String oldPwd;
    private boolean isOldPwdTrue;

    private void initViews(){
        if(curLayoutType == TYPE_FIRST_SET_PWD){
            tv_trade_pwd_tip1.setVisibility(View.INVISIBLE);
            tv_trade_pwd_tip2.setVisibility(View.INVISIBLE);
        }else if(curLayoutType == TYPE_RESET_PWD){
            setTitle(R.string.old_trade_password);
            tv_trade_pwd_tip1.setVisibility(View.INVISIBLE);
            tv_trade_pwd_tip2.setVisibility(View.VISIBLE);
        }else if(curLayoutType == TYPE_FORGET_PWD){
            tv_trade_pwd_tip1.setVisibility(View.INVISIBLE);
            tv_trade_pwd_tip2.setVisibility(View.INVISIBLE);
        }
        gpv.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged(String psw) {
                if (psw.length() < 6)
                    btn_set_trade_password.setEnabled(false);
            }

            @Override
            public void onMaxLength(String psw) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (TextUtils.isEmpty(firstPwd)) {
                    if (curLayoutType == TYPE_FIRST_SET_PWD || curLayoutType == TYPE_FORGET_PWD || (curLayoutType == TYPE_RESET_PWD && isOldPwdTrue)) {
                        firstPwd = psw;
                        gpv.clearPassword();
                        tv_trade_pwd_tip1.setVisibility(View.VISIBLE);
                        tv_trade_pwd_tip1.setText(R.string.input_trade_password_again);
                        tv_trade_pwd_tip2.setVisibility(View.INVISIBLE);
                    } else if (curLayoutType == TYPE_RESET_PWD && !isOldPwdTrue) {
                        oldPwd = psw;
                        btn_set_trade_password.setEnabled(true);
                    }
                } else {
                    if (firstPwd.equals(gpv.getPassWord())) {
                        // TODO 密码一致设置密码
                        btn_set_trade_password.setEnabled(true);
                        imm.hideSoftInputFromWindow(gpv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } else {
                        //TODO 密码不一致根据当前layoutType提示
                        firstPwd = null;
                        gpv.clearPassword();
                        tv_trade_pwd_tip1.setText(R.string.trade_password_unsame);
                        tv_trade_pwd_tip1.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        tv_trade_pwd_tip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ForgetTradePasswordActivity.class));
                finish();
            }
        });
        btn_set_trade_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下一步
                TradeEngineImpl tradeEngine = new TradeEngineImpl();
                ParseHttpListener<Boolean> listener = new ParseHttpListener<Boolean>() {
                    public void onFailure(ErrorBundle errorBundle) {
                        if(errorBundle.getErrorKey().equals("password_lock_invalid")){
                            gpv.clearPassword();
                            showPwdLockedDialog(errorBundle.getErrorMessage());
                        }else{
                            super.onFailure(errorBundle);
                        }
                    }
                    @Override
                    protected Boolean parseDateTask(String jsonData) {
                        try {
                            JSONObject json = new JSONObject(jsonData);
                            if (json.has("status")) {
                                return json.getBoolean("status");
                            }

                        } catch (Exception e) {
                        }
                        return null;
                    }

                    @Override
                    protected void afterParseData(Boolean object) {
                        if (null != object) {
                            if (curLayoutType == TYPE_FIRST_SET_PWD || curLayoutType == TYPE_FORGET_PWD) {
                                if (object) {
                                    PromptManager.showToast(R.string.set_trade_password_suc);
                                    setResult(ActivityCode.TRADE_PASSWORD_SETTING_RESULT.ordinal());
                                    manualFinish();
                                } else {
                                    PromptManager.showToast(R.string.set_trade_password_fail);
                                }
                            } else if (curLayoutType == TYPE_RESET_PWD) {
                                if (object) {
                                    if (TextUtils.isEmpty(firstPwd)) {
                                        //设置新交易密码成功
                                        setResult(ActivityCode.TRADE_PASSWORD_SETTING_RESULT.ordinal());
                                        PromptManager.showToast(R.string.origin_trade_password_correct);
                                        isOldPwdTrue = object;
                                        setTitle(R.string.setting_trade_password);
                                        gpv.clearPassword();
                                        btn_set_trade_password.setEnabled(false);
                                        tv_trade_pwd_tip1.setVisibility(View.VISIBLE);
                                        tv_trade_pwd_tip1.setText(R.string.pls_input_trade_pwd);
                                        tv_trade_pwd_tip2.setVisibility(View.INVISIBLE);
                                    } else {
                                        PromptManager.showToast(R.string.set_new_trade_password_suc);

                                        finish();
                                    }
                                } else {
                                    if (TextUtils.isEmpty(firstPwd)) {
                                        gpv.clearPassword();
                                        btn_set_trade_password.setEnabled(false);
                                        PromptManager.showToast(R.string.origin_trade_password_uncorrect);
                                    } else {
                                        PromptManager.showToast(R.string.set_new_trade_password_fail);
                                    }
                                }
                            }
                        }
                    }
                };
                if (curLayoutType == TYPE_FIRST_SET_PWD) {
                    tradeEngine.resetTradePassword(bankCardId, bankCardNo, realName, idCardNo, mobile, captcha, gpv.getPassWord(), listener.setLoadingDialog(mContext));
                } else if (curLayoutType == TYPE_RESET_PWD) {
                    if (TextUtils.isEmpty(firstPwd)) {
                        tradeEngine.checkTradePassword(oldPwd, listener.setLoadingDialog(mContext));
                    } else {
                        tradeEngine.changeTradePassword(oldPwd, firstPwd, listener.setLoadingDialog(mContext));
                    }
                }else if(curLayoutType == TYPE_FORGET_PWD){
                    tradeEngine.resetTradePassword(bankCardId, bankCardNo, realName, idCardNo, mobile, captcha, gpv.getPassWord(), listener.setLoadingDialog(mContext));
                }
            }
        });

    }

    private void showPwdLockedDialog(String msg) {
        new MyAlertDialog(this).builder()
                .setCancelable(false)
                .setMsg(msg)
                .setPositiveButton(getResources().getString(R.string.forget_password), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(ForgetTradePasswordActivity.newIntent(mContext, false));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        manualFinish();
                    }
                }).show();
    }

}
