package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.UsersTopicsFragment;
import com.dkhs.portfolio.utils.UIUtils;

public class UserTopicsActivity extends ModelAcitivity {

    public static final String USER_ID = "User_id";
    public static final String USER_NAME = "user_Name";
    public static final String CONTENT_TYPE = "user_Name";


    public static void starActivity(Context context, String userId, String userName) {
        Intent intent = new Intent(context, UserTopicsActivity.class);
        intent.putExtra(USER_ID, userId);
        intent.putExtra(USER_NAME, userName);

        UIUtils.startAnimationActivity((Activity) context, intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_topics);
        setTitle(R.string.title_activity_my_topics);
        String userId = getIntent().getStringExtra(UserTopicsActivity.USER_NAME);
        String userName = getIntent().getStringExtra(UserTopicsActivity.USER_ID);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, UsersTopicsFragment.newIntent(userId, userName,TopicsDetailActivity.TYPE_TOPIC)).commitAllowingStateLoss();
    }


}
