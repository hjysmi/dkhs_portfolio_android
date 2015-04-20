package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.fragment.InvalidStateFragment;
import com.lidroid.xutils.util.LogUtils;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;

/**
 * @author zwm
 * @version 1.0
 * @ClassName RCChatActivity
 * @Description TODO(单聊界面)
 * @date 2015/4/16.15:21
 */
public class RCChatActivity extends ModelAcitivity {


    private RongIMClient.ConversationType conversationType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();

        String data = intent.getDataString();

        final Uri uri = Uri.parse(data);


        final String targetId = uri.getQueryParameter("targetId");

        String title = uri.getQueryParameter("title");

        String conversationTypeStr = uri.getLastPathSegment();


        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onClickUserPortrait(Context context, RongIMClient.ConversationType conversationType, RongIMClient.UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onClickMessage(Context context, RongIMClient.Message message) {

                LogUtils.e(message.getContent()+"");
                return true  ;
            }
        });

        conversationType = RongIMClient.ConversationType.valueOf(conversationTypeStr.toUpperCase());


        if (TextUtils.isEmpty(title)) {
            new GetConversationTitleTask().execute(targetId);
        } else {
            setTitle(title);
        }


        if (PortfolioApplication.hasUserLogin()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new ConversationFragment()).commit();
            getRightButton().setBackgroundResource(R.drawable.rc_bar_more);
            getRightButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RongIM.getInstance().startConversationSetting(RCChatActivity.this, conversationType, targetId);
                }
            });

        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new InvalidStateFragment()).commit();
        }


    }

    /**
     * 获取会话的标题
     */
    class GetConversationTitleTask extends AsyncTask<String, Void, String> {




        @Override
        protected String doInBackground(String... params) {

            RongIMClient client = RongIM.getInstance().getRongIMClient();
            RongIMClient.Conversation conversation = client.getConversation(conversationType, params[0]);
            LogUtils.e(conversation.getConversationTitle());

            return conversation.getConversationTitle();
        }

        @Override
        protected void onPostExecute(String  str) {


            if (TextUtils.isEmpty(str)) {
                str = getResources().getString(R.string.message_center);
            }

            setTitle(str);
            super.onPostExecute(str);
        }
    }
}