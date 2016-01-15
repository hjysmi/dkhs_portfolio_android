package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RongConnectSuccessEvent;
import com.dkhs.portfolio.ui.messagecenter.ConversationListListener;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.lidroid.xutils.util.LogUtils;
import com.squareup.otto.Subscribe;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;

/**
 * @author zwm
 * @version 1.0
 * @ClassName RCChatListActivity
 * @Description TODO(会话列表)
 * @date 2015/4/16.15:21
 */
public class RCChatListActivity extends ModelAcitivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setTitle(R.string.message_center);

//        Intent intent = getIntent();
//        LogUtils.e(intent.getDataString());
//        LogUtils.e(intent.getData().toString());
//        LogUtils.e(intent.toString());

        BusProvider.getInstance().register(this);
        if (MessageManager.getInstance().isConnect()) {
            displayRClListFragment();
        }

        RongIM.setConversationListBehaviorListener(new ConversationListListener());
    }


    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onRongConnect(RongConnectSuccessEvent event) {

        displayRClListFragment();
    }

    private void displayRClListFragment() {

        ConversationListFragment conversationListFragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        conversationListFragment.setUri(uri);
        if (PortfolioApplication.hasUserLogin()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, conversationListFragment).commitAllowingStateLoss();
            findViewById(R.id.loadView).setVisibility(View.GONE);
        }
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_rcchat_list;
    }
}