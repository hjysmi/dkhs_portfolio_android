package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SignupBean;
import com.dkhs.portfolio.bean.ThreePlatform;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.SIMCardInfo;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;

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
    // private CheckBox cbRequestTestServer;
    private TextView tvAnnoyLogin;
    private View ivWeibo, ivQQ, ivWeixin;

    private String mUserAccout;
    private UserEngineImpl engine;

    public static final String EXTRA_PHONENUM = "extra_phone";
    public static final String EXTRA_LOGINANNOY = "extra_loginannoy";

    private boolean isLoginByAnnoy = false;

    public static Intent getLoginActivity(Context context, String phoneNum) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        return intent;
    }

    public static Intent loginActivityByAnnoy(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(EXTRA_LOGINANNOY, true);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // setBackTitle(R.string.login_title);
        // setTitle(R.string.login);
        // 默认为8030地址，即预发布地址

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        engine = new UserEngineImpl();

        initViews();

        initDatas();
        setListener();
        LogUtils.customTagPrefix = "LoginActivity";

        ShareSDK.initSDK(this);
        // ShareSDK.registerPlatform(Laiwang.class);
        ShareSDK.setConnTimeout(5000);
        ShareSDK.setReadTimeout(10000);
        if (PortfolioApplication.getInstance().isDebug()) {
            Intent intent = new Intent(this, GettingUrlForAPPActivity.class);
            startActivity(intent);
        } else {

        }
    }

    /**
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void initDatas() {
        mUserAccout = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
        if (isLoginByAnnoy) {
            setupDefalutUserInfo();
            setTitle(R.string.login);
            // getBtnBack().setText(R.string.cancel);
            getBtnBack().setText("取消");
            // getBtnBack().setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            if (!TextUtils.isEmpty(phoneNum)) {
                etUserName.setText(phoneNum);
                setupLastUserInfo();
            }
        } else {
            hideHead();
            if (!TextUtils.isEmpty(phoneNum)) {
                etUserName.setText(phoneNum);
            } else if (!TextUtils.isEmpty(mUserAccout)) {
                etUserName.setText(mUserAccout);
                setupLastUserInfo();
            }

        }
    }

    private void setupLastUserInfo() {
        tvUsername.setText(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME));
        String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
        if (!TextUtils.isEmpty(url)) {
            BitmapUtils bitmapUtils = new BitmapUtils(this);
            // url = DKHSUrl.BASE_DEV_URL + url;
            bitmapUtils.display(ivHeader, url);
        }
    }

    private void setupDefalutUserInfo() {
        tvUsername.setText("");
        ivHeader.setImageResource(R.drawable.ic_user_head);
    }

    private void handleExtras(Bundle extras) {

        phoneNum = extras.getString(EXTRA_PHONENUM);
        if (TextUtils.isEmpty(phoneNum)) {
            phoneNum = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
        }
        isLoginByAnnoy = extras.getBoolean(EXTRA_LOGINANNOY);

    }

    public void setListener() {
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.tv_forget).setOnClickListener(this);

    }

    public void initViews() {
        // if (!isLoginByAnnoy) {
        // getBtnBack().setVisibility(View.GONE);
        // } else {
        // getBtnBack().setVisibility(View.VISIBLE);
        // }

        etUserName = (EditText) findViewById(R.id.username);
        ivHeader = (ImageView) findViewById(R.id.iv_header);

        etPassword = (EditText) findViewById(R.id.password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        rlfbutton = (Button) findViewById(R.id.login);

        ivWeibo = findViewById(R.id.iv_weibo);
        ivQQ = findViewById(R.id.iv_qq);
        ivWeixin = findViewById(R.id.iv_weixin);
        ivWeibo.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
        ivWeixin.setOnClickListener(this);

        tvRegister.setOnClickListener(this);
        tvAnnoyLogin = (TextView) findViewById(R.id.tv_is_request_test);
        tvAnnoyLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalParams.ACCESS_TOCKEN = "";
                goMainPage();
            }
        });

        // if (BuildConfig.DEBUG) {
        // cbRequestTestServer.setVisibility(View.VISIBLE);
        // } else {
        // cbRequestTestServer.setVisibility(View.GONE);
        // }

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
     * @param string
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
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
            case R.id.iv_weibo: {
                authPlatform(SinaWeibo.NAME);
                // authorize(new SinaWeibo(this));
            }
            break;
            case R.id.iv_qq: {
                authPlatform(QZone.NAME);

                // authorize(new QZone(this));
            }
            break;
            case R.id.iv_weixin: {
                authPlatform(Wechat.NAME);
                // authorize(new Wechat(this));
            }
            break;
            default:
                break;
        }
    }

    // private void authorize(Platform plat) {
    // if (plat.isValid()) {
    // plat.removeAccount();
    // }
    // plat.setPlatformActionListener(platFormActionListener);
    // plat.SSOSetting(true);
    // plat.showUser(null);
    // }

    private void authPlatform(String platformName) {

        if (!platformName.equals(Wechat.NAME)) {
            PromptManager.showProgressDialog(this, "", false);
        }
        // System.out.println("authPlatform:" + platformName);
        ShareSDK.removeCookieOnAuthorize(true);
        Platform plat = ShareSDK.getPlatform(platformName);
        if (plat.isValid()) {
            plat.removeAccount();
        }
        // 这里开启一下SSO，防止OneKeyShare分享时调用了oks.disableSSOWhenAuthorize();把SSO关闭了
        // plat.SSOSetting(false);
        plat.setPlatformActionListener(platFormActionListener);
        plat.showUser(null);
        // plat.authorize();
    }

    private void login() {
        userName = etUserName.getText().toString();
        String passWord = etPassword.getText().toString();

        checkAndLogin(userName, passWord);

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
        if (checkEmail(userName)) {
            PromptManager.showProgressDialog(this, R.string.logining);
            engine.login(userName, passWord, ConstantValue.IS_EMAIL, listener);
        } else if (SIMCardInfo.isMobileNO(userName)) {
            PromptManager.showProgressDialog(this, R.string.logining);
            engine.login(userName, passWord, ConstantValue.IS_MOBILE, listener);
        } else {
            // Toast.makeText(this, "请输入手机号或者邮箱", Toast.LENGTH_SHORT).show();
            etUserName.setError(Html.fromHtml("<font color='red'>请输入手机号或者邮箱</font>"));
            etUserName.requestFocus();
        }
    }

    private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        // public void onHttpFailure(int errCode, String errMsg) {
        // PromptManager.closeProgressDialog();
        // super.onHttpFailure(errCode, errMsg);
        // };

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            PromptManager.closeProgressDialog();
        }

        ;

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                UserEntity entity = DataParse.parseObjectJson(UserEntity.class, json.getJSONObject("user"));
                String token = (String) json.getJSONObject("token").get("access_token");
                entity.setAccess_token(token);
                if (SIMCardInfo.isMobileNO(userName)) {
                    GlobalParams.MOBILE = userName;
                    entity.setMobile(userName);
                }
                engine.saveLoginUserInfo(entity);
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT, etUserName.getText()
                        .toString());
                return entity;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(UserEntity entity) {
            PromptManager.closeProgressDialog();
            PortfolioApplication.getInstance().exitApp();
            PortfolioApplication.getInstance().setLogin(true);
            goMainPage();
        }
    };

    // private void goMainPage() {
    // Intent intent = new Intent(LoginActivity.this, NewMainActivity.class);
    // startActivity(intent);
    // finish();
    // }

    private String userName;

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

    PlatformActionListener platFormActionListener = new PlatformActionListener() {

        @Override
        public void onError(Platform plat, int action, Throwable t) {
            // System.out.println("PlatformActionListener onError()");
            t.printStackTrace();

            Message msg = new Message();
            msg.arg1 = 2;
            msg.arg2 = action;
            msg.obj = t;
            platFormAction.sendMessage(msg);
        }

        @Override
        public void onComplete(Platform plat, int action, HashMap<String, Object> res) {

            // Toast.makeText(getApplicationContext(), text, duration)
            // System.out.println("PlatformActionListener onComplete()");
            // System.out.println("action:" + action);
            // System.out.println("platform user id:" + plat.getDb().getUserId());
            // System.out.println("platform user name:" + plat.getDb().getUserName());
            // System.out.println("platform  name:" + plat.getName());
            // System.out.println("platform  nickname:" + plat.getDb().get("nickname"));
            // System.out.println("platform  getToken:" + plat.getDb().getToken());

            res.put("plat", plat);
            Message msg = new Message();
            msg.arg1 = 1;
            msg.arg2 = action;
            msg.obj = res;

            platFormAction.sendMessage(msg);

        }

        @Override
        public void onCancel(Platform plat, int action) {

            System.out.println("PlatformActionListener onCancel()");
            Message msg = new Message();
            msg.arg1 = 3;
            msg.arg2 = action;
            msg.obj = plat;
            platFormAction.sendMessage(msg);

        }
    };
    Handler platFormAction = new Handler() {
        public void handleMessage(Message msg) {
            PromptManager.closeProgressDialog();
            switch (msg.arg1) {
                case 1: {
                    HashMap<String, Object> res = (HashMap<String, Object>) msg.obj;
                    // Platform plat = (Platform) msg.obj;
                    Platform plat = (Platform) (res.containsKey("plat") ? res.get("plat") : null);

                    if (null != plat) {

                        String platname = plat.getName();
                        String imageUrl = "";
                        // Toast.makeText(getApplicationContext(), "platname:" + platname, Toast.LENGTH_SHORT).show();
                        if (platname.contains(SinaWeibo.NAME)) {
                            platname = "weibo";
                            imageUrl = (String) (res.containsKey("avatar_large") ? res.get("avatar_large") : "");
                        } else if (platname.contains(Wechat.NAME)) {
                            platname = "weixin";
                        } else {
                            platname = "qq";
                            imageUrl = (String) (res.containsKey("figureurl_qq_2") ? res.get("figureurl_qq_2") : "");
                        }
                        ThreePlatform platData = new ThreePlatform();
                        platData.setAccess_token(plat.getDb().getToken());
                        platData.setOpenid(plat.getDb().getUserId());
                        platData.setAvatar(imageUrl);
                        platData.setRefresh_token("");
                        phoneNum = "";
                        engine.registerThreePlatform(plat.getDb().getUserName(), plat.getDb().getUserId(), platname,
                                platData, registerListener.setLoadingDialog(LoginActivity.this, false));
                    }
                }
                break;
                case 2: {
                    String failtext = "";
                    if (msg.obj instanceof WechatClientNotExistException) {
                        failtext = getResources().getString(R.string.wechat_client_inavailable);
                    } else if (msg.obj instanceof WechatTimelineNotSupportedException) {
                        failtext = getResources().getString(R.string.wechat_client_inavailable);
                    } else if (msg.obj instanceof java.lang.Throwable && msg.obj.toString() != null
                            && msg.obj.toString().contains("prevent duplicate publication")) {

                        failtext = getResources().getString(R.string.oauth_fail);
                    } else if (msg.obj.toString().contains("error")) {
                        failtext = getResources().getString(R.string.oauth_fail);

                    } else {
                        failtext = getResources().getString(R.string.oauth_fail);
                    }
                    PromptManager.showToast(failtext);

                }
                break;
                case 3: {
                    // Toast.makeText(getApplicationContext(), "PlatformActionListener onCancel()", Toast.LENGTH_SHORT)
                    // .show();

                }
                break;

                default:
                    break;
            }
        }

        ;
    };

    private ParseHttpListener<SignupBean> registerListener = new ParseHttpListener<SignupBean>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }

        ;

        @Override
        protected SignupBean parseDateTask(String jsonData) {
            try {
                SignupBean signupBean = DataParse.parseObjectJson(SignupBean.class, jsonData);
                JSONObject json = new JSONObject(jsonData);

                // json.optBoolean("is_new_user");
                // UserEntity entity = DataParse.parseObjectJson(UserEntity.class, json.getJSONObject("user"));
                String token = (String) json.getJSONObject("token").get("access_token");
                signupBean.getUser().setAccess_token(token);
                signupBean.getUser().setMobile(phoneNum);
                engine.saveLoginUserInfo(signupBean.getUser());
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT, phoneNum);
                return signupBean;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(SignupBean entity) {

            // PromptManager.closeProgressDialog();
            if (null != entity && !entity.isNewUser()) {
                goMainPage();
            } else if (null != entity && entity.isNewUser()) {
                uploadUserFollowStock();
            }
        }
    };

    ParseHttpListener uploadStockListner = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            new VisitorDataEngine().delAllOptionalStock();
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            uploadUserFollowCombination();

        }
    }.setLoadingDialog(this, "正在登陆", false);

    private VisitorDataEngine visitorEngine;

    public void uploadUserFollowStock() {
        if (null == visitorEngine) {
            visitorEngine = new VisitorDataEngine();
        }
        if (!visitorEngine.uploadUserFollowStock(uploadStockListner)) {
            uploadUserFollowCombination();
        }

    }

    ParseHttpListener uploadCombinationListener = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            new VisitorDataEngine().delAllCombinationBean();
            return null;
        }

        @Override
        protected void afterParseData(Object object) {

            Log.i("uploadUserFollowCombination", "uploadUserFollowCombination success");
            goMainPage();

        }
    }.setLoadingDialog(this, "正在登陆", false);

    public void uploadUserFollowCombination() {
        if (!visitorEngine.uploadUserFollowCombination(uploadCombinationListener)) {
            goMainPage();
        }
    }

    private void goMainPage() {

        // 设置小红点可以出现
        PortfolioApplication.getInstance().exitApp();
        Intent intent = new Intent(LoginActivity.this, NewMainActivity.class);
        startActivity(intent);
        finish();
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_login);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }
}