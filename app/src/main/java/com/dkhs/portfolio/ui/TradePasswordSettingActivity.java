package com.dkhs.portfolio.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by zhangcm on 2015/9/17.16:15
 */
public class TradePasswordSettingActivity extends ModelAcitivity{

    public static String TAG = "TradePasswordSettingActivity";

    @ViewInject(R.id.gpv)
    private GridPasswordView gpv;

    @ViewInject(R.id.btn_set_trade_password)
    private Button btnSetTradePassword;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trade_password_setting);
        ViewUtils.inject(this);
        setTitle(R.string.setting_trade_password);
        initViews();
    }

    private void initViews(){
        gpv.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged(String psw) {
                if(psw.length() < 6)
                    btnSetTradePassword.setEnabled(false);
            }

            @Override
            public void onMaxLength(String psw) {
                Log.i(TAG, "password ...." + psw);
                btnSetTradePassword.setEnabled(true);
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(gpv.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        btnSetTradePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下一步
            }
        });

    }

}
