package com.dkhs.portfolio.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.AppBean;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.bean.IdentityInfoBean;
import com.dkhs.portfolio.bean.ProInfoBean;
import com.dkhs.portfolio.bean.ProVerificationBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.AppUpdateEngine;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.ui.widget.UpdateDialog;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.portfolio.utils.WaterMarkUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 软件设置界面
 * 用于设置组合股是否公开
 * 当前有个问题:退出按钮暂时无法符合美工要求(当高度没达到全屏时,退出按钮位于屏幕最下方,当高度超过当前屏幕长度时,可以被顶到屏幕外去)
 *
 * @author weiting
 */
public class SettingActivity extends ModelAcitivity implements OnClickListener {


    //这里为什么是静态的
    public static boolean isSetPassword = true;
    private static final String EDIT_MODE = "userInfo";


    private Context context;
    private ImageView settingImageHead;
    private ImageView watermarkIv;
    private TextView settingTextNameText;
    // private Button btnLogin;
    // private View viewLogin;
    private View viewPassword;
    private UserEntity ue;
    private TextView settingSingText;
    private TextView tvBindPhone;
    private boolean login = false;
    private LinearLayout settingAccountLayout;
    @SuppressLint("HandlerLeak")
    private WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 333: {
                    // 当退出登录后，需要清空通知栏上的通知列表

                    PortfolioApplication.getInstance().exitApp();
                    Intent intent = new Intent(SettingActivity.this, LoginRegisterAcitvity.class);
                    startActivity(intent);
                    PromptManager.closeProgressDialog();
                }
                break;
                default:
                    break;
            }
            return false;
        }
    });

    public static Intent getEditUserInfoIntent(Context context) {


        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra(EDIT_MODE, true);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        context = this;
        // UserEngineImpl.queryThreePlatBind(bindsListener);
        initViews();
        setListener();
//        UserEngineImpl.queryThreePlatBind(bindsListener);
        // initData();
        // loadCombinationData();
    }

    public void initData() {
        if (PortfolioApplication.hasUserLogin()) {

            UserEngineImpl engine = new UserEngineImpl();
            engine.getSettingMessage(listener);
            UserEngineImpl.queryThreePlatBind(bindsListener);
            listener.setLoadingDialog(context);

            if (!TextUtils.isEmpty(GlobalParams.MOBILE)) {
                engine.isSetPassword(GlobalParams.MOBILE, new ParseHttpListener<Object>() {

                    @Override
                    protected Object parseDateTask(String jsonData) {
                        return jsonData;
                    }

                    @Override
                    protected void afterParseData(Object object) {
                        try {
                            JSONObject json = new JSONObject((String) object);
                            if (json.has("status")) {
                                isSetPassword = json.getBoolean("status");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    public void setListener() {
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_setpassword).setOnClickListener(this);
        viewPassword = findViewById(R.id.setting_layout_password);
        viewPassword.setVisibility(View.GONE);
        viewPassword.setOnClickListener(this);
        findViewById(R.id.setting_layout_username).setOnClickListener(this);
        findViewById(R.id.setting_layout_icon).setOnClickListener(this);
        findViewById(R.id.feed_back_layout).setOnClickListener(this);
        findViewById(R.id.rl_aboutus).setOnClickListener(this);
        findViewById(R.id.setting_layout_check_version).setOnClickListener(this);
        findViewById(R.id.setting_layout_sign).setOnClickListener(this);
        findViewById(R.id.setting_layout_bound).setOnClickListener(this);
        findViewById(R.id.tv_boundphone).setOnClickListener(this);
//        findViewById(R.id.setting_image_bound).setOnClickListener(this);
        settingSingText = (TextView) findViewById(R.id.setting_sing_text);

    }

    public void initViews() {
        // TODO Auto-generated method stub
        setTitle(R.string.setting);
        // btnLogin = (Button) findViewById(R.id.btn_login);
        // btnLogin.setOnClickListener(this);
        View viewUserInfo = findViewById(R.id.person_setting_parent);
        // viewLogin = findViewById(R.id.ll_login_layout);
        LinearLayout settingLayoutGroup = (LinearLayout) findViewById(R.id.setting_layout_group);
        settingImageHead = (ImageView) findViewById(R.id.iv_avatar);
        watermarkIv = (ImageView) findViewById(R.id.iv_water_mark);
        TextView settingTextAccountText = (TextView) findViewById(R.id.setting_text_account_text);
        settingTextNameText = (TextView) findViewById(R.id.setting_text_name_text);
        settingAccountLayout = (LinearLayout) findViewById(R.id.setting_account_layout);
        String account = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
        // account = setAccount(account);
        settingTextAccountText.setText(account);
        settingTextNameText.setText(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME));

        tvBindPhone = (TextView) findViewById(R.id.tv_boundphone);

        if (getEditModeEnable()) {
            setTitle(R.string.personal_setting);
            findViewById(R.id.feed_back_layout).setVisibility(View.GONE);
            findViewById(R.id.setting_layout_check_version).setVisibility(View.GONE);
            findViewById(R.id.rl_aboutus).setVisibility(View.GONE);
            findViewById(R.id.btn_exit).setVisibility(View.GONE);
            findViewById(R.id.rl_btn_exit).setVisibility(View.GONE);
            findViewById(R.id.setting_layout_bound).setVisibility(View.GONE);
            findViewById(R.id.line5).setVisibility(View.GONE);
            findViewById(R.id.line6).setVisibility(View.GONE);
            findViewById(R.id.line7).setVisibility(View.GONE);
            findViewById(R.id.line8).setVisibility(View.GONE);
            findViewById(R.id.setting_layout_boundphone).setVisibility(View.GONE);
            if (PortfolioApplication.hasUserLogin() && GlobalParams.LOGIN_USER.verified) {
                getProVerificationInfo();
            }
//            findViewById(R.id.line_tx). findViewById(R.id.line).setVisibility(View.GONE);
        } else {
            setTitle(R.string.setting);


//            findViewById(R.id.line4).setVisibility(View.GONE);
//            findViewById(R.id.line).setVisibility(View.GONE);
//            findViewById(R.id.line2).setVisibility(View.GONE);


            findViewById(R.id.setting_layout_sign).setVisibility(View.GONE);
            //需求取消该行
            settingAccountLayout.setVisibility(View.GONE);

            findViewById(R.id.setting_layout_icon).setVisibility(View.GONE);
            findViewById(R.id.setting_layout_username).setVisibility(View.GONE);
        }


        if (PortfolioApplication.hasUserLogin()) {
            // btnLogin.setVisibility(View.GONE);
            // viewLogin.setVisibility(View.GONE);
            viewUserInfo.setVisibility(View.VISIBLE);
            String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
            if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                // url = DKHSUrl.BASE_DEV_URL + url;
                BitmapUtils.display(settingImageHead, url);

            } else {
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_head);
                b = UIUtils.toRoundBitmap(b);
                settingImageHead.setImageBitmap(b);

            }
            boolean isVerified = PortfolioPreferenceManager.getBooleanValue(PortfolioPreferenceManager.KEY_VERIFIED);
            int verifiedType = PortfolioPreferenceManager.getIntValue(PortfolioPreferenceManager.KEY_VERIFIED_TYPE);
            WaterMarkUtil.calWaterMarkImage(watermarkIv, isVerified, verifiedType);
        } else {
            // viewLogin.setVisibility(View.GONE);
            viewUserInfo.setVisibility(View.GONE);
            findViewById(R.id.btn_exit).setVisibility(View.GONE);

        }


    }

    public String setAccount(String account) {
        if (account.contains("@")) {
            int k = account.indexOf("@");
            account = account.substring(0, k - 3) + "***" + account.substring(k, account.length());
        } else {
            account = account.substring(0, account.length() - 5) + "***"
                    + account.substring(account.length() - 2, account.length());
        }
        return account;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_login: {
                UIUtils.iStartLoginActivity(this);
            }
            break;
            case R.id.btn_exit:
                if (isSetPassword) {

                    new Thread() {
                        public void run() {
                            DbUtils dbUtils = AppConfig.getDBUtils();

                            try {
                                dbUtils.deleteAll(UserEntity.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }

                        ;
                    }.start();

                    GlobalParams.clearUserInfo();

                    // 断开融云连接
                    // RongIM.getInstance().disconnect(false);
                    MessageManager.getInstance().disConnect(this);

                    // 注销消息中心的联系，需要一段延迟
                    handler.sendEmptyMessageDelayed(333, 600);
                    PromptManager.showProgressDialog(this, "", false);

                } else {
                    intent = new Intent(this, SetPasswordActivity.class);

                    startActivity(intent);
                }
                break;
            case R.id.btn_setpassword:
                if (UIUtils.iStartLoginActivity(this)) {
                    return;
                }
                if (isSetPassword) {
                    intent = new Intent(this, SetPasswordActivity.class);
                    // intent.putExtra("type", SetPasswordActivity.SET_PASSWORD_TYPE);
                    intent.putExtra("is_setpassword", isSetPassword);
                } else {
                    intent = new Intent(this, SetPasswordActivity.class);
                    // intent.putExtra("type", SetPasswordActivity.SET_PASSWORD_TYPE);
                    intent.putExtra("needClear", false);
                    intent.putExtra("is_setpassword", isSetPassword);
                }
                UIUtils.startAnimationActivity(this, intent);

                break;
            case R.id.setting_layout_password:
                if (UIUtils.iStartLoginActivity(this)) {
                    return;
                }
                intent = new Intent(this, ModifyPswActivity.class);
                UIUtils.startAnimationActivity(this, intent);
                break;
            case R.id.setting_layout_username:
                if (UIUtils.iStartLoginActivity(this)) {
                    return;
                }
                intent = new Intent(this, ModifyUNameActivity.class);
                startActivityForResult(intent, 6);
                UIUtils.setOverridePendingAnin(this);
                break;
            case R.id.setting_layout_icon:
                if (UIUtils.iStartLoginActivity(this)) {
                    return;
                }
                intent = new Intent(context, PickupPhotoActivity.class);
                startActivityForResult(intent, 5);
                UIUtils.setOverridePendingSlideFormBottomAnim(this);
                break;
            case R.id.feed_back_layout:
                intent = new Intent(this, FeedBackActivity.class);
                UIUtils.startAnimationActivity(this, intent);
                break;
            case R.id.rl_aboutus: {
                intent = new Intent(this, AboutUsActivity.class);
                UIUtils.startAnimationActivity(this, intent);
            }
            break;
            case R.id.setting_layout_check_version:
                AppUpdateEngine.getAppVersion("portfolio_android", userInfoListener);
                userInfoListener.setLoadingDialog(context);
                break;
            case R.id.setting_layout_sign:
                if (UIUtils.iStartLoginActivity(this)) {
                    return;
                }
                intent = new Intent(this, ModifyUserSignActivity.class);
                Bundle b = new Bundle();
                if (null != ue)
                    b.putString(ModifyUserSignActivity.DESCRIPTION, ue.getRawDescription());
                intent.putExtras(b);
                UIUtils.startAnimationActivity(this, intent);
                break;
            case R.id.setting_layout_bound:
                if (UIUtils.iStartLoginActivity(this)) {
                    return;
                }
                intent = new Intent(this, BoundAccountActivity.class);
                UIUtils.startAnimationActivity(this, intent);
                break;
            case R.id.tv_boundphone:
                startActivity(RLFActivity.bindPhoneIntent(this));
                break;
            case R.id.setting_material:
                if(pro != null){
                    Intent it = VerifiedProFileActivity.getInent(this,pro.cert_description,pro.image1,pro.image2,
                            pro.image3,pro.image4,pro.image5,pro.image6);
                    UIUtils.startAnimationActivity(this,it);
                }
                break;
            default:
                break;
        }
    }

    ParseHttpListener userInfoListener = new ParseHttpListener<AppBean>() {

        @Override
        protected AppBean parseDateTask(String jsonData) {

            return DataParse.parseObjectJson(AppBean.class, jsonData);
        }

        @Override
        protected void afterParseData(AppBean object) {
            try {
                if (null != object) {
                    final AppBean bean = object;
                    PackageManager manager = context.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                    String version = info.versionName;
                    if (object.isNewVersion(version)) {
                        UpdateDialog alert = new UpdateDialog(context);
                        alert.showByAppBean(object);
                    } else {
                        PromptManager.showToast("当前已经是最新版本");
                    }

                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            settingTextNameText.setText(PortfolioPreferenceManager
                    .getStringValue(PortfolioPreferenceManager.KEY_USERNAME));
            String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
            if (!TextUtils.isEmpty(url)) {
                // url = DKHSUrl.BASE_DEV_URL + url;
                BitmapUtils.display(settingImageHead, url);
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        initData();
        super.onResume();
    }

    private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }

        ;

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                return DataParse.parseObjectJson(UserEntity.class, json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(UserEntity entity) {

            // PromptManager.closeProgressDialog();
            if (null != entity) {
                ue = entity;
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERNAME, entity.getUsername());
                settingTextNameText.setText(entity.getUsername());
                settingSingText.setText(ue.getDescription());
            }
        }
    };
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
            if (!entity.isEmpty()) {
                for (int i = 0; i < entity.size(); i++) {
                    BindThreePlat palt = entity.get(i);


                    if (palt.getProvider().equalsIgnoreCase("mobile") || palt.getProvider().equalsIgnoreCase("email")) {
                        if (palt.isStatus()) {
                            if (!getEditModeEnable()) {
                                viewPassword.setVisibility(View.VISIBLE);
                            }

                        }
                    }


                    if (palt.isStatus() && palt.getProvider().contains("mobile")) {
                        tvBindPhone.setText(palt.getUsername());
                        tvBindPhone.setEnabled(false);

                    }


                }
                // Message msg = updateHandler.obtainMessage(777);
                // msg.obj = entity;
                // msg.sendToTarget();
            }

        }
    };

    WeakHandler updateHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            List<BindThreePlat> bindList = (List<BindThreePlat>) msg.obj;
            for (BindThreePlat plat : bindList) {
                if (plat.isStatus()) {
                    if (plat.getProvider().contains("mobile")) {
                        login = true;
                    } else if (plat.getProvider().contains("email")) {
                        login = true;
                    }
                }
            }
            if (login) {
                settingAccountLayout.setVisibility(View.GONE);
            } else {
                settingAccountLayout.setVisibility(View.GONE);
            }
            return false;
        }
    });
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_setting);


    public boolean getEditModeEnable() {

        return getIntent().getBooleanExtra(EDIT_MODE, false);
    }

    private void getProVerificationInfo() {
        DKHSClient.requestByGet(new ParseHttpListener<ProVerificationBean>() {
            @Override
            protected ProVerificationBean parseDateTask(String jsonData) {
                return DataParse.parseObjectJson(ProVerificationBean.class, jsonData);
            }

            @Override
            protected void afterParseData(ProVerificationBean bean) {
                updateProVerificationInfo(bean);
            }
        }, DKHSUrl.User.get_pro_verification);
    }

    private ProInfoBean pro;
    private void updateProVerificationInfo(ProVerificationBean info) {
        IdentityInfoBean identity = info.identity;
        pro = info.pro;
        if(pro == null || identity == null)
            return;
        String name = identity.real_name;
        if(!TextUtils.isEmpty(name)){
//            name = name.replace(name.substring(0, 1), "*");
            ((TextView) findViewById(R.id.tv_real_name_value)).setText(name);
        }
        ((TextView) findViewById(R.id.tv_id_card_value)).setText(identity.id_card_no_masked);
        UserEntity user = GlobalParams.LOGIN_USER;
        if(user != null){
            StringBuilder residence = new StringBuilder();
            if(!TextUtils.isEmpty(user.getProvince())){
                residence.append(user.getProvince());
            }
            if(!TextUtils.isEmpty(user.getCity())){
                residence.append(user.getProvince()+" "+user.getCity());
            }
            ((TextView) findViewById(R.id.tv_city_value)).setText(residence.toString());
        }
        ((TextView) findViewById(R.id.tv_verified_type_value)).setText(getVerifiedName(pro.verified_type));
        if (pro.verified_type == UserEntity.VERIFIEDTYPE.EXPERT.getTypeid()) {
            findViewById(R.id.setting_cert_no).setVisibility(View.GONE);
            findViewById(R.id.setting_organize).setVisibility(View.GONE);
            findViewById(R.id.setting_material).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.tv_material_value)).setText(pro.cert_description);
            findViewById(R.id.setting_material).setOnClickListener(this);
        } else {
            ((TextView) findViewById(R.id.tv_cert_no_value)).setText(pro.cert_no);
            ((TextView) findViewById(R.id.tv_organize_value)).setText(pro.org_profile.name);
            findViewById(R.id.setting_cert_no).setVisibility(View.VISIBLE);
            findViewById(R.id.setting_organize).setVisibility(View.VISIBLE);
            findViewById(R.id.setting_material).setVisibility(View.GONE);
        }
        findViewById(R.id.ll_pro_ver).setVisibility(View.VISIBLE);
    }

    //0, 投资牛人 1, 投资顾问 2, 分析师 3, 基金执业 4, 期货执业
    private String getVerifiedName(int type) {
        String verifiedName = "";
        switch (type) {
            case 0:
                verifiedName = getString(R.string.verified_type_expert);
                break;
            case 1:
                verifiedName = getString(R.string.verified_type_adviser);
                break;
            case 2:
                verifiedName = getString(R.string.verified_type_analyst);
                break;
            case 3:
                verifiedName = getString(R.string.verified_type_fund_certificate);
                break;
            case 4:
                verifiedName = getString(R.string.verified_type_futures_certificate);
                break;
        }
        return verifiedName;
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_setting;
    }
}
