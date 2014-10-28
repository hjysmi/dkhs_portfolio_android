package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.UserEngineImpl;

public class SettingNameActivity extends ModelAcitivity implements OnClickListener {
    private CheckBox cbShowPassword;
    private EditText etPassword;
    private EditText etUserName;
    public static final String EXTRA_PHONENUM = "extra_phone";
    public static final String EXTRA_CODE = "extra_code";
    private String phoneNum;
    private String code;
    private Button rlfbutton;

    private UserEngineImpl engine;

    public static Intent newIntent(Context context, String phoneNum, String code) {
        Intent intent = new Intent(context, SettingNameActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_CODE, code);
        return intent;
    }

    private void handleExtras(Bundle extras) {

        phoneNum = extras.getString(EXTRA_PHONENUM);
        code = extras.getString(EXTRA_CODE);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_name);
        setTitle("设置昵称");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        engine = new UserEngineImpl();
        rlfbutton = (Button) findViewById(R.id.rlbutton);
        setRegistAble();
        cbShowPassword = (CheckBox) findViewById(R.id.cb_show_psw);
        etPassword = (EditText) findViewById(R.id.et_password);
        cbShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if(isChecked){
                // etPassword.setInputType(type)
                // }else{
                //
                // }

                if (isChecked) {

                    // 显示密码
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else { // 隐藏密码
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }

            }
        }

        );

        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    setRegistAble();
            }
        });
        etUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6 && s.length() <= 20) {

                    isUserNameAble = true;
                } else {
                    isUserNameAble = false;
                }
                setRegistAble();
            }
        });
    }

    private boolean isUserNameAble;
    private boolean isPswAble;

    private void setRegistAble() {
        if (isUserNameAble && isPswAble) {
            rlfbutton.setEnabled(true);
        } else {
            rlfbutton.setEnabled(false);
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param v
     * @return
     */
    @Override
    public void onClick(View v) {

    }

}
