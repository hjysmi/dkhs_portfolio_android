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
import com.dkhs.portfolio.ui.SettingActivity;
import com.dkhs.portfolio.ui.adapter.UserInfoAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
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


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mInfoAdatper);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeProvider(mInfoAdatper)
                .color(Color.TRANSPARENT)
                .build());


    }


    private void updateUserInfo() {

        updateMessageCenterState();

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
        StatService.onPageStart(getActivity(), TAG);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        updateMessageCenterState();
        mInfoAdatper.notifyItemChanged(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(getActivity(), TAG);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


}