package com.dkhs.portfolio.ui.listener;

import android.content.Context;
import android.content.DialogInterface;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.StatusReportActivity;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.TextModifyUtil;

/**
 * Created by zjz on 2015/7/30.
 */
public class CommentItemClick {

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


//    private MAlertDialog mDialog;

    private void showTopicOtherReplyDialog(final CommentBean commentBean) {

        MAlertDialog dialog = PromptManager.getAlertDialog(mContext);
        String[] choice = mContext.getResources().getStringArray(R.array.choices_topic_othereply);
        dialog = dialog.setSingleChoiceItems(choice, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://回复评论
                        //
                        break;
                    case 1://复制
                        TextModifyUtil.copyToClipboard(commentBean.getText(), mContext);
                        PromptManager.showToast(R.string.msg_copy_content_success);
                        break;
                    case 2://举报
                        mContext.startActivity(StatusReportActivity.getIntent(mContext, commentBean.getId(), commentBean.getUser().getUsername(), commentBean.getText()));
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
                        //
                        break;
                    case 1://复制
                        TextModifyUtil.copyToClipboard(commentBean.getText(), mContext);
                        PromptManager.showToast(R.string.msg_copy_content_success);
                        break;
                    case 2://删除
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
                        //
                        break;
                    case 1://查看主贴
                        break;
                    case 2://复制
                        TextModifyUtil.copyToClipboard(commentBean.getText(), mContext);
                        PromptManager.showToast(R.string.msg_copy_content_success);
                        break;
                    case 3://举报
                        mContext.startActivity(StatusReportActivity.getIntent(mContext, commentBean.getId(), commentBean.getUser().getUsername(), commentBean.getText()));
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
                        //
                        break;
                    case 1://复制内容
                        TextModifyUtil.copyToClipboard(commentBean.getText(), mContext);
                        PromptManager.showToast(R.string.msg_copy_content_success);
                        break;
                    case 2://删除回复
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


//    private void


}
