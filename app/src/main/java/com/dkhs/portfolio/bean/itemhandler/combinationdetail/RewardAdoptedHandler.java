package com.dkhs.portfolio.bean.itemhandler.combinationdetail;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.LikeBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.listener.RewardReplyItemClick;
import com.dkhs.portfolio.ui.widget.SwitchLikeStateHandler;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.mingle.bean.PhotoBean;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class RewardAdoptedHandler extends SimpleItemHandler<CommentBean> {


    private Context mContext;
    private boolean mAvatarImResponse;
    private boolean mCompact;

    public RewardAdoptedHandler(Context context) {
        mContext = context;
    }

    public RewardAdoptedHandler(Context context, boolean avatarImResponse, boolean compact) {
        mAvatarImResponse = avatarImResponse;
        mCompact = compact;
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
        setClickListener(vh.get(R.id.adopt_like_ll), comment);
        setClickListener(vh.get(R.id.adopt_tv_username), comment);
        setClickListener(vh.get(R.id.adopt_iv_image), comment);
        if (comment.attitudes_count > 0) {
            ((TextSwitcher) vh.get(R.id.adopt_tv_like)).setCurrentText(StringFromatUtils.handleNumber(comment.attitudes_count));
            ((TextSwitcher) vh.get(R.id.adopt_tv_like)).setVisibility(View.VISIBLE);
        } else {
            ((TextSwitcher) vh.get(R.id.adopt_tv_like)).setCurrentText("");
            ((TextSwitcher) vh.get(R.id.adopt_tv_like)).setVisibility(View.GONE);
        }


        if (comment.medias != null && comment.medias.size() > 0) {
            vh.get(R.id.adopt_iv_image).setVisibility(View.VISIBLE);
            ImageLoaderUtils.setImagDefault(comment.medias.get(0).getImage_sm(), vh.getImageView(R.id.adopt_iv_image));
        } else {
            vh.get(R.id.adopt_iv_image).setVisibility(View.GONE);
        }


        if (comment.like) {
            vh.getImageView(R.id.adopt_iv_praise).setImageResource(R.drawable.praised);
        } else {
            vh.getImageView(R.id.adopt_iv_praise).setImageResource(R.drawable.praise);
        }

        if (mAvatarImResponse) {
            setClickListener(vh.get(R.id.adopt_iv_head), comment);
        }
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

        vh.getTextView(R.id.adopt_tv_hint).post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationX(vh.get(R.id.indicate), vh.getTextView(R.id.adopt_tv_hint).getLeft() + vh.getTextView(R.id.adopt_tv_hint).getWidth() / 2 - vh.get(R.id.indicate).getWidth() / 2);
            }
        });

    }

    public void setClickListener(View view, LikeBean data) {
        ItemHandlerClickListenerImp<LikeBean> itemHandlerClickListener = null;
        if (null != view.getTag() && view.getTag() instanceof ItemHandlerClickListenerImp) {
            itemHandlerClickListener = (ItemHandlerClickListenerImp<LikeBean>) view.getTag();
        } else {

            switch (view.getId()) {
                case R.id.adopt_like_ll:
                    itemHandlerClickListener = new LikeClickListenerImp();
                    break;
                case R.id.adopt_iv_head:
                case R.id.adopt_tv_username:
                    itemHandlerClickListener = new AvatarClickListenerImp();
                    break;
                case R.id.adopt_iv_image:
                    itemHandlerClickListener = new ImageViewClickListenerImp();
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

    class LikeClickListenerImp extends ItemHandlerClickListenerImp<LikeBean> implements SwitchLikeStateHandler.StatusChangeI {

        private SwitchLikeStateHandler mSwitchLikeStateHandler;
        private View mView;

        private LikeBean mLikeBean;

        @Override
        public View.OnClickListener setDate(LikeBean o) {
            this.mLikeBean = o;
            mSwitchLikeStateHandler = new SwitchLikeStateHandler(mLikeBean);
            return this;
        }


        @Override
        public void onClick(View v) {
            mView = v;
            ImageView imageView = (ImageView) v.findViewById(R.id.adopt_iv_praise);
            mSwitchLikeStateHandler.setStatusChangeI(this);
            mSwitchLikeStateHandler.attachLikeImage(imageView);
            mSwitchLikeStateHandler.toggleLikeState();
        }

        @Override
        public void likePre() {
            mLikeBean.setAttitudes_count(mLikeBean.getAttitudes_count() + 1);
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.adopt_tv_like);
            if (mLikeBean.getAttitudes_count() > 0) {

                likeTV.setText(StringFromatUtils.handleNumber(mLikeBean.getAttitudes_count()));
                likeTV.setVisibility(View.VISIBLE);
            } else {
                likeTV.setText("");
                likeTV.setVisibility(View.GONE);
            }
        }

        @Override
        public void unLikePre() {
            mLikeBean.setAttitudes_count(mLikeBean.getAttitudes_count() - 1);
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.adopt_tv_like);
            if (mLikeBean.getAttitudes_count() > 0) {

                likeTV.setText(StringFromatUtils.handleNumber(mLikeBean.getAttitudes_count()));
                likeTV.setVisibility(View.VISIBLE);
            } else {
                likeTV.setText("");
                likeTV.setVisibility(View.GONE);
            }

        }
    }

    class AvatarClickListenerImp extends ItemHandlerClickListenerImp<LikeBean> {


        private LikeBean mLikeBean;


        @Override
        public View.OnClickListener setDate(LikeBean o) {
            this.mLikeBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            if (mLikeBean.getUser() != null) {
                UIUtils.startAnimationActivity((Activity) mContext,
                        UserHomePageActivity.getIntent(mContext, mLikeBean.getUser().getUsername(), mLikeBean.getUser().getId() + ""));
            }

        }
    }

    class ImageViewClickListenerImp extends ItemHandlerClickListenerImp<LikeBean> {
        private LikeBean topicsBean;

        @Override
        public View.OnClickListener setDate(LikeBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            ArrayList<PhotoBean> arrayList = new ArrayList<>();
            PhotoBean photoBean = new PhotoBean();
            photoBean.title = topicsBean.id + "";
            photoBean.loadingURl = topicsBean.getMedias().get(0).getImage_sm();
            photoBean.imgUrl = topicsBean.getMedias().get(0).getImage_md();
            arrayList.add(photoBean);
            PhotoViewActivity.startPhotoViewActivity(mContext, arrayList, v, 0);
        }
    }
}