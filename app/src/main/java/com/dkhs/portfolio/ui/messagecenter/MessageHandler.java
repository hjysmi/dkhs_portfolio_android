package com.dkhs.portfolio.ui.messagecenter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.ProVerificationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.BaseInfoEngine;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.AdActivity;
import com.dkhs.portfolio.ui.BetterRecruitActivity;
import com.dkhs.portfolio.ui.CallMeActivity;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.CommentMeActivity;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.ui.InfoActivity;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.MarketSubpageActivity;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.fragment.MarketSubpageFragment;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
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
    public Context mContext;

    private List<String> mFilterUrls = new ArrayList<>();


    public MessageHandler(Context context) {
        this.mContext = context;
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
            MessageManager.getInstance().startPrivateChat(mContext, message.getSenderUserId(), null);
            return;
        }
        if (!handleURL(messageContent.getUrl())) {
            mContext.startActivity(AdActivity.getIntent(mContext, messageContent.getUrl()));
        }
    }


    public boolean handleURL(String url) {
        if (TextUtils.isEmpty(url)) {
            return true;
        }

        Uri uri = Uri.parse(url);
        boolean hasHandle = true;
        List<String> segments = uri.getPathSegments();
//        if(uri.getHost().equals("")) {


        if (segments.size() > 0) {
            if (segments.get(0).equals("s") && segments.size() >= 2) {
                gotoStockQuotesActivity(segments);
                hasHandle = true;
            } else if (segments.get(0).equals("p") && segments.size() >= 2 && segments.get(1).matches("\\d+")) {
                hasHandle = true;
                gotoOrderFundDetailActivity(segments.get(1));
            } else if (segments.get(0).equals("statuses") && segments.size() >= 2 && segments.get(1).equals("news")) {
                //https://www.dkhs.com/statuses/news/ //跳转至信息资讯页面
                gotoMainInfoActivity();
            } else if (segments.get(0).equals("statuses") && segments.size() >= 2 && segments.get(1).equals("mentions_timeline")) {
                //https://www.dkhs.com/statuses/mentions_timeline/ 跳转至【提到我的】页面
                gotoCallMeActivity();
            } else if (segments.get(0).equals("statuses") && segments.size() >= 2 && segments.get(1).equals("comments_timeline")) {
                //https://www.dkhs.com/statuses/comments_timeline/ 跳转至评论或回复我的
                gotoCommentMeActivity();
            } else if (segments.get(0).equals("statuses") && segments.size() >= 2 && segments.get(1).equals("public_timeline")) {
                //https://www.dkhs.com/statuses/public_timeline/ //跳转至社区热门话题界面d+
                gotoHostTopicsActivity();

            } else if (segments.get(0).equals("statuses") && segments.size() >= 2 && segments.get(1).matches("\\d+")) {
                hasHandle = true;
                gotoNewOrYaoBaoDetail(segments.get(1));
            } else if (segments.get(0).equals("u") && segments.size() >= 2) {
                hasHandle = true;
                gotoCombinationUserActivity(segments.get(1));
                //symbols/funds/managers/
            } else if (segments.get(0).equals("symbols") && segments.size() >= 3 && segments.get(1).equals("funds") && segments.get(2).equals("managers")) {
                //https://www.dkhs.com/symbols/funds/managers/ //跳转至基金经理排行页面
                //https://www.dkhs.com/symbols/funds/managers/pk/ //跳转至基金经理详情页
                String pk = null;
                if (segments.size() >= 4 && segments.get(3).matches("\\d+")) {
                    pk = segments.get(3);
                }
                gotoFundManager(pk);
            } else if (segments.get(0).equals("symbols") && segments.size() >= 2 && segments.get(1).equals("funds")) {
                //https://www.dkhs.com/symbols/funds/ //跳转至基金传统排行页面
                gotoFundsRanking();

            } else if (segments.get(0).equals("symbols") && segments.size() >= 3 && segments.get(1).equals("markets") && segments.get(2).equals("cn")) {
                //https://www.dkhs.com/symbols/markets/cn/ //跳转至沪深行情股票界面
                gotoSHActivity();
            } else if (segments.get(0).equals("symbols") && segments.size() >= 2 && segments.get(1).equals("following")) {
                //https://www.dkhs.com/symbols/following/ //跳转至自选股票界面
                gotoOptionSymbols();
            } else if (segments.get(0).equals("portfolio") && segments.size() >= 2 && segments.get(1).equals("ranking_list")) {
                // https://www.dkhs.com/portfolio/ranking_list/ //跳转至行情组合排行榜界面
                gotoCombinationRankingActivity();
            } else if (segments.get(0).equals("portfolio") && segments.size() >= 2 && segments.get(1).equals("create")) {
                //  https://www.dkhs.com/portfolio/create/ // 跳转至创建组合页面
                gotoCreateCombinationActivity();
            } else if (segments.get(0).equals("shakes") && segments.size() >= 1) {
                //https://www.dkhs.com/shakes/ //跳转至摇一摇界面
                gotoShakeActivity();
            } else if (segments.get(0).equals("accounts") && segments.size() >= 2 && segments.get(1).equals("mine")) {
                //https://www.dkhs.com/accounts/mine/ //跳转至“我的”页面
                gotoUserActivity();
            } else if ((segments.get(0).equals("accounts") && segments.size() >= 2 && segments.get(1).equals("pro_verfications"))) {
                gotoauthentication(url);
            } else {
                mContext.startActivity(AdActivity.getIntent(mContext, url));
            }
        } else if (!TextUtils.isEmpty(url)) {
            mContext.startActivity(AdActivity.getIntent(mContext, url));
        }
//        }

        return hasHandle;
    }

    /**
     * @param url
     */
    private void gotoauthentication(String url) {
        if (!UIUtils.iStartLoginActivity(mContext)) {
            int verified_status = PortfolioPreferenceManager.getIntValue(PortfolioPreferenceManager.KEY_VERIFIED_STATUS);
            if (2 == verified_status) {
                loadOnLineData(url);
            }else{
                goBetterRecruitActivity(url,null,true);
            }
        }
    }

    private void loadOnLineData(final String url) {
        DKHSClient.requestByGet(new ParseHttpListener<ProVerificationBean>() {
            @Override
            protected ProVerificationBean parseDateTask(String jsonData) {
                return DataParse.parseObjectJson(ProVerificationBean.class, jsonData);
            }

            @Override
            protected void afterParseData(ProVerificationBean bean) {
                //  updateProVerificationInfo(bean);
                goBetterRecruitActivity(url,bean,false);
            }
        }.setLoadingDialog(PortfolioApplication.getInstance()), DKHSUrl.User.get_pro_verification);
    }

    private void goBetterRecruitActivity(String url,ProVerificationBean bean,boolean needCheckUrl){
        Intent intent = new Intent(mContext, BetterRecruitActivity.class);
        intent.putExtra("proverification_bean", Parcels.wrap(bean));
        String verified_type = "0";
        if(needCheckUrl){
            Uri uri = Uri.parse(url);
            verified_type = uri.getQueryParameter("verified_type");
        }else{
            verified_type = String.valueOf(bean.pro.verified_type);
        }
        intent.putExtra("type", Integer.parseInt(verified_type));
        mContext.startActivity(intent);
    }

    private void gotoCallMeActivity() {
        mContext.startActivity(new Intent(mContext, CallMeActivity.class));
    }

    private void gotoCommentMeActivity() {

        mContext.startActivity(new Intent(mContext, CommentMeActivity.class));
    }


    private void gotoSHActivity() {
        MainActivity.gotoSHActivity(mContext);
    }

    private void gotoCreateCombinationActivity() {

        mContext.startActivity(PositionAdjustActivity.newIntent(mContext, null));
    }

    private void gotoUserActivity() {
        MainActivity.gotoUserActivity(mContext);
    }

    private void gotoShakeActivity() {
        MainActivity.gotoShakeActivity(mContext);


    }

    private void gotoOptionSymbols() {
        MainActivity.gotoOptionSymbols(mContext);
    }

    private void gotoHostTopicsActivity() {
        MainActivity.gotoHostTopicsActivity(mContext);


    }

    private void gotoFundsRanking() {
//        MainActivity.gotoFundsRanking(mContext);
        UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_ALL_RANKING_MONTH));
    }


    private void gotoCombinationRankingActivity() {
//        MainActivity.gotoCombinationRankingActivity(mContext);
        UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_COMBINATION));
    }

    private void gotoMainInfoActivity() {
        mContext.startActivity(new Intent(mContext, InfoActivity.class));

    }

    public void gotoCombinationUserActivity(String userId) {
        mContext.startActivity(UserHomePageActivity.getIntent(mContext, null, userId));
    }

    public void gotoFundManager(String pk) {

        if (pk == null) {
            //基金经理排行
            //基金经理
//           mContext.startActivity(FundManagerActivity.newIntent(mContext, pk ));
//            MainActivity.gotoFundManagerRanking(mContext);
            UIUtils.startAnimationActivity((Activity) mContext, MarketSubpageActivity.getIntent(mContext, MarketSubpageFragment.SubpageType.TYPE_FUND_MANAGER_RANKING_WEEK));

        } else {

            //基金经理
            mContext.startActivity(FundManagerActivity.newIntent(mContext, pk));

        }
    }

    /**
     * 跳转到研报或者是公告
     *
     * @param id
     */
    private void gotoNewOrYaoBaoDetail(String id) {
        TopicsDetailActivity.startActivity(mContext, id);
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
                PromptManager.showProgressDialog(mContext, "", true);
                super.beforeRequest();
            }

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String championBeanStr = jsonObject.getString("portfolio");
                    CombinationBean mChampionBean = DataParse.parseObjectJson(CombinationBean.class, championBeanStr);
                    mContext.startActivity(CombinationDetailActivity.newIntent(mContext, mChampionBean));
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
//        itemStock.setId(Long.parseLong(segments.get(2)));
        itemStock.setSymbol(segments.get(1));
        itemStock.setSymbol_type("1");
        itemStock.setName("- -");
        new QuotesEngineImpl().quotes(segments.get(1), new BasicHttpListener() {
            @Override
            public void beforeRequest() {
                //fixme 由于用的到 PromptManager.showProgressDialog,里面维护一个静态的进度框,所以还没走到requestCallBack 就会被关闭
                PromptManager.showProgressDialog(mContext, "", true);
                super.beforeRequest();
            }

            @Override
            public void onSuccess(String jsonData) {

                if (TextUtils.isEmpty(jsonData)) {
                    PromptManager.showCustomToast(0, R.string.null_stock);
                    return;
                }

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    JSONObject jsonOb = jsonArray.getJSONObject(0);
                    StockQuotesBean stockQuotesBean = DataParse.parseObjectJson(StockQuotesBean.class, jsonOb);

                    if (StockUitls.isFundType(stockQuotesBean.getSymbol_type())) {
                        SelectStockBean itemStock = SelectStockBean.copy(stockQuotesBean);
                        mContext.startActivity(FundDetailActivity.newIntent(mContext, itemStock));
                    } else {
                        itemStock.setSymbol_type(stockQuotesBean.getSymbol_type());
                        itemStock.setName(stockQuotesBean.getAbbrName());
                        itemStock.setId(stockQuotesBean.getId());
                        mContext.startActivity(StockQuotesActivity.newIntent(mContext, itemStock));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PromptManager.showCustomToast(0, R.string.null_stock);
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
