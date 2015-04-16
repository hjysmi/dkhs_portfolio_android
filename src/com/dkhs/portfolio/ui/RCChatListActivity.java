package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.os.Bundle;

import com.dkhs.portfolio.R;

import io.rong.imkit.fragment.ConversationListFragment;

/**
 * @author useradmin
 * @version 1.0
 * @ClassName zwm
 * @Description TODO(会话列表)
 * @date 2015/4/16.15:21
 */
public class RCChatListActivity extends ModelAcitivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setTitle(R.string.message_center);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new ConversationListFragment()).commit();
    }
}