package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;

import io.rong.imkit.fragment.SetConversationNotificationFragment;
import io.rong.imkit.fragment.SetConversationToTopFragment;

/**
 * @author zwm
 * @version 1.0
 * @ClassName RCChatSettingActivity
 * @Description TODO(会话设置)
 * @date 2015/4/16.15:22
 */
public class RCChatSettingActivity extends ModelAcitivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_setting);


        setTitle(R.string.setting);


        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new SetConversationToTopFragment()).commit();


    }
}