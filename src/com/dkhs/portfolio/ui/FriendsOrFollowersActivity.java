package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

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
    public static final String USER_ID="user_id";
    public static final String FRIENDS="friends";
    public static final String FOLLOWER="follower";


    private Button btnRefresh;
    private FriendsOrFollowersFragment listFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_or_followers);

        String    getTypeStr=   getIntent().getStringExtra(FriendsOrFollowersActivity.KEY);




        btnRefresh = getSecondRightButton();
        btnRefresh.setVisibility(View.VISIBLE);
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh_selector),
                null, null, null);

        if(getTypeStr.equals(FriendsOrFollowersActivity.FRIENDS)){
            setTitle(R.string.following);
        }else if(getTypeStr.equals(FriendsOrFollowersActivity.FOLLOWER)){
            setTitle(R.string.followers);
        }
        listFragment=new FriendsOrFollowersFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new FriendsOrFollowersFragment()).commitAllowingStateLoss();



    }

    public void rotateRefreshButton() {
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing), null,
                null, null);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        btnRefresh.startAnimation(animation);
    }

    public void stopRefreshAnimation() {

        btnRefresh.clearAnimation();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh), null,
                null, null);
        btnRefresh.setVisibility(View.GONE);
    }


}