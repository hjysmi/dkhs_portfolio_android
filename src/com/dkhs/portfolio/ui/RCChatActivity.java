package com.dkhs.portfolio.ui;

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
 * @author useradmin
 * @version 1.0
 * @ClassName zwm
 * @Description TODO(单聊界面)
 * @date 2015/4/16.15:21
 */
public class RCChatActivity extends ModelAcitivity {


    private RongIMClient.ConversationType  conversationType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();

        String data = intent.getDataString();

        final Uri uri = Uri.parse(data);



        final String targetId = uri.getQueryParameter("targetId");

        String title = uri.getQueryParameter("title");

        String conversationTypeStr=uri.getLastPathSegment();

       LogUtils.e(RongIMClient.ConversationType.SYSTEM.getName());

        conversationType= RongIMClient.ConversationType.valueOf(conversationTypeStr.toUpperCase());


        if(TextUtils.isEmpty(title)){
            new  GetConversationTitleTask().execute(targetId);
        }else{
            setTitle(title);
        }





        if(PortfolioApplication.hasUserLogin()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new ConversationFragment()).commit();
            getRightButton().setBackgroundResource(R.drawable.rc_bar_more);
            getRightButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RongIM.getInstance().startConversationSetting(RCChatActivity.this,conversationType, targetId);
                }
            });

        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new InvalidStateFragment()).commit();
        }




    }

    /**
     * 获取会话的标题
     */
   class GetConversationTitleTask extends AsyncTask<String,Void,Void>{


       private  String title;
       @Override
       protected Void doInBackground(String... params) {

           RongIMClient client = RongIM.getInstance().getRongIMClient();
           RongIMClient.Conversation conversation = client.getConversation(conversationType, params[0]);
           LogUtils.e(conversation.getConversationTitle());
           title=conversation.getConversationTitle();

           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {


           if(TextUtils.isEmpty(title)){
               title=getResources().getString(R.string.message_center);
       }

           setTitle(title);
           super.onPostExecute(aVoid);
       }
   }
}