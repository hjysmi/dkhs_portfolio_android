package com.dkhs.portfolio.ui.messagecenter;


import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.NewCombinationDetailActivity;
import com.dkhs.portfolio.ui.NewsActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.WebActivity;
import com.dkhs.portfolio.ui.fragment.FundsOrderFragment;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.List;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * @author 曾伟明
 * @version 1.0
 * @ClassName MessageHandler
 * @Description Message的处理类
 * @date 2015-5-18 下午4:13:10
 */
public class MessageHandler {


    public String TAG = "MessageHandler";
    public Context context;


    public MessageHandler(Context context) {
        this.context = context;
    }


    public boolean handleMessage(Message message) {

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
        } else if (messageContent instanceof DKImgTextMsg) {// DKImgTextMsg
            handleDKImgTextMsg(message);
        }
        return true;
    }

    /**
     * 处理由我们自己的  DKImgTextMsg  消息;
     *
     * @param message
     */
    private void handleDKImgTextMsg(Message message) {
        DKImgTextMsg messageContent = (DKImgTextMsg) message.getContent();
        if (TextUtils.isEmpty(messageContent.getUrl())) {
            //无url 跳转到单聊界面;
            MessageManager.getInstance().startPrivateChat(context, message.getSenderUserId(), null);
            return;
        }
        Uri uri = Uri.parse(messageContent.getUrl());
        List<String> segments = uri.getPathSegments();
        if (segments.size() > 0) {
            if (segments.get(0).equals("s") && segments.size() == 3) {
                gotoStockQuotesActivity(segments);
            } else if (segments.get(0).equals("p") && segments.size() == 2) {
                gotoOrderFundDetailActivity(segments.get(1));
            } else if (segments.get(0).equals("statuses") && segments.size() == 2) {
                gotoNewOrYaoBaoDetail(segments.get(1));
            } else {
                //不在定义范围内 ,使用WebActivity去处理
                context.startActivity(WebActivity.getIntent(context, messageContent.getTitle(), messageContent.getUrl()));
            }
        } else {
            //不在定义范围内 ,使用WebActivity去处理
            context.startActivity(WebActivity.getIntent(context, messageContent.getTitle(), messageContent.getUrl()));
        }
    }

    /**
     * 跳转到研报或者是公告
     * @param id
     */
    private void gotoNewOrYaoBaoDetail(String  id) {
        //todo  未处理跳转逻辑


    }

    /**
     * 跳转到组合界面
     * @param id
     */
    private void gotoOrderFundDetailActivity(String id) {

        //todo  未处理跳转逻辑
        CombinationBean mChampionBean = new CombinationBean();
        mChampionBean.setId(id);
        context.startActivity(NewCombinationDetailActivity.newIntent(context, mChampionBean));


    }

    /**
     * 跳转到个股详情
     * @param segments
     */
    private void gotoStockQuotesActivity(List<String> segments) {
        SelectStockBean itemStock = new SelectStockBean();
        itemStock.setId(Long.parseLong(segments.get(2)));
        itemStock.setCode(segments.get(1));
        itemStock.setSymbol_type("1");
        itemStock.setName("- -");
        context.startActivity(StockQuotesActivity.newIntent(context, itemStock));

    }

}
