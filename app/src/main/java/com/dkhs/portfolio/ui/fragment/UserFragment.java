/**
 * @Title UserFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-5 下午1:01:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.SettingActivity;
import com.dkhs.portfolio.ui.adapter.UserInfoAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;
import com.dkhs.portfolio.ui.eventbus.TopEvent;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.widget.HorizontalDividerItemDecoration;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

/**
 * @author zjz
 * @version 1.0
 * @ClassName UserFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-5 下午1:01:32
 */
public class UserFragment extends BaseTitleFragment {

    public static final String TAG = "UserFragment";
    //    private UserEngineImpl userImp = new UserEngineImpl();
    private UserInfoAdapter mInfoAdatper;
    private RecyclerView mRecyclerView;

    private boolean isFirst = true;//防止onResume,requestData多次获取数据


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mInfoAdatper = new UserInfoAdapter(getActivity());
    }

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
    }

    @Override
    public void requestData() {
        updateState();
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


        RecyclerView recyclerView;
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mInfoAdatper);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeProvider(mInfoAdatper)
                .color(Color.TRANSPARENT)
                .build());


    }


    private void updateMessageCenterState() {

        if (PortfolioApplication.hasUserLogin()) {
            int totalCount = MessageManager.getInstance().getTotalUnreadCount();

            if (null != mInfoAdatper) {

                mInfoAdatper.setUnreadCount(totalCount);
            }
        }
    }


    private void startSettingActivity() {

//        if(BuildConfig.DEBUG){
//            //测试
//            if(UserEngineImpl.getUserEntity() != null) {
//                Notification notificationCompat = new NotificationCompat.Builder(PortfolioApplication.getInstance()).setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("谁牛金融userId" + UserEngineImpl.getUserEntity().getId()).setContentText(DKHSClient.getHeadUrl()).setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
//                        .build();
//                notificationCompat.icon = R.drawable.ic_launcher;
//
//                NotificationManager notificationManager = (NotificationManager) PortfolioApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(-1, notificationCompat);
//            }
//        }


        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @Subscribe
    public void forward2Top(TopEvent event) {
        if (event != null && isVisible()) {
            if (mRecyclerView != null) {
                mRecyclerView.scrollToPosition(0);
            }
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

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
        } else {
            updateState();
        }
    }

    private void updateState() {
        updateMessageCenterState();
        updateVerifyStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    ParseHttpListener userInfoListener = new ParseHttpListener<UserEntity>() {

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            return DataParse.parseObjectJson(UserEntity.class, jsonData);
        }

        @Override
        protected void afterParseData(UserEntity entity) {
            if (null != entity) {
                checkRefresh(entity);
                GlobalParams.LOGIN_USER = entity;
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERNAME, entity.getUsername());
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERID, entity.getId() + "");
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_HEADER_URL, entity.getAvatar_md());
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_VERIFIED, entity.verified);
                if (null == entity.verified_type) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_VERIFIED_TYPE, -1);
                } else {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_VERIFIED_TYPE, entity.verified_type);
                }
                if (null == entity.verified_status) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_VERIFIED_STATUS, -1);
                } else {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_VERIFIED_STATUS, entity.verified_status);
                }

                //  mInfoAdatper.notifyItemChanged(0);
            }
        }

        @Override
        public void onFailure(ErrorBundle errorBundle) {
            super.onFailure(errorBundle);

        }
    };

    /**
     * 在线数据与本地数据匹配
     * 如果不同就刷新页面
     * 防止过度刷新
     *
     * @param entity
     */
    private void checkRefresh(UserEntity entity) {
        try {
            if (!PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME).equals(entity.getUsername())
                    || GlobalParams.LOGIN_USER.getFriends_count() != entity.getFriends_count()
                    || GlobalParams.LOGIN_USER.getFollowed_by_count() != entity.getFollowed_by_count()
                    || GlobalParams.LOGIN_USER.verified_status != entity.verified_status
                    || !GlobalParams.LOGIN_USER.getAvatar_md().equals(entity.getAvatar_md())
                    || !GlobalParams.LOGIN_USER.getUsername().equals(entity.getUsername())) {
                mInfoAdatper.notifyItemChanged(0);
            }
        } catch (Exception e) {
            mInfoAdatper.notifyItemChanged(0);
        }

    }

    private void updateVerifyStatus() {
        if (PortfolioApplication.hasUserLogin()) {
            UserEngineImpl userEngine = new UserEngineImpl();
            UserEntity entity = UserEngineImpl.getUserEntity();
            userEngine.getBaseUserInfo(String.valueOf(entity.getId()), userInfoListener);
        }
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_mine;
    }
}