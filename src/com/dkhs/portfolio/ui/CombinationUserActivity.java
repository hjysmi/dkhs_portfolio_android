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
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.UserCombinationListFragment;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * @ClassName UserCombinationActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-6 下午4:40:09
 * @version 1.0
 */
public class CombinationUserActivity extends ModelAcitivity implements View.OnClickListener {

    private String mUserId;
    private String mUserName;
    private boolean isMyInfo;

    private ImageView ivHeader;
    private TextView tvUName;
    private TextView tvCText;

    private TextView tvUserDesc;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private TextView tvSymbols;
    private Context context;



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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
            if(isMyInfo){
            Button rightBtn= getRightButton();
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

        tvCText = (TextView) findViewById(R.id.tv_combination_text);
        tvUserDesc = (TextView) findViewById(R.id.tv_user_desc);
        tvFollowers = (TextView) findViewById(R.id.tv_followers);
        tvFollowing = (TextView) findViewById(R.id.tv_following);
        tvSymbols = (TextView) findViewById(R.id.tv_symbols);

        if (isMyInfo) {
            setTitle("我的主页");
            tvCText.setText(R.string.text_my_manager_combin);
        } else {
            setTitle("Ta的主页");
            tvCText.setText(R.string.text_other_manager_combin);
        }



        findViewById(R.id.ll_followers).setOnClickListener(this);
        findViewById(R.id.ll_following).setOnClickListener(this);

        replaceCombinationListView();

    }

    private void replaceCombinationListView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_combination_list, UserCombinationListFragment.getFragment(mUserName, mUserId))
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

    protected void updateUserView(UserEntity object) {
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_user_head);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_user_head);
        if (null != object.getAvatar_md() && object.getAvatar_md().length() > 35) {
            bitmapUtils.display(ivHeader, object.getAvatar_md());
        }
        tvUName.setText(object.getUsername());
        handleNumber(tvFollowers,object.getFollowed_by_count());
        handleNumber(tvFollowing,object.getFriends_count());
        handleNumber(tvSymbols,object.getSymbols_count());


//        tvUCreateTime.setText(TimeUtils.getSimpleDay(object.getDate_joined()) + "   进驻谁牛");

        if(TextUtils.isEmpty(object.getDescription())){
            tvUserDesc.setText(getString(R.string.format_sign_text_title, getResources().getString(R.string.nodata_user_description)));
        }else {

            tvUserDesc.setText(getString(R.string.format_sign_text_title, object.getDescription()));
        }

    }

    private void handleNumber(TextView tv, int count) {

        String countStr;

        if(count >1000){
            DecimalFormat df2 = new DecimalFormat("#.#");
            countStr  =df2.format(count/1000.0)+"k";
        }else{
            countStr=count+"";
        }
        tv.setText(countStr);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case  R.id.ll_followers:
                Intent intent1=new Intent(this,FriendsOrFollowersActivity.class);
                intent1.putExtra(FriendsOrFollowersActivity.KEY,FriendsOrFollowersActivity.FOLLOWER);
                startActivity(intent1);
                break;
            case  R.id.ll_following:
                Intent intent=new Intent(this,FriendsOrFollowersActivity.class);
                intent.putExtra(FriendsOrFollowersActivity.KEY,FriendsOrFollowersActivity.FRIENDS);
                startActivity(intent);

                break;
            default:
                break;
        }
    }
}
