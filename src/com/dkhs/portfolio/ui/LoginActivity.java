package com.dkhs.portfolio.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.BuildConfig;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

public class LoginActivity extends ModelAcitivity implements OnClickListener {

    public static final int REQUEST_REGIST = 0;
    public static final int RESPONSE_REGIST = 1;
    private EditText etUserName;
    private EditText etPassword;
    private TextView tvRegister;
    private TextView tvUsername;
    private Button rlfbutton;
    private ImageView ivHeader;
    private String phoneNum;
    private CheckBox cbRequestTestServer;

    private String mUserAccout;

    public static final String EXTRA_PHONENUM = "extra_phone";

    public static Intent getLoginActivity(Context context, String phoneNum) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hideHead();
        // setBackTitle(R.string.login_title);
        // setTitle(R.string.login);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        getBtnBack().setVisibility(View.GONE);

        initViews();
        initDatas();
        setListener();
        LogUtils.customTagPrefix = "LoginActivity";
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initDatas() {
        mUserAccout = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
        if (!TextUtils.isEmpty(phoneNum)) {
            etUserName.setText(phoneNum);
        } else if (!TextUtils.isEmpty(mUserAccout)) {
            etUserName.setText(mUserAccout);
            setupLastUserInfo();
        }

    }

    private void setupLastUserInfo() {
        tvUsername.setText(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME));
        String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
        if (!TextUtils.isEmpty(url)) {
            BitmapUtils bitmapUtils = new BitmapUtils(this);
            url = DKHSUrl.BASE_DEV_URL + url;
            bitmapUtils.display(ivHeader, url);
        }
    }

    private void setupDefalutUserInfo() {
        tvUsername.setText("");
        ivHeader.setImageResource(R.drawable.ic_user_head);
    }

    private void handleExtras(Bundle extras) {

        phoneNum = extras.getString(EXTRA_PHONENUM);

    }

    public void setListener() {
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.tv_forget).setOnClickListener(this);

    }

    public void initViews() {
        etUserName = (EditText) findViewById(R.id.username);
        ivHeader = (ImageView) findViewById(R.id.iv_header);

        etPassword = (EditText) findViewById(R.id.password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        rlfbutton = (Button) findViewById(R.id.login);

        tvRegister.setOnClickListener(this);
        cbRequestTestServer = (CheckBox) findViewById(R.id.cb_is_request_test);
        cbRequestTestServer.setChecked(PortfolioPreferenceManager.isRequestByTestServer());
        cbRequestTestServer.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                PortfolioPreferenceManager.setRequestByTestServer(isChecked);
            }
        });

        if (BuildConfig.DEBUG) {
            cbRequestTestServer.setVisibility(View.VISIBLE);
        } else {
            cbRequestTestServer.setVisibility(View.GONE);
        }

        etUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && etPassword.getText().length() > 0) {
                    rlfbutton.setEnabled(true);
                } else {
                    rlfbutton.setEnabled(false);
                }
                isLastUserAccount(s.toString());
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && etUserName.getText().length() > 0) {
                    rlfbutton.setEnabled(true);
                } else {
                    rlfbutton.setEnabled(false);
                }
            }
        });

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param string
     * @return void
     */
    protected void isLastUserAccount(String accountText) {
        if (accountText.equalsIgnoreCase(mUserAccout)) {
            setupLastUserInfo();
        } else {
            setupDefalutUserInfo();
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.tv_forget:
                Intent intent = new Intent(LoginActivity.this, ForgetPswActivity.class);
                // intent.putExtra("activity_type", RLFActivity.FORGET_PSW_TYPE);
                startActivity(intent);
                break;
            case R.id.tv_register: {
                Intent intent2 = new Intent(this, RLFActivity.class);
                intent2.putExtra("activity_type", RLFActivity.REGIST_TYPE);
                startActivity(intent2);
            }
                break;
            default:
                break;
        }
    }

    private void login() {
        userName = etUserName.getText().toString();
        String passWord = etPassword.getText().toString();
        if (NetUtil.checkNetWork(this)) {
            checkAndLogin(userName, passWord);
        } else {
            PromptManager.showNoNetWork(this);
        }

    }

    /**
     * 校验用户
     * 
     * @param userName
     * @param passWord
     */
    private void checkAndLogin(String userName, String passWord) {
        if (TextUtils.isEmpty(userName)) {
            // Toast.makeText(this, "用户或者密码不能为空", Toast.LENGTH_SHORT).show();
            etUserName.setError(Html.fromHtml("<font color='red'>用户名不能为空</font>"));
            etUserName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            etPassword.setError(Html.fromHtml("<font color='red'>密码不能为空</font>"));
            etPassword.requestFocus();
            return;
        }
        UserEngineImpl engine = new UserEngineImpl();
        if (checkEmail(userName)) {
            PromptManager.showProgressDialog(this, R.string.logining);
            engine.login(userName, passWord, ConstantValue.IS_EMAIL, listener);
        } else if (isMobileNO(userName)) {
            PromptManager.showProgressDialog(this, R.string.logining);
            engine.login(userName, passWord, ConstantValue.IS_MOBILE, listener);
        } else {
            // Toast.makeText(this, "请输入手机号或者邮箱", Toast.LENGTH_SHORT).show();
            etUserName.setError(Html.fromHtml("<font color='red'>请输入手机号或者邮箱</font>"));
            etUserName.requestFocus();
        }
    }

    private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        public void onHttpFailure(int errCode, String errMsg) {
            PromptManager.closeProgressDialog();
            super.onHttpFailure(errCode, errMsg);
        };

        public void onFailure(int errCode, String errMsg) {
            PromptManager.closeProgressDialog();
            super.onFailure(errCode, errMsg);
        };

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                UserEntity entity = DataParse.parseObjectJson(UserEntity.class, json.getJSONObject("user"));
                String token = (String) json.getJSONObject("token").get("access_token");
                entity.setAccess_token(token);
                GlobalParams.ACCESS_TOCKEN = entity.getAccess_token();
                if (isMobileNO(userName)) {
                    GlobalParams.MOBILE = userName;
                    entity.setMobile(userName);
                }
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERNAME, entity.getUsername());
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL,
                        entity.getAvatar_md());
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT, etUserName.getText()
                        .toString());
                saveUser(entity);
                return entity;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(UserEntity entity) {
            PromptManager.closeProgressDialog();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    private String userName;

    private void saveUser(final UserEntity user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                UserEntity entity = UserEntityDesUtil.decode(user, "DECODE", ConstantValue.DES_PASSWORD);
                DbUtils dbutil = DbUtils.create(PortfolioApplication.getInstance());
                UserEntity dbentity;
                try {
                    dbentity = dbutil.findFirst(UserEntity.class);
                    if (dbentity != null) {
                        dbutil.delete(dbentity);
                    }
                    dbutil.save(entity);
                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 验证邮箱地址是否正确
     * 
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 验证手机号码
     * 
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGIST && resultCode == RESPONSE_REGIST) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 检查软键盘是否弹出状态,是,就隐藏
     */
    protected void checkSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 隐藏软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }
}
