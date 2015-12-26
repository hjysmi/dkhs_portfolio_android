package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
    public static Intent firstSetPwdIntent(Context context){
        Intent intent = new Intent(context, TradePasswordSettingActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_FIRST_SET_PWD);
        return intent;
    }
    public static Intent resetPwdIntent(Context context){
        Intent intent = new Intent(context, TradePasswordSettingActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_RESET_PWD);
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
        setResult(0);
        initViews();
    }

    private void handleExtras(Bundle extras) {
        curLayoutType = extras.getInt(LAYOUT_TYPE, 0);
    }

    private String firstPwd;
    private String oldPwd;
    private boolean isOldPwdTrue;

    private void initViews(){
        if(curLayoutType == TYPE_FIRST_SET_PWD){
            tv_trade_pwd_tip1.setVisibility(View.INVISIBLE);
            tv_trade_pwd_tip2.setVisibility(View.INVISIBLE);
        }else if(curLayoutType == TYPE_RESET_PWD){
            tv_trade_pwd_tip1.setVisibility(View.INVISIBLE);
            tv_trade_pwd_tip2.setVisibility(View.VISIBLE);
            setTitle(R.string.old_trade_password);
        }else if(curLayoutType == TYPE_FORGET_PWD){
            tv_trade_pwd_tip1.setVisibility(View.INVISIBLE);
            tv_trade_pwd_tip2.setVisibility(View.INVISIBLE);
        }
        gpv.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged(String psw) {
                if(psw.length() < 6)
                    btn_set_trade_password.setEnabled(false);
            }

            @Override
            public void onMaxLength(String psw) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(TextUtils.isEmpty(firstPwd)){
                    if(curLayoutType == TYPE_FIRST_SET_PWD || (curLayoutType == TYPE_RESET_PWD&&isOldPwdTrue)){
                        firstPwd =psw;
                        gpv.clearPassword();
                        tv_trade_pwd_tip1.setVisibility(View.VISIBLE);
                        tv_trade_pwd_tip1.setText(R.string.input_trade_password_again);
                        tv_trade_pwd_tip2.setVisibility(View.INVISIBLE);
                    }else if(curLayoutType == TYPE_RESET_PWD && !isOldPwdTrue){
                        oldPwd = psw;
                        btn_set_trade_password.setEnabled(true);
                    }
                }else{
                    if(firstPwd.equals(gpv.getPassWord())){
                        // TODO 密码一致设置密码
                        btn_set_trade_password.setEnabled(true);
                        imm.hideSoftInputFromWindow(gpv.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    }else{
                        //TODO 密码不一致根据当前layoutType提示
                        if(curLayoutType == TYPE_FIRST_SET_PWD ||curLayoutType == TYPE_RESET_PWD){
                            firstPwd = null;
                            gpv.clearPassword();
                            tv_trade_pwd_tip1.setText(R.string.trade_password_unsame);
                            tv_trade_pwd_tip1.setVisibility(View.VISIBLE);
                        }else if(curLayoutType == TYPE_FORGET_PWD){

                        }
                    }
                }
            }
        });
        btn_set_trade_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下一步
                TradeEngineImpl tradeEngine = new TradeEngineImpl();
                ParseHttpListener<Boolean> listener = new ParseHttpListener<Boolean>() {
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
                            if (curLayoutType == TYPE_FIRST_SET_PWD) {
                                if (object) {
                                    PromptManager.showToast("交易密码设置成功");
                                    setResult(0);
                                    manualFinish();
                                } else {
                                    PromptManager.showToast("设置密码失败");
                                }
                            } else if (curLayoutType == TYPE_RESET_PWD) {
                                if (object) {
                                    if (TextUtils.isEmpty(firstPwd)) {
                                        //设置新交易密码成功
                                        PromptManager.showToast("原密码正确,请设置新的交易密码");
                                        isOldPwdTrue = object;
                                        setTitle(R.string.setting_trade_password);
                                        gpv.clearPassword();
                                        btn_set_trade_password.setEnabled(false);
                                        tv_trade_pwd_tip1.setVisibility(View.VISIBLE);
                                        tv_trade_pwd_tip1.setText(R.string.pls_input_trade_pwd);
                                        tv_trade_pwd_tip2.setVisibility(View.INVISIBLE);
                                    } else {
                                        PromptManager.showToast("设置新交易密码成功");
                                        finish();
                                    }
                                } else {
                                    if (TextUtils.isEmpty(firstPwd)) {
                                        gpv.clearPassword();
                                        btn_set_trade_password.setEnabled(false);
                                        PromptManager.showToast("原密码错误");
                                    } else {
                                        PromptManager.showToast("设置新交易密码失败");
                                    }
                                }
                            }

                        }
                    }
                };
                if (curLayoutType == TYPE_FIRST_SET_PWD) {
                    tradeEngine.setTradePassword(gpv.getPassWord(), listener.setLoadingDialog(mContext));
                } else if (curLayoutType == TYPE_RESET_PWD) {
                    if (TextUtils.isEmpty(firstPwd)) {
                        tradeEngine.checkTradePassword(oldPwd, listener.setLoadingDialog(mContext));
                    } else {
                        tradeEngine.changeTradePassword(oldPwd, firstPwd, listener.setLoadingDialog(mContext));
                    }
                }
            }
        });

    }

}
