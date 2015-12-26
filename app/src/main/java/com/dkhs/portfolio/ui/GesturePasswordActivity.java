package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.TextView;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.widget.MyAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yang.gesturepassword.GesturePassword;
import com.yang.gesturepassword.GesturePasswordManager;
import com.yang.gesturepassword.LockPatternView;

import java.util.List;

/**
 * Created by zhangcm on 2015/10/21.15:57
 */
public class GesturePasswordActivity extends ModelAcitivity {


    @ViewInject(R.id.tv_tip)
    private TextView tv_tip;

    @ViewInject(R.id.lockPatternView)
    private LockPatternView lockPatternView;

    @ViewInject(R.id.tv_not_set)
    private TextView tv_not_set;
    @ViewInject(R.id.tv_forget)
    private TextView tv_forget;
    @ViewInject(R.id.tv_reset)
    private TextView tv_reset;

    private String gesturePassword;
    private String originPassword;
    private GesturePassword gesPassword;
    private boolean isVerified;

    private static final String IS_FROM_SETTING = "is_from_setting";
    private boolean isFromSetting;
    private static final String IS_AUTO = "is_auto";
    private boolean isAuto;
    private static final String LAYOUT_TYPE = "layout_type";
    private static final int TYPE_OPEN_SETTING = 1;
    private static final int TYPE_CLOSE_SETTING = 2;
    private static final int TYPE_SET_PASSWROD = 3;
    private static final int TYPE_FIRST_SET_PASSWORD = 4;
    private static final int TYPE_VERIFY_PASSWORD = 5;
    private int curLayoutType;
    private boolean needVerifyPassword = true;

    public static Intent openSettingIntent(Context context, boolean isFromSetting) {
        Intent intent = new Intent(context, GesturePasswordActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_OPEN_SETTING);
        intent.putExtra(IS_FROM_SETTING, isFromSetting);
        return intent;
    }

    public static Intent closeSettingIntent(Context context, boolean isFromSetting) {
        Intent intent = new Intent(context, GesturePasswordActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_CLOSE_SETTING);
        intent.putExtra(IS_FROM_SETTING, isFromSetting);
        return intent;
    }

    public static Intent firstSetPasswordIntent(Context context, boolean isFromSetting) {
        Intent intent = new Intent(context, GesturePasswordActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_FIRST_SET_PASSWORD);
        intent.putExtra(IS_FROM_SETTING, isFromSetting);
        return intent;
    }

    public static Intent setPasswordIntent(Context context, boolean isFromSetting) {
        Intent intent = new Intent(context, GesturePasswordActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_SET_PASSWROD);
        intent.putExtra(IS_FROM_SETTING, isFromSetting);
        return intent;
    }

    public static Intent verifyPasswordIntent(Context context, boolean isAuto) {
        Intent intent = new Intent(context, GesturePasswordActivity.class);
        intent.putExtra(LAYOUT_TYPE, TYPE_VERIFY_PASSWORD);
        intent.putExtra(IS_AUTO, isAuto);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_gesture_password);
        ViewUtils.inject(this);
        setTitle(R.string.setting_gesture_password);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initData();
        lockPatternView.setDiameterFactor(0.1f);
        lockPatternView.setTactileFeedbackEnabled(true);
        lockPatternView.setOnPatternListener(setListener);
    }


    private TranslateAnimation animation;

    private void initData() {
        gesPassword = GesturePasswordManager.getInstance().getGesturePassword(mContext, GlobalParams.MOBILE);
        if (gesPassword == null) {
            gesPassword = new GesturePassword();
            gesPassword.mobile = GlobalParams.MOBILE;
        } else {
            if (gesPassword.leftCount == 0 && !isResetMode) {
                showErrorDialog();
            }else if(gesPassword.leftCount == 1){
                gesPassword.leftCount = 0;
            }
        }
        originPassword = gesPassword.password;
        if (curLayoutType == TYPE_OPEN_SETTING || curLayoutType == TYPE_CLOSE_SETTING || curLayoutType == TYPE_VERIFY_PASSWORD) {
            tv_not_set.setVisibility(View.GONE);
            tv_reset.setVisibility(View.GONE);
            tv_forget.setVisibility(View.VISIBLE);
            if (curLayoutType == TYPE_OPEN_SETTING) {
                setTitle(R.string.open_gesture_password);
            } else if (curLayoutType == TYPE_CLOSE_SETTING) {
                setTitle(R.string.close_gesture_password);
            } else {
                setTitle(R.string.verify_gesture_password);
                tv_tip.setText(R.string.pls_input_gesture_password);

            }
        } else if (curLayoutType == TYPE_SET_PASSWROD || curLayoutType == TYPE_FIRST_SET_PASSWORD) {
            if (isFromSetting) {
                tv_not_set.setVisibility(View.GONE);
                tv_reset.setVisibility(View.GONE);
            }
            if (curLayoutType == TYPE_FIRST_SET_PASSWORD) {
                if (isFromSetting) {
                    tv_tip.setText(R.string.pls_input_gesture_password_for_first_time);
                } else {
                    tv_tip.setText(R.string.pls_input_gesture_password_for_security);
                }
                tv_forget.setVisibility(View.GONE);
            } else {
                tv_forget.setVisibility(View.VISIBLE);
            }
            setTitle(R.string.setting_gesture_password);
        }

        animation = new TranslateAnimation(0, -10, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(100);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);

    }

    private void handleExtras(Bundle extras) {
        curLayoutType = extras.getInt(LAYOUT_TYPE);
        isFromSetting = extras.getBoolean(IS_FROM_SETTING);
        isAuto = extras.getBoolean(IS_AUTO);
//        if(isAuto){
//            setSwipeBackEnable(false);
//        }
    }

    @OnClick({R.id.tv_not_set, R.id.tv_forget, R.id.tv_reset})
    private void onClick(View v) {
        //TODO 点击事件
        switch (v.getId()) {
            case R.id.tv_not_set:
                // TODO: 2015/10/22
                PromptManager.showToast("跳过");
                gesPassword.isOpen = false;
                GesturePasswordManager.getInstance().saveGesturePassword(mContext, gesPassword);
                if (!isFromSetting) {
                    startActivity(new Intent(mContext, MyAssestsActivity.class));
                }
                manualFinish();
                break;
            case R.id.tv_forget:
                startActivityForResult(new Intent(mContext, VerifyLoginPasswordActivity.class), 200);
                break;
            case R.id.tv_reset:
                gesturePassword = null;
                tv_tip.setText(R.string.pls_input_new_gesture_password);
                tv_tip.setTextColor(getResources().getColor(R.color.black));
                lockPatternView.clearPattern();
                break;
        }
    }

    private LockPatternView.OnPatternListener setListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {

        }

        @Override
        public void onPatternCleared() {
        }

        @Override
        public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
        }

        @Override
        public void onPatternDetected(List<LockPatternView.Cell> pattern) {
            if (TextUtils.isEmpty(gesturePassword) && pattern.size() < 4 && (isVerified || curLayoutType == TYPE_FIRST_SET_PASSWORD)) {
                lockPatternView.clearPattern();
                tv_tip.setText(R.string.pls_input_gesture_password_4points);
                tv_tip.setTextColor(getResources().getColor(R.color.red));
                tv_tip.startAnimation(animation);
                return;
            }
            String str = LockPatternView.patternToString(pattern);
            if (gesturePassword == null) {
                gesturePassword = str;
                if (curLayoutType == TYPE_FIRST_SET_PASSWORD || !needVerifyPassword) {
                    tv_tip.setText(R.string.pls_input_gesture_password_again);
                    tv_tip.setTextColor(getResources().getColor(R.color.black));
                    lockPatternView.clearPattern();
                } else {
                    gesPassword.password = gesturePassword;
                    if (GesturePasswordManager.getInstance().verifyGesturePassword(mContext, gesPassword)) {
                        //TODO 密码正确
                        gesPassword.leftCount = 4;
                        if (curLayoutType == TYPE_CLOSE_SETTING) {
                            gesPassword.isOpen = false;
                            manualFinish();
                        } else {
                            gesPassword.isOpen = true;
                            if (curLayoutType == TYPE_SET_PASSWROD) {
                                tv_tip.setText(R.string.pls_input_new_gesture_password);
                                tv_tip.setTextColor(getResources().getColor(R.color.black));
                                needVerifyPassword = false;
                                tv_reset.setVisibility(View.VISIBLE);
                                tv_forget.setVisibility(View.GONE);
                            } else if (curLayoutType == TYPE_OPEN_SETTING) {
                                manualFinish();
                            } else if (curLayoutType == TYPE_VERIFY_PASSWORD) {
                                GlobalParams.needShowGesture = false;
                                if (isAuto) {
                                    manualFinish();
                                } else {
                                    startActivity(new Intent(mContext, MyAssestsActivity.class));
                                    finish();
                                }
                            }
                        }
                        lockPatternView.clearPattern();
                        gesPassword.password = originPassword;
                        GesturePasswordManager.getInstance().saveGesturePasswordWithOutEncrypt(mContext, gesPassword);
                    } else {
                        lockPatternView.setInStealthMode(false);
                        lockPatternView
                                .setDisplayMode(LockPatternView.DisplayMode.Wrong);
                        lockPatternView.invalidate();
                        lockPatternView.clearPattern();
                        if (gesPassword.leftCount == 0) {
                            tv_tip.setText(String.format(getResources().getString(R.string.blank_gesture_password_error), gesPassword.leftCount));
                            showErrorDialog();
                        } else {
                            tv_tip.setText(String.format(getResources().getString(R.string.blank_gesture_password_error), gesPassword.leftCount));
                        }
                        if (gesPassword.leftCount > 0) {
                            gesPassword.leftCount = gesPassword.leftCount - 1;
                        }
                        tv_tip.setTextColor(getResources().getColor(R.color.red));
                        tv_tip.startAnimation(animation);
                        gesPassword.password = originPassword;
                    }
                    gesturePassword = null;
                }
                return;
            }
            if (gesturePassword.equals(str)) {
                //TODO 设置成功,保存密码
                gesPassword.password = gesturePassword;
                gesPassword.isOpen = true;
                gesPassword.leftCount = 4;
                GesturePasswordManager.getInstance().saveGesturePassword(mContext, gesPassword);
                PromptManager.showToast("设置成功");
                GlobalParams.needShowGesture = false;
                if (lastLayoutType != 0) {
                    if (lastLayoutType == TYPE_VERIFY_PASSWORD && !isAuto) {
                        startActivity(new Intent(mContext, MyAssestsActivity.class));
                        finish();
                    } else {
                        manualFinish();
                    }
                } else {
                    if (isFromSetting) {
                        manualFinish();
                    } else {
                        startActivity(new Intent(mContext, MyAssestsActivity.class));
                        finish();
                    }
                }
            } else {
                lockPatternView.setInStealthMode(false);
                lockPatternView
                        .setDisplayMode(LockPatternView.DisplayMode.Wrong);
                lockPatternView.invalidate();
                tv_tip.setText(R.string.pls_reinput_gesture_password);
                lockPatternView.clearPattern();
                tv_tip.setTextColor(getResources().getColor(R.color.red));
                tv_tip.startAnimation(animation);
            }
        }

    };


    @Override
    public void onBackPressed() {
        GlobalParams.needShowGesture = false;
        setResult(500);
        if(gesPassword.leftCount == 0){
            gesPassword.leftCount =1;
        }
        if(curLayoutType != TYPE_FIRST_SET_PASSWORD){
            GesturePasswordManager.getInstance().saveGesturePasswordWithOutEncrypt(mContext, gesPassword);
        }
        super.onBackPressed();
    }

    private void showErrorDialog() {
        lockPatternView.clearPattern();
        lockPatternView.invalidate();
        gesPassword.leftCount = 0;
        GesturePasswordManager.getInstance().saveGesturePasswordWithOutEncrypt(mContext, gesPassword);
        new MyAlertDialog(this).builder()
                .setCancelable(false)
                .setMsg(getResources().getString(R.string.gesture_password_msg))
                .setPositiveButton(getResources().getString(R.string.verify), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(mContext, VerifyLoginPasswordActivity.class), 100);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(500);
                        manualFinish();
                    }
                }).show();
    }

    private boolean isResetMode;
    private int lastLayoutType;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 || requestCode == 200) {
            if (resultCode == 500) {
                if (requestCode == 100) {
                    setResult(500);
                    manualFinish();
                }
            } else if (resultCode == 200) {
                //验证成功，重新设手势密码
                lastLayoutType = curLayoutType;
                curLayoutType = TYPE_FIRST_SET_PASSWORD;
                isFromSetting = true;
                isResetMode = true;
                setTitle(R.string.setting_gesture_password);
                tv_reset.setVisibility(View.VISIBLE);
                tv_forget.setVisibility(View.GONE);
                tv_tip.setTextColor(getResources().getColor(R.color.black));
                tv_tip.setText(R.string.pls_input_new_gesture_password);
            }
        }
    }

    @Override
    public void onUserInteraction() {
    }
}
