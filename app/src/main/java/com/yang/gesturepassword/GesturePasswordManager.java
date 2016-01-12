package com.yang.gesturepassword;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.GesturePasswordActivity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * 使用密码控件时，在app初始化时调用 GesturePasswordManager.getInstance().startWatch(getApplication());即可实现自动锁屏
 */
public class GesturePasswordManager {

    public static final String GESTURE_PASSWORD_LOCAL_KEY = "gesture_password";

    public static final String TOKEN_LOCAL_KEY = "token_id";

    public static final String HEADER = "SAYDUIBQJMI";

    public static int PASSWORD_LENGTH = 64;

    static GesturePasswordManager instance;

    /**
     * 无操作后锁屏时间
     */
    private final static int SHOW_GESTURE_DELAY = GlobalParams.SHOW_GESTURE_DELAY;

    private final static int SHOW_GESTURE = 1;

    private boolean showGestureImmediately = false;

    private Handler mHandler;

    private Activity mCurActivity;

    private class MyHandler extends Handler {
        public MyHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_GESTURE:
                    showGesturePasswordActivity();
                    break;
            }
        }
    }

    private GesturePasswordManager() {
    }

    private static DbUtils dbUtils;

    public static GesturePasswordManager getInstance() {
        if (instance == null) {
            synchronized (GesturePasswordManager.class) {
                if (instance == null) {
                    instance = new GesturePasswordManager();
                    dbUtils = AppConfig.getDBUtils();
                }
            }
        }
        return instance;
    }

    public void startWatch(Application app) {
        mHandler = new MyHandler(app.getMainLooper());
        showGestureImmediately = true;
        app.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity,
                                                    Bundle outState) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                mCurActivity = activity;
                if (activity instanceof ISecurityGesture) {
                    if (!showGestureImmediately) {
                        userInteraction();
                    }
                } else {
                    mHandler.removeMessages(SHOW_GESTURE);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {
            }
        });
    }

    public void setGesturePassword() {

    }

    public void verifyPassword() {

    }

    public void userInteraction() {
        mHandler.removeMessages(SHOW_GESTURE);
        mHandler.sendEmptyMessageDelayed(SHOW_GESTURE, SHOW_GESTURE_DELAY);
    }
    public void removeUserInteraction() {
        mHandler.removeMessages(SHOW_GESTURE);
    }

    private void showGesturePasswordActivity() {
        showGestureImmediately = true;
        mCurActivity.startActivityForResult(GesturePasswordActivity.verifyPasswordIntent(mCurActivity, true),100);
    }

    protected void setShowGestureImmediately(boolean showGestureImmediately) {
        this.showGestureImmediately = showGestureImmediately;
    }


    public void saveGesturePassword(Context context, GesturePassword gesPassword) {
//		dbUtils.findFirst(WhereBuilder.b("mobile","=",gesPassword.mobile));
        String password = gesPassword.password;
        String imei = Util.getDeviceIMEI(context);
        if(!TextUtils.isEmpty(gesPassword.password)){
            StringBuilder encryptPassword = new StringBuilder(HEADER);
            encryptPassword.append(password);
            // 加入随机字串，让每次的加密结果都不一样
            if (encryptPassword.length() < PASSWORD_LENGTH) {
                String randomString = Util.getRandomString(PASSWORD_LENGTH
                        - encryptPassword.length());
                encryptPassword.append(randomString);
                String result = null;
                try {
                    // 密钥需要加入设备的硬件信息，防止重放
                    result = AESUtil.encrypt(imei + HEADER + password,
                            encryptPassword.toString());
                } catch (Exception e) {
                    // AES加密失败，就取md5值
                    result = Md5Generator.generate(imei + HEADER + password);
                }
                // 在sp中保存的key使用相应的md5值，以免轻易暴露此key保存内容的作用
                String saveKey = Md5Generator.generate(GESTURE_PASSWORD_LOCAL_KEY);
                if (saveKey.length() > 64) {
                    saveKey = saveKey.substring(0, 64);
                }
                gesPassword.password = result;
            }
        }


        try {
            GesturePassword originPsw = dbUtils.findFirst(Selector.from(GesturePassword.class).where("mobile", "=", gesPassword.mobile));
            if (originPsw != null) {
                gesPassword.id = originPsw.id;
            }
            dbUtils.saveOrUpdate(gesPassword);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public GesturePassword getGesturePassword(Context mContext, String mobile) {
        GesturePassword originPsw = null;
        try {
            originPsw = dbUtils.findFirst(Selector.from(GesturePassword.class).where("mobile", "=", mobile));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return originPsw;

    }

    public boolean verifyGesturePassword(Context context, GesturePassword gesPassword) {
        String password = gesPassword.password;
        String saveKey = Md5Generator.generate(GESTURE_PASSWORD_LOCAL_KEY);
        if (saveKey.length() > 64) {
            saveKey = saveKey.substring(0, 64);
        }
        GesturePassword originPsw = null;
        try {
            originPsw = dbUtils.findFirst(Selector.from(GesturePassword.class).where("mobile", "=", gesPassword.mobile));
            String encryptPassword = originPsw.password;
            if (TextUtils.isEmpty(encryptPassword)) {
                // 未保存手势密码直接返回false
                return false;
            }

            String imei = Util.getDeviceIMEI(context);

            String passwordMd5 = Md5Generator.generate(imei + HEADER + password);
            // 如果手势密码的MD5与保存的结果一样，说明设置的时候AES加密失败了，保存的是手势密码的MD5值，两个MD5一样，返回true.
            if (passwordMd5.equals(encryptPassword)) {
                return true;
            }

            String result = null;
            try {
                result = AESUtil.decrypt(imei + HEADER + password, encryptPassword);
            } catch (Exception e) {
            }

            // 未解密出结果，返回false
            if (TextUtils.isEmpty(result)) {
                return false;
            }

            // 如果解密正确，解密结果应该是由 HEADER + gesture password + random组成.
            if (result.startsWith(HEADER + password)) {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }


        return false;
    }

    public void saveGesturePasswordWithOutEncrypt(Context mContext, GesturePassword gesPassword) {
        try {
            GesturePassword originPsw = dbUtils.findFirst(Selector.from(GesturePassword.class).where("mobile", "=", gesPassword.mobile));
            if (originPsw != null) {
                gesPassword.id = originPsw.id;
            }
            Log.i("GesturePassword","saveGesturePasswordWithOutEncrypt手势密码状态" + (gesPassword.isOpen?":已打开":"已关闭"));
            dbUtils.saveOrUpdate(gesPassword);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public boolean needSetGesturePassword(Context mContext, String mobile) {
        try {
            GesturePassword originPsw = dbUtils.findFirst(Selector.from(GesturePassword.class).where("mobile", "=", mobile));
            return originPsw == null;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isGesturePasswordOpen(Context mContext, String mobile) {
        try {
            GesturePassword originPsw = dbUtils.findFirst(Selector.from(GesturePassword.class).where("mobile", "=", mobile));
            if (originPsw != null){
                Log.i("GesturePassword","isGesturePasswordOpen手势密码状态" + (originPsw.isOpen?":已打开":"已关闭"));
                return originPsw.isOpen;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
}
