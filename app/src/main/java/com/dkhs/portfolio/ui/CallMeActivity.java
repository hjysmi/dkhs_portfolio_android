package com.dkhs.portfolio.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.CallMeFragment;
import com.dkhs.portfolio.ui.fragment.CommentMeFragment;
import com.dkhs.portfolio.ui.messagecenter.ConversationListListener;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class CallMeActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_me);



        try {
            RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, ConversationListListener.getCallMeId());

        }catch (Exception e){
            e.printStackTrace();
        }

        setTitle(R.string.title_activity_call_me);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new CallMeFragment()).commitAllowingStateLoss();
    }


}
