package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.UsersTopicsFrament;

public class UserTopicsActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_topics);
        setTitle(R.string.title_activity_my_topics);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new UsersTopicsFrament()).commitAllowingStateLoss();
    }


}
