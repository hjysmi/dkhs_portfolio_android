package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.FriendsOrFollowersFragment;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FrendOrFollowerActivity
 * @date 2015/4/23.13:18
 * @Description TODO(查看ta 关注的人和关注他的人 复合activity)
 */
public class FriendsOrFollowersActivity extends ModelAcitivity {



    public static final String KEY="type";
    public static final String FRIENDS="friends";
    public static final String FOLLOWER="follower";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_or_followers);

        String    getTypeStr=   getIntent().getStringExtra(FriendsOrFollowersActivity.KEY);

        if(getTypeStr.equals(FriendsOrFollowersActivity.FRIENDS)){
            setTitle(R.string.following);
        }else if(getTypeStr.equals(FriendsOrFollowersActivity.FOLLOWER)){
            setTitle(R.string.followers);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new FriendsOrFollowersFragment()).commitAllowingStateLoss();



    }
}