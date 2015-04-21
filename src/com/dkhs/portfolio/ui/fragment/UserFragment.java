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
import android.test.UiThreadTest;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.MyCombinationActivity;
import com.dkhs.portfolio.ui.RCChatListActivity;
import com.dkhs.portfolio.ui.SettingActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;
import com.dkhs.portfolio.ui.eventbus.RongConnectEvent;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import io.rong.imkit.RongIM;

/**
 * @ClassName UserFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-2-5 下午1:01:32
 * @version 1.0
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


    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setTitle(R.string.title_user);


    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResume() {

        super.onResume();
        updateUserInfo();

    }

    private void initView(View view) {
        Button addButton = getRightButton();
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
        //标记  已阅，红点不见
        PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.S_APP_NEW_MESSAGE, false);
        BusProvider.getInstance().post(new NewMessageEvent());
        if (PortfolioApplication.hasUserLogin()) {
            viewLogin.setVisibility(View.GONE);
            viewUserInfo.setVisibility(View.VISIBLE);
            String account = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT);
            // account = setAccount(account);
            if (!TextUtils.isEmpty(account)) {
                settingTextAccountText.setText(account);
            }
            settingTextNameText.setText(PortfolioPreferenceManager
                    .getStringValue(PortfolioPreferenceManager.KEY_USERNAME));

            String url = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL);
            if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
                // url = DKHSUrl.BASE_DEV_URL + url;
                BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
                bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_user_head);
                // bitmapUtils.configDefaultLoadingImage(R.drawable.ic_user_head);
                bitmapUtils.display(settingImageHead, url);

            } else {
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_head);
                b = UIUtils.toRoundBitmap(b);
                settingImageHead.setImageBitmap(b);

            }
        } else {
            viewLogin.setVisibility(View.VISIBLE);
            viewUserInfo.setVisibility(View.GONE);

        }


        updateMessageCenterState();


    }

    private void updateMessageCenterState() {
        if (PortfolioApplication.hasUserLogin()) {
                RongIM rongIM=RongIM.getInstance();
                if(rongIM != null) {
                    int totalCount = rongIM.getTotalUnreadCount();

                    if (totalCount > 0) {
                        unreadCountTV.setVisibility(View.VISIBLE);
                        unreadCountTV.setText(totalCount + "");
                    } else {
                        unreadCountTV.setVisibility(View.GONE);
                    }
                }else{
                    unreadCountTV.setVisibility(View.GONE);
                }

        }
    }

    // public String setAccount(String account) {
    // if (account.contains("@")) {
    // int k = account.indexOf("@");
    // account = account.substring(0, k - 3) + "***" + account.substring(k, account.length());
    // } else {
    // account = account.substring(0, account.length() - 5) + "***"
    // + account.substring(account.length() - 2, account.length());
    // }
    // return account;
    // }

    private void startSettingActivity() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
        // UIUtils.startAminationActivity(getActivity(), intent);
    }

    @OnClick({ R.id.btn_login, R.id.ll_userinfo_layout, R.id.user_myfunds_layout,R.id.message_center_layout })
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.btn_login == id) {
            UIUtils.iStartLoginActivity(getActivity());
        } else if (R.id.ll_userinfo_layout == id) {
            startSettingActivity();
        } else if (R.id.user_myfunds_layout == id) {
            if (!UIUtils.iStartLoginActivity(getActivity())) {
//                getActivity().UIUtils.startAminationActivity(getActivity(), (new Intent(getActivity(),
//                        MyCombinationActivity.class)));
                startActivity(new Intent(getActivity(),
                        MyCombinationActivity.class));
            }
        }else if (R.id.message_center_layout == id) {
            if (!UIUtils.iStartLoginActivity(getActivity())) {


                RongIM rongIM=   RongIM.getInstance();

                if(rongIM ==null) {
                    BusProvider.getInstance().post(new RongConnectEvent());
                }
                startActivity(new Intent(getActivity(),
                        RCChatListActivity.class));
            }
        }

    }

//    @OnClick(R.id.message_center_layout)
//    public void messageCenterClick(View v) {
//
//        if (!UIUtils.iStartLoginActivity(getActivity())) {
//
//            Toast.makeText(getActivity(),"t",Toast.LENGTH_LONG).show();
//            RongIM.getInstance().startConversationList(getActivity());
//        }
//    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void  updateMessageCenter(NewMessageEvent newMessageEvent){


        updateMessageCenterState();

    }


}
