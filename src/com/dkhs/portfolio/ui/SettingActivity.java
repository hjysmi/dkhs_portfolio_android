package com.dkhs.portfolio.ui;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.google.gson.reflect.TypeToken;
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
        initData();
        // loadCombinationData();
    }

    public void initData() {
        UserEngineImpl engine = new UserEngineImpl();
        engine.getSettingMessage(listener);
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
    }

    public void initViews() {
        // TODO Auto-generated method stub
        setTitle(R.string.setting);
        settingLayoutGroup = (LinearLayout) findViewById(R.id.setting_layout_group);
        settingImageHead = (ImageView) findViewById(R.id.setting_image_head);
        settingTextAccountText = (TextView) findViewById(R.id.setting_text_account_text);
        settingTextNameText = (TextView) findViewById(R.id.setting_text_name_text);
        String account = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
        account = setAccount(account);
        settingTextAccountText.setText(account);
        settingTextNameText.setText(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME));
        String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
        if (!TextUtils.isEmpty(url)) {
            url = DKHSUrl.BASE_DEV_URL + url;
            BitmapUtils bitmapUtils = new BitmapUtils(context);
            bitmapUtils.display(settingImageHead, url);
        }else{
	        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.ic_user_head);
	        b = UIUtils.toRoundBitmap(b);
	        settingImageHead.setImageBitmap(b);
        }
    }
    public String setAccount(String account){
    	if(account.contains("@")){
    		int k = account.indexOf("@");
    		account = account.substring(0, k-3) + "***" + account.substring(k,account.length());
    	}else{
    		account = account.substring(0, account.length() - 5) + "***" + account.substring(account.length() - 2,account.length());
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
            	startActivityForResult(intent,6);
                break;
            case R.id.setting_layout_icon:
            	intent = new Intent(context,CopyMessageDialog.class);
    	    	startActivityForResult(intent,5);
            	break;
            default:
                break;
        }
    }

    /**
     * 添加组合股是否公开列表数据
     * 
     * @param lsit
     */
    /*
     * public void createGroupShow(List<CombinationBean> lsit){
     * int i = 0;
     * if(settingCheckbox.isChecked()){
     * settingLayoutGroup.setClickable(false);
     * }
     * for (CombinationBean combinationBean : lsit) {
     * LayoutInflater l = LayoutInflater.from(context);
     * View view = l.inflate(R.layout.setting_group_item, null);
     * Switch s = (Switch) view.findViewById(R.id.switch1);
     * s.setText(combinationBean.getName());
     * if(settingCheckbox.isChecked()){
     * s.setChecked(false);
     * }
     * settingLayoutGroup.addView(view);
     * }
     * }
     */
    private void loadCombinationData() {
        new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<List<CombinationBean>>() {

            @Override
            protected List<CombinationBean> parseDateTask(String jsonData) {
                Type listType = new TypeToken<List<CombinationBean>>() {
                }.getType();
                List<CombinationBean> combinationList = DataParse.parseJsonList(jsonData, listType);

                return combinationList;
            }

            @Override
            protected void afterParseData(List<CombinationBean> dataList) {
                // createGroupShow(dataList);
            }

        }.setLoadingDialog(SettingActivity.this, R.string.loading));
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			settingTextNameText.setText(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME));
			String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
            if (!TextUtils.isEmpty(url)) {
                url = DKHSUrl.BASE_DEV_URL + url;
                BitmapUtils bitmapUtils = new BitmapUtils(context);
                bitmapUtils.display(settingImageHead, url);
            }
		}
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
            	settingTextNameText.setText(entity.getUsername());
            }
        }
    };
}
