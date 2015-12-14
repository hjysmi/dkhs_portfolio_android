package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

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
import com.dkhs.portfolio.bean.LikeBean;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RewardDetailRefreshEvent;
import com.dkhs.portfolio.ui.listener.CommentItemClick;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.ui.widget.SwitchLikeStateHandler;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.portfolio.utils.WaterMarkUtil;
import com.mingle.bean.PhotoBean;

import java.util.ArrayList;


/**
 * @author zwm
 * @version 2.0
 * @ClassName CommendHandler
 * @Description 用于显示话题回复及悬赏回复
 * @date 2015/7/28.
 */
public class CommentHandler extends SimpleItemHandler<LikeBean> {

    private boolean mAvatarImResponse = true;
    private boolean isReplyComment = false;
    private boolean mCompact = false;

    private Context mContext;
    private long mRewardUserId;
    private int mRewardState;
    /**
     * 用于区分悬赏详情(不显示图片)　和　我参与的（显示图片）　场景
     * @param isShowRewardStatus
     */
    private boolean isShowRewardStatus = true;

    public CommentHandler(Context context, boolean avatarImResponse) {
        this(context, avatarImResponse, false);
    }


    public CommentHandler(Context context) {
        this(context, true);
    }

    public CommentHandler(Context context, boolean avatarImResponse, boolean compact) {
        this(context, avatarImResponse, compact,true);
    }

    public CommentHandler(Context context, boolean avatarImResponse, boolean compact,boolean showRewardStatus) {
        mAvatarImResponse = avatarImResponse;
        mCompact = compact;
        mContext = context;
        isShowRewardStatus = showRewardStatus;
    }


    public void setIsShowRewardStatus(boolean isShowRewardStatus) {
        this.isShowRewardStatus = isShowRewardStatus;
    }

    public void setRewardUserId(long userId){
        mRewardUserId = userId;
    }


    public void setRewardState(int state){
        mRewardState = state;
    }

    public CommentHandler setReplyComment(boolean isReplyComment) {
        this.isReplyComment = isReplyComment;
        return this;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_reply;
    }

    @Override
    public void onBindView(ViewHolder vh, final LikeBean comment, int position) {
        super.onBindView(vh, comment, position);
        long replyUserId = -1;
        if(comment !=null && comment.getUser() != null){
            replyUserId = comment.getUser().getId();
        }
        //判断是否显示采纳按钮
        if(isShowAdopt(comment.content_type,replyUserId)){
            vh.getTextView(R.id.tv_adopt).setVisibility(View.VISIBLE);
            vh.get(R.id.view_divider).setVisibility(View.VISIBLE);
        }else{
            vh.getTextView(R.id.tv_adopt).setVisibility(View.GONE);
            vh.get(R.id.view_divider).setVisibility(View.GONE);
        }
        //显示蓝色悬赏图片
        if(comment.rewarded_type == 1 && isShowRewardStatus){
            vh.getImageView(R.id.iv_rewarded).setVisibility(View.VISIBLE);
        }else{
            vh.getImageView(R.id.iv_rewarded).setVisibility(View.GONE);
        }

        PeopleBean user = comment.user;
        if (user !=null && !TextUtils.isEmpty(user.getAvatar_sm())) {
            ImageLoaderUtils.setHeanderImage(comment.user.getAvatar_sm(), vh.getImageView(R.id.iv_avatar));
        } else {
            vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.default_head);
        }
        WaterMarkUtil.calWaterMarkImage(vh.getImageView(R.id.iv_water_mark), user.verified, user.verified_type == UserEntity.VERIFIEDTYPE.EXPERT.getTypeid() ? WaterMarkUtil.TYPE_RED : WaterMarkUtil.TYPE_BLUE);
        if(user != null){
            vh.getTextView(R.id.tv_username).setText(user.getUsername());
        }
        vh.getTextView(R.id.tv_text).setText(comment.text);
        ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText(String.valueOf(comment.attitudes_count));
        vh.getTextView(R.id.tv_time).setText(TimeUtils.getBriefTimeString(comment.created_at));
        setClickListener(vh.get(R.id.like_ll), comment);
        setClickListener(vh.get(R.id.tv_username), comment);
        setClickListener(vh.get(R.id.iv_image), comment);
        if (comment.attitudes_count > 0) {
            ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText(StringFromatUtils.handleNumber(comment.attitudes_count));
            vh.get(R.id.tv_like).setVisibility(View.VISIBLE);
        } else {
            ((TextSwitcher) vh.get(R.id.tv_like)).setCurrentText("");
            vh.get(R.id.tv_like).setVisibility(View.GONE);
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
            setClickListener(vh.get(R.id.iv_avatar), comment);
        }
        vh.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LikeBean bean = comment;
//                if(bean.content_type == 0){
                    showCommentDialog(v, comment);
//                }else{
//                    showRewardReplyDialog(v,bean);
//                }
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
    private void showCommentDialog(View v, LikeBean comment) {
        CommentItemClick mCommentClick;
        if (null != GlobalParams.LOGIN_USER) {
            mCommentClick = new CommentItemClick(GlobalParams.LOGIN_USER.getId() + "", v.getContext());
        } else {
            mCommentClick = new CommentItemClick("", v.getContext());
        }
        if (isReplyComment) {
            mCommentClick.clickFromMyReply(comment,comment.rewarded_type == 1);
        } else {

            mCommentClick.clickFromMyTopic(comment);
        }
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
                case R.id.iv_avatar:
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
                likeTV.setVisibility(View.VISIBLE);
                likeTV.setText(StringFromatUtils.handleNumber(mLikeBean.getAttitudes_count()));
            } else {
                likeTV.setText("");
                likeTV.setVisibility(View.GONE);
            }
        }

        @Override
        public void unLikePre() {
            mLikeBean.setAttitudes_count(mLikeBean.getAttitudes_count() - 1);
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.tv_like);
            if (mLikeBean.getAttitudes_count() > 0) {
                likeTV.setVisibility(View.VISIBLE);
                likeTV.setText(StringFromatUtils.handleNumber(mLikeBean.getAttitudes_count()));
            } else {
                likeTV.setText("");
                likeTV.setVisibility(View.GONE);
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

    private boolean isShowAdopt(int contentType,long replyUserId){
        if(contentType != TopicsDetailActivity.TYPE_REWARD){
            return false;
        }
        if(mRewardUserId == replyUserId){
            return false;
        }
        if(mRewardState != 0 ){
            return false;
        }
        if(GlobalParams.LOGIN_USER == null ||mRewardUserId != GlobalParams.LOGIN_USER.getId()){
            return false;
        }
        return true;
    }
    private void showAdoptDialog(LikeBean comment) {
        MAlertDialog builder = PromptManager.getAlertDialog(mContext);
        final int commentId =  comment.getId();
        builder.setMessage(mContext.getString(R.string.msg_adopt_reply)).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
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
                        PromptManager.showToast(R.string.reward_success);
                        BusProvider.getInstance().post(new RewardDetailRefreshEvent(0));
                    }
                }.setLoadingDialog(mContext,false));
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel,null);
        builder.show();
    }

}
