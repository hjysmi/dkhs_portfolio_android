package com.dkhs.portfolio.ui.messagecenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dkhs.portfolio.ui.CallMeActivity;
import com.dkhs.portfolio.ui.CommentMeActivity;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;

/**
 * @author zwm
 * @version 2.0
 * @ClassName ConversationListListener
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/8/25.
 */
public class ConversationListListener implements RongIM.ConversationListBehaviorListener {
    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {

        if (uiConversation.getConversationSenderId().equals(getCallMeId())) {
            context.startActivity(new Intent(context, CallMeActivity.class));
            return true;
        }
        if (uiConversation.getConversationSenderId().equals(getCommentMeId())) {
            context.startActivity(new Intent(context, CommentMeActivity.class));
            return true;
        }
        return false;
    }


    public static String getCallMeId() {

        String id = null;
        switch (PortfolioPreferenceManager.getIntValue(PortfolioPreferenceManager.KEY_APP_URL)) {
            case 0:
//                id=   DKHSUrl.BASE_DEV_URL;
                id = "640";
                break;
            case 1:
//                id=   DKHSUrl.BASE_TEST_URL;
                id = "2944";
                break;
            case 2:
//                id=   DKHSUrl.BASE_DEV_MAIN;
                id = "18393";
                break;
            case 3:
//                id=   DKHSUrl.BASE_DEV_TAG;
                id = "640";
                break;
            default:
                id = "-1";
        }
        return id;
    }

    public static String getCommentMeId() {
        String id = null;
        switch (PortfolioPreferenceManager.getIntValue(PortfolioPreferenceManager.KEY_APP_URL)) {
            case 0:
//                id=   DKHSUrl.BASE_DEV_URL;
                id = "641";
                break;
            case 1:
//                id=   DKHSUrl.BASE_TEST_URL;
                id = "2946";
                break;
            case 2:
//                id=   DKHSUrl.BASE_DEV_MAIN;
                id = "18394";
                break;
            case 3:
//                id=   DKHSUrl.BASE_DEV_TAG;
                id = "641";
                break;
            default:
                id = "-1";
        }
        return id;
    }


}
