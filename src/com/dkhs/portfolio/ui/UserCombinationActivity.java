/**
 * @Title UserCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-6 下午4:40:09
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @ClassName UserCombinationActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-6 下午4:40:09
 * @version 1.0
 */
public class UserCombinationActivity extends ModelAcitivity {

    private String mUserId;
    private boolean isMyInfo;

    public static Intent getIntent(Context context, String userId, boolean isMyInfo) {
        Intent intent = new Intent(context, UserCombinationActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("is_my_info", isMyInfo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_user_combination);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        initViews();
        initData();

    }

    private void handleExtras(Bundle extras) {
        mUserId = extras.getString("user_id");
        isMyInfo = extras.getBoolean("is_my_info");
    }

    private void initViews() {
        if (isMyInfo) {
            setTitle("我的资料");
        } else {
            setTitle("基金经理资料");
        }

    }

    private void initData() {
        // TODO Auto-generated method stub

    }

}
