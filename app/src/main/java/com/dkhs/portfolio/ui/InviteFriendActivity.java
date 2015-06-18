package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.R;

/**
 * Created by zjz on 2015/6/18.
 */
public class InviteFriendActivity extends ModelAcitivity {


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, InviteFriendActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_invite_friend);
        setContentView(R.layout.activity_invite_friend);
    }
}
