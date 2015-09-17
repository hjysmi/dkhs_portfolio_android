package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.CommentMeFragment;
import com.dkhs.portfolio.ui.messagecenter.ConversationListListener;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class CommentMeActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, ConversationListListener.getCommentMeId());


        }catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_comment_me);
        setTitle(R.string.title_activity_comment_me);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new CommentMeFragment()).commitAllowingStateLoss();
    }

}
