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
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @author zwm
 * @version 1.0
 * @ClassName RCChatActivity
 * @Description TODO(单聊界面)
 * @date 2015/4/16.15:21
 */
public class RCChatActivity extends ModelAcitivity {


    private ConversationType conversationType;

    private static final String TAG = "RCChatActivity";
    private ConversationFragment fragment;

    private MessageHandler messageHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final Intent intent = getIntent();
        String data = intent.getDataString();
        final Uri uri = Uri.parse(data);
        final String targetId = uri.getQueryParameter("targetId");
        String title = uri.getQueryParameter("title");
        String conversationTypeStr = uri.getLastPathSegment();
        RongIM.setConversationBehaviorListener(new ConversationBehaviorListener());
        messageHandler = new MessageHandler(this);
        conversationType = ConversationType.valueOf(conversationTypeStr.toUpperCase());
        if (TextUtils.isEmpty(title)) {
            new GetConversationTitleTask().execute(targetId);
        } else {
            setTitle(title);
        }

        if (PortfolioApplication.hasUserLogin()) {
            fragment = new ConversationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, fragment).commit();
//            getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
//            getRightButton().setBackgroundResource(R.drawable.rc_bar_more);
//            getRightButton().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent1=new Intent(mContext,RCChatSettingActivity.class);
//                    mContext.startActivity(intent1);
////                    RongIM.getInstance().startConversationSetting(RCChatActivity.this, conversationType, targetId);
//                }
//            });

            //解决列表闪一下
            getTitleView().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (!isDestroy) {
                        getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
                    }

                }
            }, 2200);

        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new InvalidStateFragment()).commit();
        }


    }


    private boolean isDestroy = false;

    @Override
    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();

    }


    class GetConversationTitleTask extends AsyncTask<String, Void, UserInfo> {

        @Override
        protected UserInfo doInBackground(String... params) {


            return MessageManager.getInstance().getmConnct().getUserInfo(params[0]);

        }

        @Override
        protected void onPostExecute(UserInfo userInfo) {
            if (!TextUtils.isEmpty(userInfo.getName())) {

                setTitle(userInfo.getName());
            }
            super.onPostExecute(userInfo);
        }
    }


    class ConversationBehaviorListener implements RongIM.ConversationBehaviorListener {

        @Override
        public boolean onUserPortraitClick(Context context, ConversationType conversationType, UserInfo userInfo) {
            return false;
        }

        @Override
        public boolean onUserPortraitLongClick(Context context, ConversationType conversationType, UserInfo userInfo) {
            return false;
        }

        @Override
        public boolean onMessageClick(Context context, View view, Message message) {
            return messageHandler.handleMessage(message);
        }

        @Override
        public boolean onMessageLinkClick(Context context, String s) {
            return false;
        }


        @Override
        public boolean onMessageLongClick(Context context, View view, Message message) {
            return false;
        }




    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_position_configure;
    }
}