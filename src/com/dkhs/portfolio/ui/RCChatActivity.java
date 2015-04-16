package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.dkhs.portfolio.R;
import com.lidroid.xutils.util.LogUtils;

import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;

/**
 * @author useradmin
 * @version 1.0
 * @ClassName zwm
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/4/16.15:21
 */
public class RCChatActivity extends ModelAcitivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();

        String data = intent.getDataString();

        final Uri uri = Uri.parse(data);
        LogUtils.e(data);
        setTitle(uri.getQueryParameter("title"));
        getRightButton().setBackgroundResource(R.drawable.rc_bar_more);
        getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startConversationSetting(RCChatActivity.this, RongIMClient.ConversationType.PRIVATE, uri.getQueryParameter("targetId"));
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new ConversationFragment()).commit();


    }
}