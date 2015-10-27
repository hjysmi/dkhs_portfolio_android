package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.text.TextUtils;

import com.dkhs.portfolio.common.GlobalParams;
import com.yang.gesturepassword.GesturePasswordManager;
import com.yang.gesturepassword.ISecurityGesture;

/**
 * 暂没用到，如若多个页面需要手势密码超时监听可用
 * Created by zhangcm on 2015/10/26.
 */
public abstract class AssestsBaseLoadMoreListActivity extends LoadMoreListActivity implements ISecurityGesture{

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        GesturePasswordManager.getInstance().startWatch(getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(GlobalParams.needShowGestureImmediately){
//            startActivity(GesturePasswordActivity.verifyPasswordIntent(this, true));
//            GlobalParams.needShowGestureImmediately = false;
//        }
        onUserInteraction();
    }

    @Override
    public void onUserInteraction() {
        if(TextUtils.isEmpty(GlobalParams.MOBILE)){
            GesturePasswordManager.getInstance().userInteraction();
        }
    }
}
