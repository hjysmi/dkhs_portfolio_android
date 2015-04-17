package com.dkhs.portfolio.ui;

import android.os.Bundle;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.fragment.InvalidStateFragment;
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


        if(PortfolioApplication.hasUserLogin()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new ConversationListFragment()).commit();

        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFL, new InvalidStateFragment()).commit();
        }


    }
}