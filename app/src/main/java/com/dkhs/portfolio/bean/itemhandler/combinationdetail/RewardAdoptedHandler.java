package com.dkhs.portfolio.bean.itemhandler.combinationdetail;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextSwitcher;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.listener.RewardReplyItemClick;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class RewardAdoptedHandler extends SimpleItemHandler<CommentBean> {


    private Context mContext;

    public RewardAdoptedHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_adopt_reply;
    }


    @Override
    public void onBindView(final ViewHolder vh, final CommentBean comment, int position) {
        PeopleBean user = comment.getUser();
        if (!TextUtils.isEmpty(user.getAvatar_sm())) {
            ImageLoaderUtils.setHeanderImage(comment.user.getAvatar_sm(), vh.getImageView(R.id.adopt_iv_head));
        } else {
            vh.getImageView(R.id.adopt_iv_head).setImageResource(R.drawable.default_head);
        }
        vh.getTextView(R.id.adopt_tv_username).setText(user.getUsername());
        vh.getTextView(R.id.adopt_tv_text).setText(comment.text);
        ((TextSwitcher) vh.get(R.id.adopt_tv_like)).setCurrentText(String.valueOf(comment.attitudes_count));
        vh.getTextView(R.id.adopt_tv_time).setText(TimeUtils.getBriefTimeString(comment.created_at));
//        setClickListener(vh.get(R.id.like_ll), comment);
//        setClickListener(vh.get(R.id.tv_username), comment);
//        setClickListener(vh.get(R.id.iv_image), comment);
        if (comment.attitudes_count > 0) {
            ((TextSwitcher) vh.get(R.id.adopt_tv_like)).setCurrentText(StringFromatUtils.handleNumber(comment.attitudes_count));
        } else {
            ((TextSwitcher) vh.get(R.id.adopt_tv_like)).setCurrentText("");
        }


        if (comment.medias != null && comment.medias.size() > 0) {
            vh.get(R.id.adopt_iv_image).setVisibility(View.VISIBLE);
            ImageLoaderUtils.setImagDefault(comment.medias.get(0).getImage_sm(), vh.getImageView(R.id.iv_image));
        } else {
            vh.get(R.id.adopt_iv_image).setVisibility(View.GONE);
        }


        if (comment.like) {
            vh.getImageView(R.id.adopt_iv_praise).setImageResource(R.drawable.praised);
        } else {
            vh.getImageView(R.id.adopt_iv_praise).setImageResource(R.drawable.praise);
        }

/*        if (mAvatarImResponse) {
            setClickListener(vh.get(R.id.iv_head), comment);
        }*/
        vh.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardReplyItemClick mCommentClick;
                if (null != GlobalParams.LOGIN_USER) {
                    mCommentClick = new RewardReplyItemClick(GlobalParams.LOGIN_USER.getId() + "", v.getContext());
                } else {
                    mCommentClick = new RewardReplyItemClick("", v.getContext());
                }
                mCommentClick.clickFromMyTopic(comment);
            }
        });

        ViewHelper.setTranslationX(vh.get(R.id.indicate), vh.getTextView(R.id.adopt_tv_hint).getLeft() + vh.getTextView(R.id.adopt_tv_hint).getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2);

    }


}