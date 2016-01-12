package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.common.GlobalParams;
import com.yang.gesturepassword.GesturePasswordManager;
import com.yang.gesturepassword.ISecurityGesture;

/**
 * 暂没用到，如若多个页面需要手势密码超时监听可用
 * Created by zhangcm on 2015/10/26.
 */
public class AssestsBaseActivity extends ModelAcitivity implements ISecurityGesture{

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        PortfolioApplication.getInstance().addAssestsActivity(this);
        GesturePasswordManager.getInstance().startWatch(getApplication());
    }
    private boolean isActivityVisible;

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
        onUserInteraction();
    }

    @Override
    protected void onResume() {
        isActivityVisible = true;
        super.onResume();
        if (GesturePasswordManager.getInstance().isGesturePasswordOpen(mContext, GlobalParams.MOBILE)) {
            if (GlobalParams.needShowGesture) {
                startActivityForResult(GesturePasswordActivity.verifyPasswordIntent(this, true), 100);
            }
            onUserInteraction();
        }
    }

    @Override
    public void onUserInteraction() {
        Log.i("GesturePassword","用户点击了");
        if (!TextUtils.isEmpty(GlobalParams.MOBILE) &&GesturePasswordManager.getInstance().isGesturePasswordOpen(mContext, GlobalParams.MOBILE)) {
            if(isActivityVisible){
                GesturePasswordManager.getInstance().userInteraction();
            }else{
                GesturePasswordManager.getInstance().removeUserInteraction();
                PortfolioApplication.getInstance().onUserInteraction();
            }
        }
    }

    @Override
    protected void onDestroy() {
        PortfolioApplication.getInstance().removeAssestsActivity(this);
        super.onDestroy();
    }
}
