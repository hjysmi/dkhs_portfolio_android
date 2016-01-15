package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.TabTopicFragment;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zjz on 2015/9/24.
 */
public class MyTopicActivity extends ModelAcitivity {

    public static final String USER_ID = "User_id";
    public static final String USER_NAME = "user_Name";


    public static void starActivity(Context context, String userId, String userName) {
        Intent intent = new Intent(context, MyTopicActivity.class);
        intent.putExtra(USER_ID, userId);
        intent.putExtra(USER_NAME, userName);

        UIUtils.startAnimationActivity((Activity) context, intent);


    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.info_title_mypost);
        replaceContentFragment(new TabTopicFragment());
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_mytopic;
    }
}
