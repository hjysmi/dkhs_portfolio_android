/**
 * @Title UserCombinationActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-6 下午4:40:09
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UnFollowEvent;
import com.dkhs.portfolio.ui.fragment.UserCombinationListFragment;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.nineoldandroids.view.ViewHelper;

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

    private final int MENU_FOLLOW_OR_UNFOLLOWE = 0;
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

    private View bgV;
    private View combinationTitleLL;
    private int headerTop;
    private int headerLeft;
    private int userNameTop;
    private int userDescTop;
    private int userNameLeft;
    private int userDescLeft;

    private TextView symbolsPromptTV;

    private UserCombinationListFragment userCombinationListFragment;
    public FloatingActionMenu localFloatingActionMenu;

    private float prePercent;
    private UserEngineImpl userEngine;


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
        userEngine= new UserEngineImpl();
        if (extras != null) {
            handleExtras(extras);


            if(null != UserEngineImpl.getUserEntity() &&(UserEngineImpl.getUserEntity().getId()+"").equals(mUserId)){
                isMyInfo=true;
            }

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

        tvFollowers = (TextView) findViewById(R.id.tv_followers);
        symbolsPromptTV = (TextView) findViewById(R.id.tv_symbols_prompt);

        tvFollowing = (TextView) findViewById(R.id.tv_following);
        tvSymbols = (TextView) findViewById(R.id.tv_symbols);
        llTool = findViewById(R.id.ll_tool);
        bgV = findViewById(R.id.v_bg);
        combinationTitleLL = findViewById(R.id.ll_combination_title);
        localFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_view);
        if (isMyInfo) {
            setTitle("我的主页");
            localFloatingActionMenu.setVisibility(View.GONE);

        } else {
            setTitle("Ta的主页");
            localFloatingActionMenu.setVisibility(View.VISIBLE);

        }

        findViewById(R.id.ll_followers).setOnClickListener(this);
        findViewById(R.id.ll_following).setOnClickListener(this);
        findViewById(R.id.ll_symbols).setOnClickListener(this);
        localFloatingActionMenu.setOnMenuItemSelectedListener(new FloatingActionMenu.OnMenuItemSelectedListener() {
            @Override
            public boolean onMenuItemSelected(int paramInt) {

                switch (paramInt) {
                    case MENU_FOLLOW_OR_UNFOLLOWE:

                        if (null == userEntity) {
                            return false ;
                        }
                        if (userEntity.isMe_follow()) {
                            unFollowAction();
                        } else {
                            followAction();
                        }
                        break;


                }
                return false;
            }
        });

        replaceCombinationListView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isMyInfo) {
            userInfoListener.setLoadingDialog(context);


            userEngine.getBaseUserInfo(mUserId, userInfoListener);
        }


    }

    private void replaceCombinationListView() {

        userCombinationListFragment = UserCombinationListFragment.getFragment(mUserName, mUserId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_combination_list, userCombinationListFragment)
                .commit();

    }

    private void initData() {



        if(!isMyInfo) {
            userInfoListener.setLoadingDialog(context);
            userEngine.getBaseUserInfo(mUserId, userInfoListener);
        }
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

                PromptManager.showDelFollowToast();

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
                PromptManager.showFollowToast();

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
        localFloatingActionMenu.removeAllItems();
        if (object.isMe_follow()) {

            localFloatingActionMenu.addItem(0,R.string.unfollowing,R.drawable.btn_del_item_selector);

        } else {
            localFloatingActionMenu.addItem(0,R.string.following,R.drawable.ic_add);

        }

        handleNumber(tvFollowers, object.getFollowed_by_count());
        handleNumber(tvFollowing, object.getFriends_count());
        handleNumber(tvSymbols, object.getSymbols_count()+object.getPortfolios_following_count());
    }

    private void handleNumber(TextView tv, int count) {

        tv.setText(StringFromatUtils.handleNumber(count));
    }

    @Override
    public void onClick(View v) {



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


            case R.id.ll_symbols:{
                    startActivity(OptionalTabActivity.newIntent(this,mUserId));
            }
            break;
            default:
                break;
        }
    }


    private void unFollowAction() {


        PromptManager.getAlertDialog(this).setTitle(R.string.tips).setMessage(getResources().getString(R.string.unfollow_alert_content))
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


        if (!UIUtils.iStartLoginActivity(this)) {
            followListener.setLoadingDialog(context);
            new UserEngineImpl().follow(userEntity.getId() + "", followListener);
        }
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
            animHeader(percent);
    }


    @Override
    public void finish() {


        if(null!=userEntity&&!userEntity.isMe_follow()){
            UnFollowEvent unFollowEvent=new UnFollowEvent();
            unFollowEvent.setId(userEntity.getId());
            BusProvider.getInstance().post(unFollowEvent);
        }

        super.finish();

    }

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




}
