package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.listener.CommentItemClick;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.ui.widget.SwitchLikeStateHandler;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;


/**
 * @author zwm
 * @version 2.0
 * @ClassName CommendHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class CommentHandler extends SimpleItemHandler<CommentBean> {

    private boolean mAvatarImResponse = true;

    public CommentHandler(boolean avatarImResponse) {
        mAvatarImResponse = avatarImResponse;
    }

    public CommentHandler() {
        this(true);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_reply;
    }

    @Override
    public void onBindView(ViewHolder vh, final CommentBean comment, int position) {
        super.onBindView(vh, comment, position);
        PeopleBean user = comment.getUser();
        if (!TextUtils.isEmpty(user.getAvatar_sm())) {
            ImageLoaderUtils.setHeanderImage(comment.getUser().getAvatar_sm(), vh.getImageView(R.id.iv_head));
        }else{
            vh.getImageView(R.id.iv_head).setImageResource(R.drawable.default_head);
        }
        vh.getTextView(R.id.tv_username).setText(user.getUsername());
        vh.getTextView(R.id.tv_text).setText(comment.getText());
        ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText(String.valueOf(comment.getAttitudes_count()));
        vh.getTextView(R.id.tv_time).setText(TimeUtils.getBriefTimeString(comment.getCreated_at()));
        setClickListener(vh.get(R.id.like_ll), comment);
        setClickListener(vh.get(R.id.tv_username), comment);
        if (comment.getAttitudes_count() > 0) {
            ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText(StringFromatUtils.handleNumber(comment.getAttitudes_count()));
        } else {
            ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText("");
        }

        if(comment.like){
            vh.getImageView(R.id.iv_praise).setImageResource(R.drawable.ic_like);
        }else{
            vh.getImageView(R.id.iv_praise).setImageResource(R.drawable.ic_unlike);
        }

        if (mAvatarImResponse) {
            setClickListener(vh.get(R.id.iv_head), comment);
        }
        vh.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentItemClick mCommentClick;
                if (null != GlobalParams.LOGIN_USER) {
                    mCommentClick = new CommentItemClick(GlobalParams.LOGIN_USER.getId() + "", v.getContext());
                } else {
                    mCommentClick = new CommentItemClick("", v.getContext());
                }
                mCommentClick.clickFromMyTopic(comment);
            }
        });


        if (comment.compact) {
            vh.get(R.id.bottom).setVisibility(View.GONE);
        } else {
            vh.get(R.id.bottom).setVisibility(View.VISIBLE);
        }


    }

    public void setClickListener(View view, CommentBean data) {
        ItemHandlerClickListenerImp<CommentBean> itemHandlerClickListener = null;
        if (null != view.getTag() && view.getTag() instanceof ItemHandlerClickListenerImp) {
            itemHandlerClickListener = (ItemHandlerClickListenerImp<CommentBean>) view.getTag();
        } else {

            switch (view.getId()) {
                case R.id.like_ll:
                    itemHandlerClickListener = new LikeClickListenerImp();
                    break;
                case R.id.iv_head:
                case R.id.tv_username:
                    itemHandlerClickListener = new AvatarClickListenerImp();
                    break;
                default:
                    itemHandlerClickListener = new LikeClickListenerImp();
                    break;
            }
            view.setOnClickListener(itemHandlerClickListener);
            view.setTag(itemHandlerClickListener);
        }
        itemHandlerClickListener.setDate(data);
    }

    class AvatarClickListenerImp extends ItemHandlerClickListenerImp<CommentBean> {


        private CommentBean mCommentBean;


        @Override
        public View.OnClickListener setDate(CommentBean o) {
            this.mCommentBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            if (mCommentBean.getUser() != null) {
                UIUtils.startAnimationActivity((Activity) mContext,
                        UserHomePageActivity.getIntent(mContext, mCommentBean.getUser().getUsername(), mCommentBean.getUser().getId() + ""));
            }

        }
    }


    class LikeClickListenerImp extends ItemHandlerClickListenerImp<CommentBean> implements SwitchLikeStateHandler.StatusChangeI {

        private SwitchLikeStateHandler mSwitchLikeStateHandler;
        private View mView;

        private CommentBean mCommentBean;

        @Override
        public View.OnClickListener setDate(CommentBean o) {
            this.mCommentBean = o;
            mSwitchLikeStateHandler = new SwitchLikeStateHandler(mCommentBean);
            return this;
        }


        @Override
        public void onClick(View v) {
            mView = v;
            ImageView imageView = (ImageView) v.findViewById(R.id.iv_praise);
            mSwitchLikeStateHandler.setStatusChangeI(this);
            mSwitchLikeStateHandler.attachLikeImage(imageView);
            mSwitchLikeStateHandler.toggleLikeState();
        }

        @Override
        public void likePre() {
            mCommentBean.setAttitudes_count(mCommentBean.getAttitudes_count() + 1);
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.tv_like);
            if (mCommentBean.getAttitudes_count() > 0) {

                likeTV.setText(StringFromatUtils.handleNumber(mCommentBean.getAttitudes_count()));
            } else {
                likeTV.setText("");
            }
        }

        @Override
        public void unLikePre() {
            mCommentBean.setAttitudes_count(mCommentBean.getAttitudes_count() - 1);
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.tv_like);
            if (mCommentBean.getAttitudes_count() > 0) {

                likeTV.setText(StringFromatUtils.handleNumber(mCommentBean.getAttitudes_count()));
            } else {
                likeTV.setText("");
            }

        }
    }
}
