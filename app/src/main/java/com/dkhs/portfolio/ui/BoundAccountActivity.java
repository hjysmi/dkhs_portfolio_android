package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.bean.ThreePlatform;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;

//import cn.sharesdk.tencent.qzone.QZone;

/**
 * 绑定第三方账号
 *
 * @author weiting
 */
public class BoundAccountActivity extends ModelAcitivity implements OnClickListener {
    private TextView boundTextEmail;
    private TextView boundTextQq;
    private TextView boundTextWeibo;
    private TextView boundTextPhone;
    private TextView boundTextWechat;
    private  WeakHandler platFormAction = new WeakHandler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message msg) {
                BoundAccountActivity.this.handleMessage(msg);
            return false;
        }
    }) ;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_bound_account);
        setTitle(R.string.title_binds);
        initView();
        initListener();
        UserEngineImpl.queryThreePlatBind(bindsListener);
        ShareSDK.initSDK(this);
        // ShareSDK.registerPlatform(Laiwang.class);
        ShareSDK.setConnTimeout(5000);
        ShareSDK.setReadTimeout(10000);
    }

    private void initView() {
        boundTextEmail = (TextView) findViewById(R.id.bound_text_email);
        boundTextQq = (TextView) findViewById(R.id.bound_text_qq);
        boundTextWeibo = (TextView) findViewById(R.id.bound_text_weibo);
        boundTextPhone = (TextView) findViewById(R.id.bound_text_phone);
        boundTextWechat = (TextView) findViewById(R.id.bound_text_wechat);
    }

    private void initListener() {
        boundTextEmail.setOnClickListener(this);
        boundTextQq.setOnClickListener(this);
        boundTextWeibo.setOnClickListener(this);
        boundTextPhone.setOnClickListener(this);
        boundTextWechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bound_text_phone: {
                startActivity(RLFActivity.bindPhoneIntent(this));
            }
            break;
            case R.id.bound_text_email:
                Intent intent = new Intent(this, BoundEmailActivity.class);
                startActivityForResult(intent, 5);
                break;
            case R.id.bound_text_qq:
                authPlatform(QZone.NAME);
                break;
            case R.id.bound_text_weibo:
                authPlatform(SinaWeibo.NAME);
                break;
            case R.id.bound_text_wechat:
                authPlatform(Wechat.NAME);
                break;
            default:
                break;
        }
    }

    private void authPlatform(String platformName) {
        // System.out.println("authPlatform:" + platformName);
        ShareSDK.removeCookieOnAuthorize(true);
        Platform plat = ShareSDK.getPlatform(platformName);
        if (plat.isValid()) {
            plat.removeAccount();
        }
        // 这里开启一下SSO，防止OneKeyShare分享时调用了oks.disableSSOWhenAuthorize();把SSO关闭了
//        plat.SSOSetting(true);
        plat.setPlatformActionListener(platFormActionListener);
        plat.showUser(null);
//        plat.authorize();

    }

    PlatformActionListener platFormActionListener = new PlatformActionListener() {

        @Override
        public void onError(Platform plat, int action, Throwable t) {
            t.printStackTrace();

            Message msg = new Message();
            msg.arg1 = 2;
            msg.arg2 = action;
            msg.obj = t;
            platFormAction.sendMessage( msg);
        }

        @Override
        public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
            Message msg = new Message();
            msg.arg1 = 1;
            msg.arg2 = action;
            msg.obj = plat;

            platFormAction.sendMessage(msg);
        }

        @Override
        public void onCancel(Platform plat, int action) {
            Message msg = new Message();
            msg.arg1 = 3;
            msg.arg2 = action;
            msg.obj = plat;
            platFormAction.sendMessage(msg);

        }
    };

    public void handleMessage(Message msg) {


        switch (msg.arg1) {
            case 1: {
                Platform plat  = (Platform) msg.obj;
                if (null != plat) {
                    String platname = plat.getName();
                    String imageUrl = "";
                    if (platname.contains(SinaWeibo.NAME)) {
                        platname = "weibo";
                        imageUrl = (String) (plat.getDb().get("icon"));
                        System.out.println("avatar_large:" + imageUrl);
                    } else if (platname.contains(Wechat.NAME)) {
                        platname = "weixin";
                        imageUrl = (String) (plat.getDb().get("icon"));
                    } else {
                        platname = "qq";
                        imageUrl = (String) (plat.getDb().get("icon"));
                        System.out.println("avatar_large:" + imageUrl);
                    }
                    ThreePlatform platData = new ThreePlatform();
                    platData.setAccess_token(plat.getDb().getToken());
                    platData.setOpenid(plat.getDb().getUserId());
                    platData.setAvatar(imageUrl);
                    platData.setRefresh_token("");
                    platData.setUsername(plat.getDb().getUserName());
                    UserEngineImpl.bindThreePlatform(plat.getDb().getUserId(), platname, platData,
                            bindsListener);
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
            }
            break;

            default:
                break;
        }
    }


    private ParseHttpListener<List<BindThreePlat>> bindsListener = new ParseHttpListener<List<BindThreePlat>>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }

        @Override
        protected List<BindThreePlat> parseDateTask(String jsonData) {
            LogUtils.e(jsonData);
            return DataParse.parseArrayJson(BindThreePlat.class, jsonData);
        }

        @Override
        protected void afterParseData(List<BindThreePlat> entity) {
            if (null != entity && entity.size() > 0) {
                Message msg = new Message();
                msg.what = 777;
                msg.obj = entity;
                updateHandler.sendMessage(msg);
            }

        }
    };


    WeakHandler updateHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            List<BindThreePlat> bindList = (List<BindThreePlat>) msg.obj;
            for (BindThreePlat plat : bindList) {
                if (plat.isStatus()) {
                    if (plat.getProvider().contains("weibo")) {
                        boundTextWeibo.setText(plat.getUsername());
                        boundTextWeibo.setEnabled(false);
                    } else if (plat.getProvider().contains("weixin")) {
                        boundTextWechat.setText(plat.getUsername());
                        boundTextWechat.setEnabled(false);
                    } else if (plat.getProvider().contains("qq")) {
                        boundTextQq.setText(plat.getUsername());
                        boundTextQq.setEnabled(false);
                    } else if (plat.getProvider().contains("mobile")) {
                        boundTextPhone.setText(plat.getUsername());
                        boundTextPhone.setEnabled(false);
                    } else if (plat.getProvider().contains("email")) {
                        boundTextEmail.setText(plat.getUsername());
                        boundTextEmail.setEnabled(false);
                    }
                }
            }
            return true;
        }
    });

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0) {
            case 5:

                break;

            default:
                break;
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_dound_third_account);

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_boundaccount;
    }
}
