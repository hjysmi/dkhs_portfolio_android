package com.dkhs.portfolio.ui.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.DeleteResponeBean;
import com.dkhs.portfolio.bean.LikeBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.StatusReportActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TextModifyUtil;
import com.dkhs.portfolio.utils.UIUtils;
import com.mingle.autolist.AutoData;

/**
 * Created by zjz on 2015/7/30.
 */
public class CommentItemClick {

    public static final String TAG = "CommentItemClick";
    private String mClickUserId;
    private Context mContext;
//    private boolean isCurrentUser;
//    private CommentBean mCommentBean;

    public CommentItemClick(String userId, Context context) {
        this.mClickUserId = userId;
        this.mContext = context;
//        this.mCommentBean = bean;
    }


    public void clickFromMyReply(LikeBean bean) {
        if (isCurrentUser(bean.user.getId() + "")) {//当前用户
            showMineReplyDialog(bean);
        } else { // TA的回复
            showOtherReplyDialog(bean);
        }
    }

    public void clickFromMyReply(LikeBean bean,boolean rewarded) {
        if (isCurrentUser(bean.user.getId() + "")) {//当前用户
            showMineReplyDialog(bean,rewarded);
        } else { // TA的回复
            showOtherReplyDialog(bean);
        }
    }

    public void clickFromMyTopic(LikeBean bean) {
        if (isCurrentUser(bean.user.getId() + "")) {//当前用户
            showTopicMineReplyDialog(bean);
        } else { // TA的回复
            showTopicOtherReplyDialog(bean);
        }
    }

    private boolean isCurrentUser(String userId) {

        if (null != GlobalParams.LOGIN_USER) {
            return String.valueOf(mClickUserId).equals(userId);
        }
        return false;
    }


    private void showTopicOtherReplyDialog(final LikeBean commentBean) {

        MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
        String[] choice = null;
        if(commentBean.content_type == TopicsDetailActivity.TYPE_REWARD){
            choice = mContext.getResources().getStringArray(R.array.choices_reward_othereply);
        }else{
            choice = mContext.getResources().getStringArray(R.array.choices_topic_othereply);
        }
        dialog = dialog.setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://回复评论
                        replyComment(commentBean);
                        break;
                    case 1://复制
                        copyComment(commentBean.getText());
                        break;
                    case 2://举报
                        reportComment(commentBean);
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showTopicMineReplyDialog(final  LikeBean commentBean) {

        MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
        String[] choice = null;
        if(commentBean.content_type == TopicsDetailActivity.TYPE_REWARD){
            choice = mContext.getResources().getStringArray(R.array.choices_reward_minereply);
        }else{
            choice = mContext.getResources().getStringArray(R.array.choices_topic_minereply);
        }
        dialog = dialog.setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://回复评论
                        replyComment(commentBean);
                        break;
                    case 1://复制
                        copyComment(commentBean.text);
                        break;
                    case 2://删除
                        deleteComment(commentBean);
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showOtherReplyDialog(final LikeBean commentBean) {

        MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
        String[] choice = mContext.getResources().getStringArray(R.array.choices_other_reply);
        dialog = dialog.setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://回复评论
                        replyComment(commentBean);
                        break;
                    case 1://查看主贴
                        showMainTopic(commentBean.replied_status);
                        break;
                    case 2://复制
                        copyComment(commentBean.text);
                        break;
                    case 3://举报
                        reportComment(commentBean);
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showMineReplyDialog(final LikeBean commentBean) {
        this.showMineReplyDialog(commentBean,false);
    }

    private void showMineReplyDialog(final LikeBean commentBean,boolean rewarded) {
        MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
        String[] choice = null;
        if(commentBean.content_type == TopicsDetailActivity.TYPE_REWARD){
            choice = mContext.getResources().getStringArray(rewarded? R.array.reward_reply_op_rewarded:R.array.reward_reply_op);
        }else{
            choice = mContext.getResources().getStringArray(R.array.choices_mine_reply);
        }
        dialog = dialog.setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://查看主贴
                        replyComment(commentBean);
                        break;
                    case 1://查看主贴
                        showMainTopic(commentBean.replied_status);
                        break;
                    case 2://复制内容
                        copyComment(commentBean.text);
                        break;
                    case 3://删除回复
                        deleteComment(commentBean);
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    /**
     * 回复评论
     */
    private void replyComment(LikeBean commentBean) {

        if (UIUtils.iStartLoginActivity(mContext)) {
            return;
        }


        if (null != commentBean.user) {
            if(commentBean.content_type == TopicsDetailActivity.TYPE_REWARD){
                mContext.startActivity(PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_REPLY_REWARD, commentBean.getId() + "", commentBean.user.getUsername()));
            }else{
                mContext.startActivity(PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_REPLY_TOPIC, commentBean.getId() + "", commentBean.user.getUsername()));
            }
        } else {
            Log.e(TAG, "comment user is null;");
        }

    }

    /**
     * 复制内容
     */
    private void copyComment(String commentText) {
        TextModifyUtil.copyToClipboard(commentText, mContext);
        PromptManager.showToast(R.string.msg_copy_content_success);
    }

    /**
     * 查看主贴
     */
    private void showMainTopic(String topicId) {
        TopicsBean topicsBean = new TopicsBean();
        topicsBean.id = Integer.valueOf(topicId);
        TopicsDetailActivity.startActivity(mContext, topicsBean);

    }

    /**
     * 删除回复
     */
    private void deleteComment(final LikeBean comment) {


        PromptManager.getAlertDialog(mContext).setMessage(mContext.getString(R.string.delete_comment)).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                postDeleteCommnent(comment);
            }
        }).setNegativeButton(R.string.cancel, null).show();


    }


    private void postDeleteCommnent(final LikeBean comment) {
        StatusEngineImpl.delete(comment.getId()+"", new ParseHttpListener<Boolean>() {
            @Override
            protected Boolean parseDateTask(String jsonData) {
                DeleteResponeBean reponseBean = DataParse.parseObjectJson(DeleteResponeBean.class, jsonData);
                return reponseBean.isStatus();
            }

            @Override
            protected void afterParseData(Boolean object) {
                if (object) {
                    PromptManager.showCancelToast(R.string.msg_del_contetn_success);
//                    DeleteCommentEvent deleteCommentEvent = new DeleteCommentEvent();
//                    deleteCommentEvent.commentId = comment.getId()+"";
//                    BusProvider.getInstance().post(deleteCommentEvent);
                    comment.appleAction(this, AutoData.Action.Delete).post();



                }

            }
        });
    }


    /**
     * 举报回复
     */
    private void reportComment(LikeBean commentBean) {
        if (!UIUtils.iStartLoginActivity(mContext))
            mContext.startActivity(StatusReportActivity.getIntent(mContext, commentBean.getId() + "", commentBean.user.getUsername(), commentBean.text,commentBean.content_type));
    }


}
