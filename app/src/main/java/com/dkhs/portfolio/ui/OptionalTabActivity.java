package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dkhs.portfolio.ui.fragment.MainOptionalFragment;

/**
 * Created by zjz on 2015/5/8.
 */
public class OptionalTabActivity extends ModelAcitivity {
    public static final String EXTRA_USER_ID = "extra_user_id";

    private String mUserId;

    public static Intent newIntent(Context context, String userId) {
        Intent intent = new Intent(context, OptionalTabActivity.class);

        intent.putExtra(EXTRA_USER_ID, userId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hideHead();
        hadFragment();

        replaceContentFragment(MainOptionalFragment.getMainFragment(this.mUserId));

    }

    @Override
    public void handleBundleExtras(Bundle extras) {
        this.mUserId = extras.getString(EXTRA_USER_ID);
    }
}
