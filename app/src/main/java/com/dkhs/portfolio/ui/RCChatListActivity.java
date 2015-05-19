package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.fragment.InvalidStateFragment;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.lidroid.xutils.util.LogUtils;
import com.squareup.otto.Subscribe;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * @author zwm
 * @version 1.0
 * @ClassName RCChatListActivity
 * @Description TODO(会话列表)
 * @date 2015/4/16.15:21
 */
public class RCChatListActivity extends ModelAcitivity {
    private ConversationListFragment conversationListFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setTitle(R.string.message_center);
        BusProvider.getInstance().register(this);


        conversationListFragment  = new ConversationListFragment();
        if(PortfolioApplication.hasUserLogin()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,conversationListFragment).commit();

        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new InvalidStateFragment()).commit();
        }

    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public  void  updateChatList(Message message){


        LogUtils.e("--updateChatList--");
        Conversation  conversation=RongIMClient.getInstance().getConversation(Conversation.ConversationType.PRIVATE, message.getSenderUserId());
        conversation.setLatestMessageId(message.getMessageId());
        conversationListFragment.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MessageManager.getInstance().getmConnct().setOnReceiveMessageListener();

    }
}