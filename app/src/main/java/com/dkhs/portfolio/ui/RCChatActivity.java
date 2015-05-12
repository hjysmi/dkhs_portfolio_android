package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.fragment.InvalidStateFragment;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.ConversationBehaviorListener;
import io.rong.imkit.UiConversation;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

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


        RongIM.setConversationBehaviorListener(new ConversationBehaviorListener() {

            @Override
            public boolean onConversationLongClick(Context arg0, UiConversation arg1) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onMessageClick(Context arg0, Message message) {
                // TODO Auto-generated method stub
                MessageContent messageContent = message.getContent();
                if (messageContent instanceof TextMessage) {// 文本消息
                    TextMessage textMessage = (TextMessage) messageContent;
                    Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());

                } else if (messageContent instanceof ImageMessage) {// 图片消息
                    ImageMessage imageMessage = (ImageMessage) messageContent;
                    Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
                } else if (messageContent instanceof VoiceMessage) {// 语音消息
                    VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                    Log.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
                } else if (messageContent instanceof RichContentMessage) {// 图文消息
                    handCustomRichContentMessage((RichContentMessage) messageContent);
                }

                return false;
            }

            @Override
            public boolean onMessageLongClick(Context arg0, Message arg1) {
                return true;
            }

            @Override
            public boolean onUserPortraitClick(Context arg0, ConversationType arg1, UserInfo arg2) {
                return false;
            }

            @Override
            public boolean onConversationItemClick(Context arg0, UiConversation arg1) {
                return false;
            }
        });

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
            getRightButton().setBackgroundResource(R.drawable.rc_bar_more);
            getRightButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RongIM.getInstance().startConversationSetting(RCChatActivity.this, conversationType, targetId);
                }
            });


            //解决列表闪一下
            getTitleView().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (!isDestroy) {
                        getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
                    }

                }
            }, 1200);

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

    /**
     * 处理由我们自己的  RichContentMessage  消息;
     *
     * @param messageContent
     */
    private void handCustomRichContentMessage(RichContentMessage messageContent) {


        Uri uri = Uri.parse(messageContent.getImgUrl());


        List<String> segments = uri.getPathSegments();


        if (segments.size() > 0) {

            if (segments.get(0).equals("s") && segments.size() == 3) {
                String name = uri.getQueryParameter("name");
                gotoStockQuotesActivity(segments, name);
            } else if (segments.get(0).equals("p") && segments.size() == 2) {

                gotoOrderFundDetailActivity(segments.get(1));

            } else if (segments.get(0).equals("statuses") && segments.size() == 2) {

            }
        }

    }

    private void gotoOrderFundDetailActivity(String id) {

        CombinationBean mChampionBean = new CombinationBean();
        mChampionBean.setId(id);
        startActivity(NewCombinationDetailActivity.newIntent(this, mChampionBean));

//        this.startActivity(
//                OrderFundDetailActivity.getIntent(this, mChampionBean, true,
//                        FundsOrderFragment.ORDER_TYPE_DAY));
    }

    private void gotoStockQuotesActivity(List<String> segments, String name) {

        SelectStockBean itemStock = new SelectStockBean();
        itemStock.setId(Long.parseLong(segments.get(2)));
        itemStock.setCode(segments.get(1));
        itemStock.setSymbol_type("1");

        itemStock.setName(name);

        UIUtils.startAminationActivity(this, StockQuotesActivity.newIntent(this, itemStock));
    }


    /**
     * 获取会话的标题
     */
    class GetConversationTitleTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            RongIMClient client = RongIM.getInstance().getRongClient();
            Conversation conversation = client.getConversation(conversationType, params[0]);
            LogUtils.e(conversation.getConversationTitle());
            return conversation.getConversationTitle();
        }

        @Override
        protected void onPostExecute(String str) {

            if (TextUtils.isEmpty(str)) {
                str = getResources().getString(R.string.message_center);
            }
            setTitle(str);
            super.onPostExecute(str);
        }
    }
}