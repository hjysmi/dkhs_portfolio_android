package com.dkhs.portfolio.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.AppBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * 软件设置界面
 * 用于设置组合股是否公开
 * 当前有个问题:退出按钮暂时无法符合美工要求(当高度没达到全屏时,退出按钮位于屏幕最下方,当高度超过当前屏幕长度时,可以被顶到屏幕外去)
 * 
 * @author weiting
 * 
 */
public class SettingActivity extends ModelAcitivity implements OnClickListener {
    public static boolean isSetPassword = true;
    private LinearLayout settingLayoutGroup;
    private Context context;
    private ImageView settingImageHead;
    private TextView settingTextAccountText;
    private TextView settingTextNameText;
    private UserEntity ue;
    private TextView settingSingText;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        context = this;
        initViews();
        setListener();
        //initData();
        // loadCombinationData();
    }

    public void initData() {
        UserEngineImpl engine = new UserEngineImpl();
        engine.getSettingMessage(listener);
        listener.setLoadingDialog(context).beforeRequest();
        if (!TextUtils.isEmpty(GlobalParams.MOBILE)) {
            engine.isSetPassword(GlobalParams.MOBILE, new ParseHttpListener<Object>() {

                @Override
                protected Object parseDateTask(String jsonData) {
                    // TODO Auto-generated method stub
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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public void setListener() {
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_setpassword).setOnClickListener(this);
        findViewById(R.id.setting_layout_optiongroup).setOnClickListener(this);
        findViewById(R.id.setting_layout_password).setOnClickListener(this);
        findViewById(R.id.setting_layout_username).setOnClickListener(this);
        findViewById(R.id.setting_layout_icon).setOnClickListener(this);
        findViewById(R.id.feed_back_layout).setOnClickListener(this);
        findViewById(R.id.rl_aboutus).setOnClickListener(this);
        findViewById(R.id.setting_layout_check_version).setOnClickListener(this);
        findViewById(R.id.setting_layout_sign).setOnClickListener(this);
        findViewById(R.id.setting_image_bound).setOnClickListener(this);
        settingSingText = (TextView) findViewById(R.id.setting_sing_text);
    }

    public void initViews() {
        // TODO Auto-generated method stub
        setTitle(R.string.setting);
        settingLayoutGroup = (LinearLayout) findViewById(R.id.setting_layout_group);
        settingImageHead = (ImageView) findViewById(R.id.setting_image_head);
        settingTextAccountText = (TextView) findViewById(R.id.setting_text_account_text);
        settingTextNameText = (TextView) findViewById(R.id.setting_text_name_text);
        String account = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
        //account = setAccount(account);
        settingTextAccountText.setText(account);
        settingTextNameText.setText(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME));
        String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
        if (!TextUtils.isEmpty(url)) {
            // url = DKHSUrl.BASE_DEV_URL + url;
            BitmapUtils bitmapUtils = new BitmapUtils(context);
            bitmapUtils.display(settingImageHead, url);
        } else {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_head);
            b = UIUtils.toRoundBitmap(b);
            settingImageHead.setImageBitmap(b);
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
            case R.id.btn_exit:
                if (isSetPassword) {
                    DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                    try {
                        GlobalParams.ACCESS_TOCKEN = null;
                        GlobalParams.MOBILE = null;
                        dbUtils.deleteAll(UserEntity.class);
                        PortfolioApplication.getInstance().exitApp();
                        intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    } catch (DbException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        PortfolioApplication.getInstance().exitApp();
                        intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(this, SetPasswordActivity.class);
                    // intent.putExtra("type", SetPasswordActivity.LOGOUT_TYPE);
                    // intent.putExtra("is_setpassword", isSetPassword);
                    startActivity(intent);
                }
                break;
            case R.id.btn_setpassword:
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
                startActivity(intent);

                break;
            case R.id.setting_layout_optiongroup:
                intent = new Intent(this, CompareForPublicSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_layout_password:
                intent = new Intent(this, SettingPasswordOnSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_layout_username:
                intent = new Intent(this, UserNameChangeActivity.class);
                startActivityForResult(intent, 6);
                break;
            case R.id.setting_layout_icon:
                intent = new Intent(context, CopyMessageDialog.class);
                startActivityForResult(intent, 5);
                break;
            case R.id.feed_back_layout:
                intent = new Intent(this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_aboutus: {
                intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
            }
            	break;
            case R.id.setting_layout_check_version:
            	UserEngineImpl mUserEngineImpl = new UserEngineImpl();
				mUserEngineImpl.getAppVersion("portfolio_android", userInfoListener);
				userInfoListener.setLoadingDialog(context).beforeRequest();
            	break;
            case R.id.setting_layout_sign:
            	intent = new Intent(this,PersonSignSettingActivity.class);
            	Bundle b = new Bundle();
            	if(null != ue)
            		b.putString(PersonSignSettingActivity.DESCRIPTION,ue.getDescription() );
            	intent.putExtras(b);
            	startActivity(intent);
            	break;
            case R.id.setting_image_bound:
            	
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
					if(!bean.getVersion().equals(version)){
						AlertDialog.Builder alert = new AlertDialog.Builder(context);
						alert.setTitle("软件升级")
								.setMessage("发现新版本,建议立即更新使用.")//"发现新版本,建议立即更新使用."
								.setCancelable(false)
								.setPositiveButton("更新",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												 DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
								            	  
								            	 Uri uri = Uri.parse(bean.getUrl());
								            	 Request request = new Request(uri);
								            	 //设置允许使用的网络类型，这里是移动网络和wifi都可以 
								            	 request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI); 
								            	 //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION 
								            	 request.setShowRunningNotification(true); 
								            	 //不显示下载界面 
								            	 request.setVisibleInDownloadsUi(true);
								            	       
								            	//request.setDestinationInExternalFilesDir(this, null, "tar.apk");
								            	long id = downloadManager.enqueue(request);
								            	PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_ID,id +"");
								            	PromptManager.showToast("开始下载");
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});
						alert.show();
					}else{
						PromptManager.showToast("当前已经是最新版本");
					}
					
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
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
                BitmapUtils bitmapUtils = new BitmapUtils(context);
                bitmapUtils.display(settingImageHead, url);
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
        };

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                UserEntity ue = DataParse.parseObjectJson(UserEntity.class, json);
                return ue;
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
                settingTextNameText.setText(entity.getUsername());
                settingSingText.setText(ue.getDescription());
            }
        }
    };
}
