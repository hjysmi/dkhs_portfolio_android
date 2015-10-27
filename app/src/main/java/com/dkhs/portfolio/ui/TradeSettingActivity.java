package com.dkhs.portfolio.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageButton;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.widget.MyAlertDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yang.gesturepassword.GesturePassword;
import com.yang.gesturepassword.GesturePasswordManager;

import java.util.List;

/**
 * Created by zhangcm on 2015/9/24.15:57
 */
public class TradeSettingActivity extends ModelAcitivity {

    @ViewInject(R.id.rl_reset_gesture_password)
    View rl_reset_gesture_password;
    @ViewInject(R.id.rl_gesture_password_setting)
    View rl_gesture_password_setting;
    @ViewInject(R.id.ib_gesture_setting)
    ImageButton ib_gesture_setting;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trade_setting);
        ViewUtils.inject(this);
        setTitle(R.string.trade_password);
    }

    private GesturePassword gesPassword;

    @Override
    protected void onResume() {
        super.onResume();
        gesPassword = GesturePasswordManager.getInstance().getGesturePassword(mContext,GlobalParams.MOBILE);
        if(gesPassword == null){
            rl_reset_gesture_password.setVisibility(View.GONE);
            rl_gesture_password_setting.setVisibility(View.GONE);
        }else{
            ib_gesture_setting.setBackgroundResource(gesPassword.isOpen?R.drawable.ios7_switch_on:R.drawable.ios7_switch_off);
        }
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
                            startActivity(new Intent(mContext, ResetTradePasswordActivity.class));
                        }else{
                            showBindcardDialog();
                        }
                    }
                };
                new TradeEngineImpl().getMyBankCards(listener.setLoadingDialog(this));
                break;
            case R.id.rl_reset_gesture_password:
                if(gesPassword != null){
                    if(TextUtils.isEmpty(gesPassword.password)){
                        startActivity(GesturePasswordActivity.firstSetPasswordIntent(mContext, true));
                    }else{
                        startActivity(GesturePasswordActivity.setPasswordIntent(mContext, true));
                    }
                }
                break;
            case R.id.ib_gesture_setting:
                if(gesPassword != null){
                    if(TextUtils.isEmpty(gesPassword.password)){
                        startActivity(GesturePasswordActivity.firstSetPasswordIntent(mContext, true));
                    }else{
                        if(gesPassword.isOpen){
                            startActivity(GesturePasswordActivity.closeSettingIntent(mContext, true));
                        }else{
                            startActivity(GesturePasswordActivity.openSettingIntent(mContext, true));
                        }
                    }
                }
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

}
