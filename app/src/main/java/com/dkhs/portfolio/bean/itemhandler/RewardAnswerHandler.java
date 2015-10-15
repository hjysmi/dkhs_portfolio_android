package com.dkhs.portfolio.bean.itemhandler;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.listener.RewardReplyItemClick;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.ui.widget.SwitchLikeStateHandler;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.mingle.bean.PhotoBean;

import java.util.ArrayList;


/**
 * @author zwm
 * @version 2.0
 * @ClassName CommendHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class RewardAnswerHandler extends SimpleItemHandler<LikeBean> {

    private boolean mAvatarImResponse = true;
    private boolean isReplyComment = false;
    private boolean mCompact = false;

    private Context mContext;
    private long mRewardUserId;
    private int mRewardState;

    public RewardAnswerHandler(Context context, boolean avatarImResponse) {
        this(context, avatarImResponse, false);
    }


    public RewardAnswerHandler(Context context) {
        this(context, true);
    }

    public RewardAnswerHandler(Context context, boolean avatarImResponse, boolean compact) {
        mAvatarImResponse = avatarImResponse;
        mCompact = compact;
        mContext = context;
    }

    public void setRewardUserId(long userId){
        mRewardUserId = userId;
    }


    public void setRewardState(int state){
        mRewardState = state;
    }

    public RewardAnswerHandler setReplyComment(boolean isReplyComment) {
        this.isReplyComment = isReplyComment;
        return this;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_reward_reply;
    }

    @Override
    public void onBindView(ViewHolder vh, final LikeBean comment, int position) {
        super.onBindView(vh, comment, position);
        long replyUserId = comment.getUser().getId();
        if(isShowAdopt(mRewardUserId,replyUserId,mRewardState)){
            vh.getTextView(R.id.tv_adopt).setVisibility(View.VISIBLE);
            vh.get(R.id.view_divider).setVisibility(View.VISIBLE);
        }else{
            vh.getTextView(R.id.tv_adopt).setVisibility(View.GONE);
            vh.get(R.id.view_divider).setVisibility(View.GONE);
        }
        PeopleBean user = comment.user;
        if(comment instanceof CommentBean){
            CommentBean cb = (CommentBean)comment;
            if(cb.rewarded_type == 1){
                vh.getImageView(R.id.iv_rewarded).setVisibility(View.VISIBLE);
            }else{
                vh.getImageView(R.id.iv_rewarded).setVisibility(View.GONE);
            }
        }else{
            vh.getImageView(R.id.iv_rewarded).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(user.getAvatar_sm())) {
            ImageLoaderUtils.setHeanderImage(comment.user.getAvatar_sm(), vh.getImageView(R.id.iv_head));
        } else {
            vh.getImageView(R.id.iv_head).setImageResource(R.drawable.default_head);
        }
        vh.getTextView(R.id.tv_username).setText(user.getUsername());
        vh.getTextView(R.id.tv_text).setText(comment.text);
        ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText(String.valueOf(comment.attitudes_count));
        vh.getTextView(R.id.tv_time).setText(TimeUtils.getBriefTimeString(comment.created_at));
        setClickListener(vh.get(R.id.like_ll), comment);
        setClickListener(vh.get(R.id.tv_username), comment);
        setClickListener(vh.get(R.id.iv_image), comment);
        if (comment.attitudes_count > 0) {
            ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText(StringFromatUtils.handleNumber(comment.attitudes_count));
        } else {
            ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText("");
        }


        if (comment.medias != null && comment.medias.size() > 0) {
            vh.get(R.id.iv_image).setVisibility(View.VISIBLE);
            ImageLoaderUtils.setImagDefault(comment.medias.get(0).getImage_sm(), vh.getImageView(R.id.iv_image));
        } else {
            vh.get(R.id.iv_image).setVisibility(View.GONE);
        }


        if (comment.like) {
            vh.getImageView(R.id.iv_praise).setImageResource(R.drawable.praised);
        } else {
            vh.getImageView(R.id.iv_praise).setImageResource(R.drawable.praise);
        }

        if (mAvatarImResponse) {
            setClickListener(vh.get(R.id.iv_head), comment);
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
                if (isReplyComment) {
                    mCommentClick.clickFromMyReply(comment,((CommentBean)comment).rewarded_type == 1?true:false);
                } else {

                    mCommentClick.clickFromMyTopic(comment);
                }
            }
        });


        if (comment.compact || mCompact) {
            vh.get(R.id.bottom).setVisibility(View.GONE);
        } else {
            vh.get(R.id.bottom).setVisibility(View.VISIBLE);
        }

        vh.getTextView(R.id.tv_adopt).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showAdoptDialog(comment);
            }
        });

    }

    public void setClickListener(View view, LikeBean data) {
        ItemHandlerClickListenerImp<LikeBean> itemHandlerClickListener = null;
        if (null != view.getTag() && view.getTag() instanceof ItemHandlerClickListenerImp) {
            itemHandlerClickListener = (ItemHandlerClickListenerImp<LikeBean>) view.getTag();
        } else {

            switch (view.getId()) {
                case R.id.like_ll:
                    itemHandlerClickListener = new LikeClickListenerImp();
                    break;
                case R.id.iv_head:
                case R.id.tv_username:
                    itemHandlerClickListener = new AvatarClickListenerImp();
                    break;
                case R.id.iv_image:
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
            ImageView imageView = (ImageView) v.findViewById(R.id.iv_praise);
            mSwitchLikeStateHandler.setStatusChangeI(this);
            mSwitchLikeStateHandler.attachLikeImage(imageView);
            mSwitchLikeStateHandler.toggleLikeState();
        }

        @Override
        public void likePre() {
            mLikeBean.setAttitudes_count(mLikeBean.getAttitudes_count() + 1);
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.tv_like);
            if (mLikeBean.getAttitudes_count() > 0) {

                likeTV.setText(StringFromatUtils.handleNumber(mLikeBean.getAttitudes_count()));
            } else {
                likeTV.setText("");
            }
        }

        @Override
        public void unLikePre() {
            mLikeBean.setAttitudes_count(mLikeBean.getAttitudes_count() - 1);
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.tv_like);
            if (mLikeBean.getAttitudes_count() > 0) {

                likeTV.setText(StringFromatUtils.handleNumber(mLikeBean.getAttitudes_count()));
            } else {
                likeTV.setText("");
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

    private boolean isShowAdopt(long rewardUserId,long replyUserId,int rewardState){
        if(rewardUserId == replyUserId){
            return false;
        }
        if(rewardState != 0 ){
            return false;
        }
        if(GlobalParams.LOGIN_USER == null ||rewardUserId != GlobalParams.LOGIN_USER.getId()){
            return false;
        }
        return true;
    }
    private void showAdoptDialog(LikeBean comment) {
        MAlertDialog builder = PromptManager.getAlertDialog(mContext);
        final int commentId =  comment.getId();
        builder.setMessage(mContext.getString(R.string.msg_adopt_reply)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatusEngineImpl.adoptReply(String.valueOf(commentId), new ParseHttpListener() {

                    @Override
                    protected Object parseDateTask(String jsonData) {
                        return null;
                    }

                    @Override
                    protected void afterParseData(Object object) {
                        //TODO:提示界面更新
                    }
                });
                dialog.dismiss();
            }
        }).setNegativeButton("取消",null);
        builder.show();
    }

}
