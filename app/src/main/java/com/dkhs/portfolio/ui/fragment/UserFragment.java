/**
 * @Title UserFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 下午1:01:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationUserActivity;
import com.dkhs.portfolio.ui.FriendsOrFollowersActivity;
import com.dkhs.portfolio.ui.InviteFriendsActivity;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.SettingActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * @author zjz
 * @version 1.0
 * @ClassName UserFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-5 下午1:01:32
 */
public class UserFragment extends BaseTitleFragment implements OnClickListener {

    @ViewInject(R.id.ll_login_layout)
    private View viewLogin;
    @ViewInject(R.id.ll_userinfo_layout)
    private View viewUserInfo;
    @ViewInject(R.id.setting_image_head)
    private ImageView settingImageHead;
    @ViewInject(R.id.setting_text_account_text)
    private TextView settingTextAccountText;
    @ViewInject(R.id.setting_text_name_text)
    private TextView settingTextNameText;
    @ViewInject(R.id.tv_unread_count)
    private TextView unreadCountTV;

    @ViewInject(R.id.tv_followers)
    private TextView tvFollowers;

    @ViewInject(R.id.tv_following)
    private TextView tvFollowing;
    private UserEngineImpl userImp = new UserEngineImpl();

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        toolBar.setClickable(true);
        initView(view);
        setTitle(R.string.title_user);
        updateUserInfo();
    }

    @Override
    public void requestData() {
        updateMessageCenterState();
    }





    private void initView(View view) {

        TextView addButton = getRightButton();
        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_setting_selecter),
                null, null, null);
        addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startSettingActivity();

            }
        });
        BusProvider.getInstance().register(this);
        updateUserInfo();
    }

    private void updateUserInfo() {

        if (PortfolioApplication.hasUserLogin()) {
            viewLogin.setVisibility(View.GONE);
            viewUserInfo.setVisibility(View.VISIBLE);
            String account = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                settingTextAccountText.setText(account);
            }
            settingTextNameText.setText(PortfolioPreferenceManager
                    .getStringValue(PortfolioPreferenceManager.KEY_USERNAME));

            String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
            if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
                bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_user_head);
                bitmapUtils.display(settingImageHead, url);

            } else {
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_head);
                b = UIUtils.toRoundBitmap(b);
                settingImageHead.setImageBitmap(b);

            }

            UserEntity userEntity = UserEngineImpl.getUserEntity();

            userImp.getBaseUserInfo(userEntity.getId() + "", userInfoListener);
        } else {
            viewLogin.setVisibility(View.VISIBLE);
            viewUserInfo.setVisibility(View.GONE);
        }

        updateMessageCenterState();

    }

    ParseHttpListener userInfoListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {

            return DataParse.parseObjectJson(UserEntity.class, jsonData);
        }

        @Override
        protected void afterParseData(UserEntity object) {
            if (null != object) {

                updateUserFollowInfo(object);
            }

        }
    };

    private void updateUserFollowInfo(UserEntity object) {


        handleNumber(tvFollowers, object.getFollowed_by_count());
        handleNumber(tvFollowing, object.getFriends_count());
    }

    private void handleNumber(TextView tv, int count) {

        tv.setText(StringFromatUtils.handleNumber(count));
    }

    private void updateMessageCenterState() {

        if (PortfolioApplication.hasUserLogin()) {
            int totalCount = MessageManager.getInstance().getTotalUnreadCount();
            if (totalCount > 0) {
                unreadCountTV.setVisibility(View.VISIBLE);
                unreadCountTV.setText(totalCount + "");
            } else {
                unreadCountTV.setVisibility(View.GONE);
            }
        }
    }


    private void startSettingActivity() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    private void startUserInfoActivity() {
        Intent intent = CombinationUserActivity.getIntent(getActivity(), UserEngineImpl.getUserEntity().getUsername(),
                UserEngineImpl.getUserEntity().getId() + "");
        startActivity(intent);
    }

    @OnClick({R.id.btn_login, R.id.setting_layout_icon, R.id.user_myfunds_layout, R.id.message_center_layout,
            R.id.ll_following, R.id.ll_followers,R.id.ll_flowPackage,R.id.ll_inviteFriends})
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_login:
                UIUtils.iStartLoginActivity(getActivity());
                break;
            case R.id.setting_layout_icon:
                startUserInfoActivity();
                break;
            case R.id.user_myfunds_layout:
                if (!UIUtils.iStartLoginActivity(getActivity())) {
                    startActivity(new Intent(getActivity(), MyCombinationActivity.class));
                }
                break;
            case R.id.message_center_layout:
                if (!UIUtils.iStartLoginActivity(getActivity()) ) {

                    MessageManager.getInstance().startConversationList(getActivity());
//                    RongIM.getInstance().startConversationList(getActivity());
                }
                break;
            case R.id.ll_following:
                Intent followIntent = new Intent(getActivity(), FriendsOrFollowersActivity.class);
                followIntent.putExtra(FriendsOrFollowersActivity.KEY, FriendsOrFollowersActivity.FRIENDS);
                followIntent.putExtra(FriendsOrFollowersActivity.USER_ID, UserEngineImpl.getUserEntity().getId() + "");
                startActivity(followIntent);
                break;
            case R.id.ll_followers:
                Intent intent1 = new Intent(getActivity(), FriendsOrFollowersActivity.class);
                intent1.putExtra(FriendsOrFollowersActivity.KEY, FriendsOrFollowersActivity.FOLLOWER);
                intent1.putExtra(FriendsOrFollowersActivity.USER_ID, UserEngineImpl.getUserEntity().getId() + "");
                startActivity(intent1);
                break;
            case R.id.ll_flowPackage:

                ArrayList<String> list=new ArrayList();
                list.add("http://img.1985t.com/uploads/attaches/2014/07/18314-I1VlAZ.jpg");
                PhotoViewActivity.startPhotoViewActivity(mActivity,list,0);
//                startActivity(new Intent(mActivity, BBSActivity.class));
//                if (!UIUtils.iStartLoginActivity(getActivity())) {
//
//                    Intent intent = new Intent(getActivity(), FlowPackageActivity.class);
//                    startActivity(intent);
//                }
                break;
            case R.id.ll_inviteFriends:
                if (!UIUtils.iStartLoginActivity(getActivity())) {
                    startActivity(new Intent(getActivity(), InviteFriendsActivity.class));
                }

                break;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void updateMessageCenter(NewMessageEvent newMessageEvent) {

        updateMessageCenterState();

    }



}