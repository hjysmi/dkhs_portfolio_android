package com.dkhs.portfolio.ui.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.StatusReportActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TextModifyUtil;

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


    public void clickFromMyReply(CommentBean bean) {
        if (isCurrentUser(bean.getUser().getId() + "")) {//当前用户
            showMineReplyDialog(bean);
        } else { // TA的回复
            showOtherReplyDialog(bean);
        }
    }

    public void clickFromMyTopic(CommentBean bean) {
        if (isCurrentUser(bean.getUser().getId() + "")) {//当前用户
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


    private void showTopicOtherReplyDialog(final CommentBean commentBean) {

        MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
        String[] choice = mContext.getResources().getStringArray(R.array.choices_topic_othereply);
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

    private void showTopicMineReplyDialog(final CommentBean commentBean) {

        MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
        String[] choice = mContext.getResources().getStringArray(R.array.choices_topic_minereply);
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
                    case 2://删除
                        deleteComment(commentBean.getId());
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showOtherReplyDialog(final CommentBean commentBean) {

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
                        showMainTopic(commentBean.getReplied_status());
                        break;
                    case 2://复制
                        copyComment(commentBean.getText());
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

    private void showMineReplyDialog(final CommentBean commentBean) {
        MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
        String[] choice = mContext.getResources().getStringArray(R.array.choices_mine_reply);
        dialog = dialog.setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://查看主贴
                        showMainTopic(commentBean.getReplied_status());
                        break;
                    case 1://复制内容
                        copyComment(commentBean.getText());
                        break;
                    case 2://删除回复
                        deleteComment(commentBean.getId());
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
    private void replyComment(CommentBean commentBean) {
        if (null != commentBean.getUser()) {
            mContext.startActivity(PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_RETWEET, commentBean.getId(), commentBean.getUser().getUsername()));
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
    private void deleteComment(String commentId) {

//        PromptManager.showToast("删除回复");
        StatusEngineImpl.delete(commentId, new ParseHttpListener() {
            @Override
            protected Object parseDateTask(String jsonData) {
                return null;
            }

            @Override
            protected void afterParseData(Object object) {
                PromptManager.showCancelToast(R.string.msg_del_contetn_success);
            }
        });
    }


    /**
     * 举报回复
     */
    private void reportComment(CommentBean commentBean) {
        mContext.startActivity(StatusReportActivity.getIntent(mContext, commentBean.getId(), commentBean.getUser().getUsername(), commentBean.getText()));
    }


}
