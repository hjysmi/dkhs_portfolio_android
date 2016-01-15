package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangcm on 2015/9/14.15:02
 */
public class VerifyLoginPasswordActivity extends ModelAcitivity {

    @ViewInject(R.id.btn_verify)
    private Button btn_verify;

    @ViewInject(R.id.et_password)
    private EditText et_password;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_verify_login_password);
        ViewUtils.inject(this);
        setTitle(R.string.verify_password);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s) && s.length() >= 6){
                    btn_verify.setEnabled(true);
                }else{
                    btn_verify.setEnabled(false);
                }
            }
        });
    }
    @OnClick(R.id.btn_verify)
    private void onclick(View v){
        ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {
            @Override
            protected UserEntity parseDateTask(String jsonData) {
                UserEntity user = null;
                try{
                    JSONObject json = new JSONObject(jsonData);
                    user = DataParse.parseObjectJson(UserEntity.class, json.getJSONObject("user"));
                }catch (Exception e){

                }
                return user;
            }

            @Override
            protected void afterParseData(UserEntity user) {
                if(user != null){
                    setResult(200);
                    manualFinish();
                }else{
                    PromptManager.showToast("密码错误，请重新验证");
                }
            }
        };
        new UserEngineImpl().login(GlobalParams.MOBILE, et_password.getText().toString(), ConstantValue.IS_MOBILE, listener.setLoadingDialog(mContext,false));
    }

    private ParseHttpListener<List<BindThreePlat>> bindsListener = new ParseHttpListener<List<BindThreePlat>>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }


        @Override
        protected List<BindThreePlat> parseDateTask(String jsonData) {

            return DataParse.parseArrayJson(BindThreePlat.class, jsonData);
        }

        @Override
        protected void afterParseData(List<BindThreePlat> entity) {

        }
    };

    @Override
    public void onBackPressed() {
        setResult(500);
        super.onBackPressed();
    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_verify_login_password;
    }
}
