package com.dkhs.portfolio.bean.itemhandler;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.LikeBean;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.ui.widget.SwitchLikeStateHandler;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.mingle.bean.PhotoBean;

import java.util.ArrayList;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class TopicsHandler extends SimpleItemHandler<LikeBean> {

    private Context mContext;
    private boolean mAvatarImResponse = true;


    public TopicsHandler(Context context) {

        this(context, true);
    }

    public TopicsHandler(Context context, boolean avatarImResponse) {
        mContext = context;
        mAvatarImResponse = avatarImResponse;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_topics;
    }

    @Override
    public void onBindView(ViewHolder vh, final LikeBean data, int position) {
        setClickListener(vh.get(R.id.fl_commend), data);

        if (mAvatarImResponse) {
            setClickListener(vh.get(R.id.iv_avatar), data);
        }
//        setClickListener(vh.get(R.id.iv), data);
        setClickListener(vh.get(R.id.main_ll), data);
        setClickListener(vh.get(R.id.fl_star), data);
        setClickListener(vh.get(R.id.name), data);

        vh.setTextView(R.id.tv_time, TimeUtils.getBriefTimeString(data.created_at));
        if (TextUtils.isEmpty(data.title)||data.title.equals("发表悬赏")) {
            vh.get(R.id.titleTV).setVisibility(View.GONE);
        } else {
            vh.get(R.id.titleTV).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.titleTV, data.title);
        }
        if (data.user != null && !TextUtils.isEmpty(data.user.getAvatar_md())) {
            ImageLoaderUtils.setHeanderImage(data.user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
        } else {
            vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_user_head);
        }


        if (TextUtils.isEmpty(data.text)) {
            vh.get(R.id.content).setVisibility(View.GONE);
        } else {
            vh.get(R.id.content).setVisibility(View.VISIBLE);
        }
        vh.setTextView(R.id.content, data.text);
        if (null != data.user) {

            vh.setTextView(R.id.name, data.user.getUsername());
        } else {
            vh.setTextView(R.id.name, "匿名用户");
        }


        TopicsImageViewHandler topicsImageViewHandler = (TopicsImageViewHandler) vh.get(R.id.content).getTag();


        if (topicsImageViewHandler == null) {
            topicsImageViewHandler = new TopicsImageViewHandler();
            vh.get(R.id.content).setTag(topicsImageViewHandler);
        }

        topicsImageViewHandler.handleMedias(vh, data, false);


        TextSwitcher textSwitcher = vh.get(R.id.tv_like);
        if (data.attitudes_count > 0) {

            textSwitcher.setCurrentText(StringFromatUtils.handleNumber(data.attitudes_count));
        } else {
            textSwitcher.setCurrentText(vh.getConvertView().getContext().getString(R.string.like));
        }
        if (data.like) {
            vh.getImageView(R.id.iv_like).setImageResource(R.drawable.praised);
        } else {
            vh.getImageView(R.id.iv_like).setImageResource(R.drawable.praise);

        }

        if (data.comments_count > 0) {
            vh.setTextView(R.id.tv_commend, StringFromatUtils.handleNumber(data.comments_count));
        } else if ((data.content_type == 40)){
            vh.setTextView(R.id.tv_commend, vh.getConvertView().getContext().getString(R.string.answer));
        } else{
            vh.setTextView(R.id.tv_commend, vh.getConvertView().getContext().getString(R.string.comment));
        }

        if (data.compact) {
            vh.get(R.id.bottom).setVisibility(View.GONE);
        } else {
            vh.get(R.id.bottom).setVisibility(View.VISIBLE);
        }
        if(data.content_type == 40){
            vh.get(R.id.layout_reward_status).setVisibility(View.VISIBLE);
            vh.getTextView(R.id.tv_reward_state).setVisibility(View.VISIBLE);
            showRewardState(vh, data);
        }else{
            vh.get(R.id.layout_reward_status).setVisibility(View.GONE);
            vh.getTextView(R.id.tv_reward_state).setVisibility(View.GONE);
        }
    }

    private void showRewardState(ViewHolder vh, LikeBean data) {
        TextView stateTv = vh.getTextView(R.id.tv_reward_state);
        TextView amountTv = vh.getTextView(R.id.tv_reward_amount);
        TextView amountUnit = vh.getTextView(R.id.tv_reward_amount_unit);
        int state;
        int amountStyle;
        int unitStyle;
        int leftDrawable;
        if(data.reward_state == 0){
            state = R.string.reward_on_going;
            amountStyle = R.style.reward_amount_on_going;
            unitStyle = R.style.reward_unit_on_going;
            leftDrawable = R.drawable.ic_money_highlight;
            stateTv.setBackgroundResource(R.drawable.bg_reward_on_going);
        }else if(data.reward_state == 1){
            state = R.string.reward_close;
            amountStyle = R.style.reward_amount_finish;
            unitStyle = R.style.reward_unit_finish;
            leftDrawable = R.drawable.ic_money_normal;
            stateTv.setBackgroundResource(R.drawable.bg_reward_finish);
        }else{
            state =  R.string.reward_finish;
            amountStyle = R.style.reward_amount_finish;
            unitStyle = R.style.reward_unit_finish;
            leftDrawable = R.drawable.ic_money_normal;
            stateTv.setBackgroundResource(R.drawable.bg_reward_finish);
        }
        stateTv.setText(state);
        amountTv.setTextAppearance(mContext, amountStyle);
        amountUnit.setTextAppearance(mContext, unitStyle);
        vh.getImageView(R.id.iv_money).setImageResource(leftDrawable);
        amountTv.setText(data.reward_amount);
    }


    public void setClickListener(View view, LikeBean data) {
        ItemHandlerClickListenerImp<LikeBean> itemHandlerClickListener = null;
        if (null != view.getTag() && view.getTag() instanceof ItemHandlerClickListenerImp) {
            itemHandlerClickListener = (ItemHandlerClickListenerImp<LikeBean>) view.getTag();
        } else {
            switch (view.getId()) {
                case R.id.fl_star:
                    itemHandlerClickListener = new LikeClickListenerImp();

                    break;
                case R.id.fl_commend:
                    itemHandlerClickListener = new CommendClickListenerImp();
                    break;
                case R.id.iv_avatar:
                case R.id.name:
                    itemHandlerClickListener = new AvatarClickListenerImp();
                    break;
                case R.id.iv:
                    itemHandlerClickListener = new ImageViewClickListenerImp();
                    break;
                case R.id.main_ll:
                    itemHandlerClickListener = new ItemClickListenerImp();
                    break;
                default:
                    itemHandlerClickListener = new ItemHandlerClickListenerImp<LikeBean>();
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


        private LikeBean likeBean;

        @Override
        public View.OnClickListener setDate(LikeBean o) {
            this.likeBean = o;
            mSwitchLikeStateHandler = new SwitchLikeStateHandler(likeBean);
            return this;
        }


        @Override
        public void onClick(View v) {
            mView = v;
            ImageView imageView = (ImageView) v.findViewById(R.id.iv_like);
            mSwitchLikeStateHandler.setStatusChangeI(this);
            mSwitchLikeStateHandler.attachLikeImage(imageView);
            mSwitchLikeStateHandler.toggleLikeState();
        }

        @Override
        public void likePre() {
            likeBean.attitudes_count += 1;
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.tv_like);
            if (likeBean.attitudes_count > 0) {

                likeTV.setText(StringFromatUtils.handleNumber(likeBean.attitudes_count));
            } else {
                likeTV.setText(mView.getContext().getString(R.string.like));
            }
        }

        @Override
        public void unLikePre() {
            likeBean.attitudes_count -= 1;
            TextSwitcher likeTV = (TextSwitcher) mView.findViewById(R.id.tv_like);
            if (likeBean.attitudes_count > 0) {

                likeTV.setText(StringFromatUtils.handleNumber(likeBean.attitudes_count));
            } else {
                likeTV.setText(mView.getContext().getString(R.string.like));
            }

        }
    }

    class CommendClickListenerImp extends ItemHandlerClickListenerImp<LikeBean> {


        private LikeBean likeBean;

        @Override
        public View.OnClickListener setDate(LikeBean o) {
            this.likeBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            //判断content_type
            if(likeBean.content_type == 40){
                rewardCommendClick();
            }else{
                topicCommendClick();
            }
        }

        private void rewardCommendClick() {
            if (likeBean.comments_count == 0) {

                if (UIUtils.iStartLoginActivity(mContext)) {
                    return;
                }
                UIUtils.startAnimationActivity((Activity) mContext, PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_COMMENT_REWARD, likeBean.id + "", likeBean.user.getUsername()));

            } else {
                //need fix
                TopicsDetailActivity.startActivity(mContext, likeBean.toTopicsBean(), true);
            }
        }

        private void topicCommendClick(){
            if (likeBean.comments_count == 0) {

                if (UIUtils.iStartLoginActivity(mContext)) {
                    return;
                }
                UIUtils.startAnimationActivity((Activity) mContext, PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_COMMENT_TOPIC, likeBean.id + "", likeBean.user.getUsername()));

            } else {
                TopicsDetailActivity.startActivity(mContext, likeBean.toTopicsBean(), true);
            }
        }
    }

    class AvatarClickListenerImp extends ItemHandlerClickListenerImp<LikeBean> {


        private LikeBean likeBean;


        @Override
        public View.OnClickListener setDate(LikeBean o) {
            this.likeBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            if (likeBean.user != null) {
                UIUtils.startAnimationActivity((Activity) mContext,
                        UserHomePageActivity.getIntent(mContext, likeBean.user.getUsername(), likeBean.user.getId() + ""));
            }


        }
    }

    class ImageViewClickListenerImp extends ItemHandlerClickListenerImp<LikeBean> {

        private LikeBean likeBean;

        @Override
        public View.OnClickListener setDate(LikeBean o) {
            this.likeBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {

            ArrayList<PhotoBean> arrayList = new ArrayList<>();
            PhotoBean photoBean = new PhotoBean();
            photoBean.title = likeBean.id + "";
            photoBean.loadingURl = likeBean.medias.get(0).getImage_sm();
            photoBean.imgUrl = likeBean.medias.get(0).getImage_md();
            arrayList.add(photoBean);
            PhotoViewActivity.startPhotoViewActivity(mContext, arrayList, v, 0);
        }
    }

    class ItemClickListenerImp extends ItemHandlerClickListenerImp<LikeBean> {


        private LikeBean likeBean;


        @Override
        public View.OnClickListener setDate(LikeBean o) {
            this.likeBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            TopicsDetailActivity.startActivity(mContext, likeBean.toTopicsBean());
        }


    }


}
