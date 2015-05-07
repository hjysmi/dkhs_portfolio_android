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
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.UserCombinationListFragment;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.lidroid.xutils.BitmapUtils;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * @author zjz
 * @version 1.0
 * @ClassName UserCombinationActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-11-6 下午4:40:09
 */
public class CombinationUserActivity extends ModelAcitivity implements View.OnClickListener, UserCombinationListFragment.OnFragmentInteractionListener {

    private String mUserId;
    private String mUserName;
    private boolean isMyInfo;

    private ImageView ivHeader;
    private TextView tvUName;

    private TextView tvUserDesc;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private TextView tvSymbols;
    private Context context;


    private UserEntity userEntity;

    private View llTool;
    private View llFollow;
    private View bgV;
    private View combinationTitleLL;
    private int headerTop;
    private int headerLeft;
    private int userNameTop;
    private int userDescTop;
    private int userNameLeft;
    private int userDescLeft;
    private TextView followTV;
    private UserCombinationListFragment userCombinationListFragment;

    private float prePercent;

    public static Intent getIntent(Context context, String username, String userId, boolean isMyInfo) {
        Intent intent = new Intent(context, CombinationUserActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("username", username);
        intent.putExtra("is_my_info", isMyInfo);
        return intent;
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user_combination);
        context = this;
        getTitleView().setBackgroundColor(getResources().getColor(R.color.user_combination_head_bg));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
            if (isMyInfo) {
                Button rightBtn = getRightButton();
                rightBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_edit_selector), null,
                        null, null);
                rightBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startSettingActivity();
                    }
                });
            }
        }

        initViews();
        initData();

    }

    private void startSettingActivity() {
        startActivity(SettingActivity.getEditUserInfoIntent(this));
        // UIUtils.startAminationActivity(getActivity(), intent);
    }


    private void handleExtras(Bundle extras) {
        mUserId = extras.getString("user_id");
        mUserName = extras.getString("username");
        isMyInfo = extras.getBoolean("is_my_info");

    }


    private void initViews() {

        ivHeader = (ImageView) findViewById(R.id.iv_uheader);
        tvUName = (TextView) findViewById(R.id.tv_user_name);
        tvUserDesc = (TextView) findViewById(R.id.tv_user_desc);
        followTV = (TextView) findViewById(R.id.tv_follow);
        tvFollowers = (TextView) findViewById(R.id.tv_followers);
        llFollow = findViewById(R.id.ll_follow);
        tvFollowing = (TextView) findViewById(R.id.tv_following);
        tvSymbols = (TextView) findViewById(R.id.tv_symbols);
        llTool = findViewById(R.id.ll_tool);
        bgV = findViewById(R.id.v_bg);
        combinationTitleLL = findViewById(R.id.ll_combination_title);

        if (isMyInfo) {
            setTitle("我的主页");
            llFollow.setVisibility(View.GONE);
        } else {
            setTitle("Ta的主页");
            llFollow.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.ll_followers).setOnClickListener(this);
        findViewById(R.id.ll_following).setOnClickListener(this);
        llFollow.setOnClickListener(this);

        replaceCombinationListView();
    }

    private void replaceCombinationListView() {

        userCombinationListFragment = UserCombinationListFragment.getFragment(mUserName, mUserId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_combination_list, userCombinationListFragment)
                .commit();

    }

    private void initData() {

        userInfoListener.setLoadingDialog(context);
        new UserEngineImpl().getBaseUserInfo(mUserId, userInfoListener);
    }

    ParseHttpListener userInfoListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {

            return DataParse.parseObjectJson(UserEntity.class, jsonData);
        }

        @Override
        protected void afterParseData(UserEntity object) {
            if (null != object) {
                updateUserView(object);
            }

        }
    };


    ParseHttpListener unfollowListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {

            return DataParse.parseObjectJson(UserEntity.class, jsonData);

        }

        @Override
        protected void afterParseData(UserEntity object) {
            if (null != object) {

                updateUserFolllowInfo(object);
            }
        }
    };
    ParseHttpListener followListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {

            try {
                JSONArray json = new JSONArray(jsonData);

                return DataParse.parseObjectJson(UserEntity.class, json.get(0).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void afterParseData(UserEntity object) {
            if (null != object) {


                //todo api 返回无me_follow 字段,所以这边手动设置为true
                object.setMe_follow(true);
                updateUserFolllowInfo(object);

                UserEngineImpl.getUserEntity();
            }

        }
    };


    protected void updateUserView(UserEntity object) {

        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_user_head);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_user_head);
        if (null != object.getAvatar_md() && object.getAvatar_md().length() > 35) {
            bitmapUtils.display(ivHeader, object.getAvatar_md());
        }
        tvUName.setText(object.getUsername());
        if (TextUtils.isEmpty(object.getDescription())) {
            tvUserDesc.setText(getResources().getString(R.string.nodata_user_description));
        } else {

            tvUserDesc.setText(object.getDescription());
        }

        updateUserFolllowInfo(object);
        tvUserDesc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvUserDesc.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                userDescLeft = tvUserDesc.getLeft();
            }
        });

        tvUName.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvUName.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                userNameLeft = tvUName.getLeft();
            }
        });

    }

    private void updateUserFolllowInfo(UserEntity object) {

        userEntity = object;

        if (object.isMe_follow()) {
            followTV.setText(" " + getResources().getString(R.string.unfollowing));
            followTV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_del_item_selector), null, null, null);
        } else {
            followTV.setText(" " + getResources().getString(R.string.following));
            followTV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_add), null, null, null);
        }

        handleNumber(tvFollowers, object.getFollowed_by_count());
        handleNumber(tvFollowing, object.getFriends_count());
        handleNumber(tvSymbols, object.getSymbols_count());
    }

    private void handleNumber(TextView tv, int count) {

        tv.setText(StringFromatUtils.handleNumber(count));
    }

    @Override
    public void onClick(View v) {


        if (!PortfolioApplication.hasUserLogin()) {
            gotoLoginDialog();
            return;
        }

        switch (v.getId()) {
            case R.id.ll_followers:

                if (null == userEntity) {
                    return;
                }
                Intent intent1 = new Intent(this, FriendsOrFollowersActivity.class);
                intent1.putExtra(FriendsOrFollowersActivity.KEY, FriendsOrFollowersActivity.FOLLOWER);
                intent1.putExtra(FriendsOrFollowersActivity.USER_ID, userEntity.getId() + "");
                startActivity(intent1);
                break;
            case R.id.ll_following:
                if (null == userEntity) {
                    return;
                }

                Intent intent = new Intent(this, FriendsOrFollowersActivity.class);
                intent.putExtra(FriendsOrFollowersActivity.KEY, FriendsOrFollowersActivity.FRIENDS);
                intent.putExtra(FriendsOrFollowersActivity.USER_ID, userEntity.getId() + "");
                startActivity(intent);
                break;
            case R.id.ll_follow:
                if (null == userEntity) {
                    return;
                }

                if (userEntity.isMe_follow()) {
                    unFollowAction();
                } else {
                    followAction();

                }


                break;
            default:
                break;
        }
    }

    private void gotoLoginDialog() {

        PromptManager.getAlertDialog(this).setTitle(R.string.tips).setMessage(R.string.nodata_login_out).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(CombinationUserActivity.this, LoginActivity.class));
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, null).create().show();


    }

    private void unFollowAction() {


        PromptManager.getAlertDialog(this).setTitle(R.string.tips).setMessage(String.format(getResources().getString(R.string.unfollow_alert_content), userEntity.getUsername()))
                   .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           unfollowListener.setLoadingDialog(context);
                           new UserEngineImpl().unfollow(userEntity.getId() + "", unfollowListener);
                           dialog.dismiss();
                       }
                   }).setNegativeButton(R.string.cancel, null).create().show();


    }

    private void followAction() {
        followListener.setLoadingDialog(context);
        new UserEngineImpl().follow(userEntity.getId() + "", followListener);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            headerTop = ivHeader.getTop();
            headerLeft = ivHeader.getLeft();
            userNameTop = tvUName.getTop();
            userNameLeft = tvUName.getLeft();
            userDescTop = tvUserDesc.getTop();
            userDescLeft = tvUserDesc.getLeft();
        }
    }



    private float toPercent;
    /**
     * 动画效果
     * @param
     */
    public void onScrollChanged(float percent) {

        if (!isMyInfo) {
            if (percent == 0 ) {
                translationShow();
            } else {
                translationDismiss();
            }
        }


        if(Math.abs(percent-prePercent) >0.12){
            toPercent=percent;
            if(!isSendState) {
                if(prePercent>toPercent) {
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(0);
                }
            }

        }else {
            animHeader(percent);
        }



    }



    private boolean isSendState=false;
    /**
     *处理快速滑动时候的动画卡顿
     */
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isSendState=true;
            switch (msg.what) {

                case 0:

                    float percent2=prePercent+0.06f;
                    if(percent2 > toPercent){
                        percent2=toPercent;
                    }

                    animHeader(percent2);
                    if(percent2 != toPercent){
                        handler.sendEmptyMessage(0);
                    }else{
                        isSendState=false;
                    }

                    break;
                case 1:
                    float percent=prePercent-0.06f;

                    if(percent < toPercent){
                        percent=toPercent;
                    }
                    animHeader(percent);
                    if(percent != toPercent){
                        handler.sendEmptyMessage(1);
                    }else{
                        isSendState=false;
                    }

                    break;
            }
        }
    };

    public void animHeader(float percent) {

        ViewHelper.setTranslationX(ivHeader, -(headerLeft - getResources().getDimensionPixelOffset(R.dimen.header_avatar_margin)) * percent);
        ViewHelper.setTranslationY(ivHeader, -(headerTop - getResources().getDimensionPixelOffset(R.dimen.header_avatar_margin) )* percent);

        ViewHelper.setTranslationX(tvUserDesc, -(userDescLeft - getResources().getDimensionPixelOffset(R.dimen.header_avatar_height)-getResources().getDimensionPixelOffset(R.dimen.header_avatar_margin)*2) * percent);
        ViewHelper.setTranslationY(tvUserDesc, -(userDescTop - getResources().getDimensionPixelOffset(R.dimen.header_avatar_height)-getResources().getDimensionPixelOffset(R.dimen.header_avatar_margin)+getResources().getDimensionPixelOffset(R.dimen.header_userDesc_height)) * percent);
        ViewHelper.setTranslationX(tvUName, -(userNameLeft - getResources().getDimensionPixelOffset(R.dimen.header_avatar_height)-getResources().getDimensionPixelOffset(R.dimen.header_avatar_margin)*2) * percent);
        ViewHelper.setTranslationY(tvUName, -(userNameTop - getResources().getDimensionPixelOffset(R.dimen.header_avatar_margin_top)) * percent);
        ViewHelper.setTranslationY(combinationTitleLL, -getResources().getDimensionPixelOffset(R.dimen.header_can_scroll_distance) * percent);
        ViewHelper.setTranslationY(llTool, -getResources().getDimensionPixelOffset(R.dimen.header_can_scroll_distance)  * percent);
//
        ViewHelper.setAlpha(llTool, 1 - percent);

        if(1==percent){
            if(llTool.getVisibility()== View.VISIBLE){
                llTool.setVisibility(View.GONE);
            }
        }else{
            if(llTool.getVisibility()== View.GONE){
                llTool.setVisibility(View.VISIBLE);
            }
        }
        ViewHelper.setTranslationY(bgV, -getResources().getDimensionPixelOffset(R.dimen.header_can_scroll_distance)  * percent);
        prePercent = percent;
    }


    private boolean followLLShow=true;
    private void translationDismiss() {

        if(followLLShow) {
            AnimationHelper.translationDismiss(llFollow);
        }
        followLLShow=false;
    }

    private void translationShow() {

        if(!followLLShow) {
            AnimationHelper.translationShow(llFollow);
        }
        followLLShow=true;
    }


}
