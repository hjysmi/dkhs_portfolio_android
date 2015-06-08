package com.dkhs.portfolio.ui.messagecenter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.BaseInfoEngine;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.WebActivity;
import com.dkhs.portfolio.ui.YanbaoDetailActivity;
import com.dkhs.portfolio.utils.PromptManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            if (segments.get(0).equals("s") && segments.size() >= 3) {
                gotoStockQuotesActivity(segments);
            } else if (segments.get(0).equals("p") && segments.size() >= 2) {
                gotoOrderFundDetailActivity(segments.get(1));
            } else if (segments.get(0).equals("statuses") && segments.size() >= 2) {
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
     *
     * @param id
     */
    private void gotoNewOrYaoBaoDetail(String id) {

        BaseInfoEngine baseInfoEngine = new BaseInfoEngine();
        baseInfoEngine.getOptionNewsBean(id, new BasicHttpListener() {
            @Override
            public void beforeRequest() {
                //fixme 由于用的到 PromptManager.showProgressDialog,里面维护一个静态的进度框,所以还没走到requestCallBack 就会被关闭
                PromptManager.showProgressDialog(context, "", true);
                super.beforeRequest();
            }

            @Override
            public void onSuccess(String result) {

                OptionNewsBean optionNewsBean = DataParse.parseObjectJson(OptionNewsBean.class, result);
                try {
                    Intent intent;
                    if (null != optionNewsBean.getSymbols() && optionNewsBean.getSymbols().size() > 0) {
                        intent = YanbaoDetailActivity.newIntent(context, optionNewsBean.getId(),
                                optionNewsBean.getSymbols().get(0).getSymbol(), optionNewsBean
                                        .getSymbols().get(0).getAbbrName(), optionNewsBean.getContentType());
                    } else {
                        intent = YanbaoDetailActivity.newIntent(context, optionNewsBean.getId(), null, null, null);
                    }
                    context.startActivity(intent);
                    // startActivity(intent);
//                    UIUtils.startAminationActivity(context, intent);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void requestCallBack() {
                super.requestCallBack();
                PromptManager.closeProgressDialog();
            }
        });
    }

    /**
     * 跳转到组合界面
     *
     * @param id
     */
    private void gotoOrderFundDetailActivity(String id) {

        BaseInfoEngine baseInfoEngine = new BaseInfoEngine();
        baseInfoEngine.getCombinationBean(id, new BasicHttpListener() {
            @Override
            public void beforeRequest() {
                //fixme 由于用的到 PromptManager.showProgressDialog,里面维护一个静态的进度框,所以还没走到requestCallBack 就会被关闭
                PromptManager.showProgressDialog(context, "", true);
                super.beforeRequest();
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String championBeanStr = jsonObject.getString("portfolio");
                    CombinationBean mChampionBean = DataParse.parseObjectJson(CombinationBean.class, championBeanStr);
                    context.startActivity(CombinationDetailActivity.newIntent(context, mChampionBean));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestCallBack() {
                super.requestCallBack();
                PromptManager.closeProgressDialog();
            }
        });

    }

    /**
     * 跳转到个或指数详情
     *
     * @param segments
     */
    private void gotoStockQuotesActivity(final List<String> segments) {
        final SelectStockBean itemStock = new SelectStockBean();
        itemStock.setId(Long.parseLong(segments.get(2)));
        itemStock.setCode(segments.get(1));
        itemStock.setSymbol_type("1");
        itemStock.setName("- -");
        new QuotesEngineImpl().quotes(segments.get(1), new BasicHttpListener() {
            @Override
            public void beforeRequest() {
            //fixme 由于用的到 PromptManager.showProgressDialog,里面维护一个静态的进度框,所以还没走到requestCallBack 就会被关闭
                PromptManager.showProgressDialog(context, "", true);
                super.beforeRequest();
            }

            @Override
            public void onSuccess(String jsonData) {

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    JSONObject jsonOb = jsonArray.getJSONObject(0);
                    StockQuotesBean stockQuotesBean = DataParse.parseObjectJson(StockQuotesBean.class, jsonOb);

                    itemStock.setSymbol_type(stockQuotesBean.getSymbol_type());
                    itemStock.setName(stockQuotesBean.getAbbrName());
                    context.startActivity(StockQuotesActivity.newIntent(context, itemStock));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestCallBack() {
                super.requestCallBack();
                PromptManager.closeProgressDialog();
            }
        });


    }

}
