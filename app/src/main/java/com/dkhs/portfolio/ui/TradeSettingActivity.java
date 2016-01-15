package com.dkhs.portfolio.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageButton;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.widget.MyAlertDialog;
import com.dkhs.portfolio.utils.ActivityCode;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yang.gesturepassword.GesturePassword;
import com.yang.gesturepassword.GesturePasswordManager;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangcm on 2015/9/24.15:57
 */
public class TradeSettingActivity extends AssestsBaseActivity {

    @ViewInject(R.id.rl_reset_gesture_password)
    View rl_reset_gesture_password;
    @ViewInject(R.id.rl_gesture_password_setting)
    View rl_gesture_password_setting;
    @ViewInject(R.id.tv_gesture_password)
    TextView tv_gesture_password;
    @ViewInject(R.id.tv_trade_password)
    TextView tv_trade_password;
    @ViewInject(R.id.ib_gesture_setting)
    SwitchCompat ib_gesture_setting;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trade_setting);
        ViewUtils.inject(this);
        setTitle(R.string.trade_password);
    }

    private GesturePassword gesPassword;
    //是否设置过交易密码
    private boolean hasTradePassword;
    //是否设置过手势密码
    private boolean hasGesturePassword;

    @Override
    protected void onResume() {
        super.onResume();
        gesPassword = GesturePasswordManager.getInstance().getGesturePassword(mContext,GlobalParams.MOBILE);
        if(gesPassword == null){
            rl_reset_gesture_password.setVisibility(View.GONE);
            rl_gesture_password_setting.setVisibility(View.GONE);
        }else{
            if(gesPassword != null){
                hasGesturePassword = !TextUtils.isEmpty(gesPassword.password);
                tv_gesture_password.setText(hasGesturePassword?R.string.reset_gesture_password:R.string.set_gesture_password);
            }
            ib_gesture_setting.setChecked(gesPassword.isOpen);
        }
        new TradeEngineImpl().isTradePasswordSet(new ParseHttpListener<Boolean>() {
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
                hasTradePassword = object;
                if (null != object) {
                    tv_trade_password.setText(object?R.string.reset_trade_password:R.string.set_trade_password);
                }
            }
        });
    }

    @OnClick({R.id.rl_reset_trade_password,R.id.rl_reset_gesture_password,R.id.ib_gesture_setting})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.rl_reset_trade_password:
                // 判断是否绑定了银行卡,没有绑定银行卡提示是否绑定
                ParseHttpListener listener = new ParseHttpListener<List<MyBankCard>>() {
                    @Override
                    protected List<MyBankCard> parseDateTask(String jsonData) {
                        List<MyBankCard> myCards = null;
                        if (!TextUtils.isEmpty(jsonData)) {
                            try {
                                jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                                myCards = gson.fromJson(jsonData, new TypeToken<List<MyBankCard>>(){}.getType());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return myCards;
                    }

                    @Override
                    protected void afterParseData(List<MyBankCard> cards) {
                        if(cards != null && cards.size() > 0){
                            if(hasTradePassword){
                                startActivity(new Intent(mContext, ResetTradePasswordActivity.class));
                            }else{
                                startActivityForResult(ForgetTradePasswordActivity.newIntent(mContext,true), ActivityCode.FORGET_TRADE_PASSWORD_REQUEST.ordinal());
                            }
                        }else{
                            showBindcardDialog();
                        }
                    }
                };
                new TradeEngineImpl().getMyBankCards(listener.setLoadingDialog(this));
                break;
            case R.id.rl_reset_gesture_password:
                if(gesPassword != null){
                    if(hasGesturePassword){
                        startActivity(GesturePasswordActivity.setPasswordIntent(mContext, true));
                    }else{
                        startActivity(GesturePasswordActivity.firstSetPasswordIntent(mContext, true));
                    }
                }
                break;
            case R.id.ib_gesture_setting:
                if(gesPassword != null){
                    if(hasGesturePassword){
                        if(gesPassword.isOpen){
                            startActivity(GesturePasswordActivity.closeSettingIntent(mContext, true));
                        }else{
                            startActivity(GesturePasswordActivity.openSettingIntent(mContext, true));
                        }
                    }else{
                        startActivity(GesturePasswordActivity.firstSetPasswordIntent(mContext, true));
                    }
                }
                ib_gesture_setting.setChecked(gesPassword.isOpen);
                break;
        }
    }

    private void showBindcardDialog(){
        new MyAlertDialog(this).builder()
                .setMsg(getResources().getString(R.string.trade_password_msg))
                .setPositiveButton(getResources().getString(R.string.fine), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, BankCardNoActivity.class));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_trade_setting;
    }
}
