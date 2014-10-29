package com.dkhs.portfolio.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class SetPasswordActivity extends ModelAcitivity implements OnClickListener {

    public static final int SET_PASSWORD_TYPE = 1001;
    public static final int LOGIN_TYPE = 1002;
    public static final int LOGOUT_TYPE = 1003;

    public Timer mTimer;// 定时器
    private static final int GET_CODE_UNABLE = 0;
    private static final int GET_CODE_ABLE = 1;
    private static final int SET_PASSWORD_MODE = 2001;
    private static final int CHANGE_PASSWORD_MODE = 2002;
    private static final int GET_VERICODE_MODE = 2003;
    private static int curtMode = 0;

    protected static final String TAG = "SetPasswordActivity";
    private int count;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_CODE_ABLE:
                    btn_get_code.setText(R.string.get_code);
                    btn_get_code.setClickable(true);
                    count = 0;
                    btn_get_code.setBackgroundResource(R.drawable.button_normal_blue);
                    mTimer.cancel();
                    break;
                case GET_CODE_UNABLE:
                    btn_get_code.setText((60 - count) + "秒");
                    break;
                default:
                    break;
            }
        };
    };
    private EditText et_pwd;
    private EditText et_check_pwd;
    private Button btn_get_code;
    private EditText et_original_pwd;
    private TextView tv_original_pwd;
    private String verifycode;
    private boolean is_setpassword;
    private TextView tv_setpwddesc;
    private EditText et_verifycode;
    private View rl_verifycode;
    private View rl_original;
    private int curType;
    private boolean needClear;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpassword_layout);
        initViews();
        setListener();
        initData();
    }

    public void initData() {
        verifycode = getIntent().getStringExtra("verify_code");
        curType = getIntent().getIntExtra("type", LOGIN_TYPE);
        is_setpassword = getIntent().getBooleanExtra("is_setpassword", false);

        needClear = getIntent().getBooleanExtra("needClear", false);

        if (curType == LOGOUT_TYPE || !TextUtils.isEmpty(verifycode)) {
            PortfolioApplication.getInstance().clearActivities();
        }
        if (!TextUtils.isEmpty(verifycode)) {
            rl_verifycode.setVisibility(View.GONE);
            tv_setpwddesc.setText(R.string.set_newpassword_desc);
            rl_original.setVisibility(View.GONE);
            et_original_pwd.setText(verifycode);
            btn_get_code.setClickable(false);
            btn_get_code.setBackgroundResource(R.drawable.button_unable);
            btn_get_code.setText("60秒");
            if (mTimer != null) {
                mTimer = null;
            }
            mTimer = new Timer();
            timerTask();
        } else {
            tv_setpwddesc.setText(R.string.set_newpassword_desc);
            if (is_setpassword) {
                rl_verifycode.setVisibility(View.GONE);
                tv_original_pwd.setText(R.string.original_password);
                et_original_pwd.setHint(R.string.hint_original_password);
            } else {
                rl_original.setVisibility(View.GONE);
                String desc = getResources().getString(R.string.set_password_desc);
                desc = String.format(desc, GlobalParams.MOBILE);
                tv_setpwddesc.setText(desc);
                tv_original_pwd.setText(R.string.original_password);
                et_original_pwd.setHint(R.string.hint_original_password);
            }
        }
    }

    public void setListener() {
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.btn_get_code).setOnClickListener(this);
    }

    public void initViews() {
        rl_verifycode = findViewById(R.id.rl_verifycode);
        rl_original = findViewById(R.id.rl_original);
        tv_setpwddesc = (TextView) findViewById(R.id.tv_setpwddesc);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_original_pwd = (TextView) findViewById(R.id.tv_original_pwd);
        et_original_pwd = (EditText) findViewById(R.id.et_original_pwd);
        et_verifycode = (EditText) findViewById(R.id.et_verifycode);
        et_check_pwd = (EditText) findViewById(R.id.et_check_pwd);
        btn_get_code = (Button) findViewById(R.id.btn_get_code);
        setTitle(R.string.set_password);
    }

    @Override
    public void onClick(View v) {
        if (!NetUtil.checkNetWork(this)) {
            PromptManager.showNoNetWork(this);
            return;
        }
        String pwd = et_pwd.getText().toString();
        String check_pwd = et_check_pwd.getText().toString();
        UserEngineImpl engine = new UserEngineImpl();
        switch (v.getId()) {
            case R.id.confirm:
                if (verifycode != null) {
                    // 说明从验证码登录
                    if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(check_pwd)) {
                        PromptManager.showToast(R.string.password_null);
                        return;
                    }
                    if (!pwd.equals(check_pwd)) {
                        PromptManager.showToast(R.string.password_unsame);
                        return;
                    }
                    PromptManager.showProgressDialog(this, R.string.changing_password, false);
                    curtMode = SET_PASSWORD_MODE;
                    // engine.setPassword(pwd, verifycode, listener);
                } else {
                    if (is_setpassword) {
                        // 设置过,用原始密码+新密码修改
                        String oldpassword = et_original_pwd.getText().toString();
                        if (TextUtils.isEmpty(oldpassword)) {
                            PromptManager.showToast(R.string.original_password_null);
                            return;
                        }
                        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(check_pwd)) {
                            PromptManager.showToast(R.string.password_null);
                            return;
                        }
                        if (!pwd.equals(check_pwd)) {
                            PromptManager.showToast(R.string.password_unsame);
                            return;
                        }
                        PromptManager.showProgressDialog(this, R.string.changing_password, false);
                        curtMode = CHANGE_PASSWORD_MODE;
                        engine.changePassword(oldpassword, pwd, listener);
                    } else {
                        // 没设置过,用验证码+新密码修改
                        verifycode = et_verifycode.getText().toString();
                        if (TextUtils.isEmpty(verifycode) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(check_pwd)) {
                            PromptManager.showToast(R.string.captcha_password_null);
                            return;
                        }
                        curtMode = SET_PASSWORD_MODE;
                        // engine.setPassword(pwd, verifycode, listener);
                    }
                }
                break;
            case R.id.btn_get_code:
                PromptManager.showProgressDialog(this, R.string.getting_code, false);
                curtMode = GET_VERICODE_MODE;
                engine.getVericode(GlobalParams.MOBILE, listener);
                btn_get_code.setClickable(false);
                btn_get_code.setBackgroundResource(R.drawable.button_unable);
                btn_get_code.setText("60秒");
                if (mTimer != null) {
                    mTimer = null;
                }
                mTimer = new Timer();
                timerTask();
                break;

            default:
                break;
        }
    }

    private ParseHttpListener<Object> listener = new ParseHttpListener<Object>() {

        public void onHttpFailure(int errCode, String errMsg) {
            PromptManager.closeProgressDialog();
            super.onHttpFailure(errCode, errMsg);
            if (curtMode == GET_VERICODE_MODE) {
                PromptManager.showToast(R.string.get_code_fail);
                btn_get_code.setClickable(true);
                count = 0;
                btn_get_code.setBackgroundResource(R.drawable.button_normal_blue);
                mTimer.cancel();
            }
        };

        public void onFailure(int errCode, String errMsg) {
            PromptManager.closeProgressDialog();
            super.onFailure(errCode, errMsg);
        };

        @Override
        protected Object parseDateTask(String jsonData) {
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            PromptManager.closeProgressDialog();
            Intent intent;
            switch (curtMode) {
                case SET_PASSWORD_MODE:
                    PromptManager.showToast(R.string.set_password_success);
                    if (curType == LOGIN_TYPE) {
                        // TODO 从登录界面过来,返回主页面
                        intent = new Intent(SetPasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if (curType == LOGOUT_TYPE) {
                        // TODO 从退出界面过来,删除数据库用户信息
                        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                        try {
                            dbUtils.deleteAll(UserEntity.class);
                            is_setpassword = true;
                            GlobalParams.ACCESS_TOCKEN = null;
                            GlobalParams.MOBILE = null;
                            intent = new Intent(SetPasswordActivity.this, NoAccountMainActivity.class);
                            startActivity(intent);
                        } catch (DbException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (curType == SET_PASSWORD_TYPE) {
                        // TODO 从设置密码界面过来
                        intent = new Intent(SetPasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                    break;
                case CHANGE_PASSWORD_MODE:
                    PromptManager.showToast(R.string.set_password_success);
                    break;
                case GET_VERICODE_MODE:
                    PromptManager.showToast(R.string.get_code_success);
                    break;

                default:
                    break;
            }
        }
    };

    private void timerTask() {
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (count < 60) {
                    handler.sendEmptyMessage(GET_CODE_UNABLE);
                    count++;
                } else {
                    handler.sendEmptyMessage(GET_CODE_ABLE);
                }
            }
        }, 0, 1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (curType == LOGIN_TYPE) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (curType == LOGOUT_TYPE) {
                    PromptManager.showToast(R.string.need_set_password);
                    return true;
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (curType == LOGIN_TYPE) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (curType == LOGOUT_TYPE) {
            if (!is_setpassword) {
                PromptManager.showToast(R.string.need_set_password);
                return;
            }
        }
        super.onBackPressed();
    }

}
