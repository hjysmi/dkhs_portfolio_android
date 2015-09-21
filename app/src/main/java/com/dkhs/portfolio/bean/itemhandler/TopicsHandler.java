package com.dkhs.portfolio.bean.itemhandler;


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
        if (TextUtils.isEmpty(data.title)) {
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


        if(TextUtils.isEmpty(data.text)){
            vh.get(R.id.content).setVisibility(View.GONE);
        }else {
            vh.get(R.id.content).setVisibility(View.VISIBLE);
        }
        vh.setTextView(R.id.content, data.text);
        vh.setTextView(R.id.name, data.user.getUsername());


        TopicsImageViewHandler topicsImageViewHandler= (TopicsImageViewHandler) vh.get(R.id.titleTV).getTag();



        if(topicsImageViewHandler == null){
            topicsImageViewHandler=  new TopicsImageViewHandler();
            vh.get(R.id.titleTV).setTag(topicsImageViewHandler);
        }

        topicsImageViewHandler .handleMedias(vh, data,false);


        TextSwitcher textSwitcher = vh.get(R.id.tv_like);
        if (data.attitudes_count > 0) {

            textSwitcher.setCurrentText(StringFromatUtils.handleNumber(data.attitudes_count));
        } else {
            textSwitcher.setCurrentText(vh.getConvertView().getContext().getString(R.string.like));
        }
        if (data.like) {
            vh.getImageView(R.id.iv_like).setImageResource(R.drawable.ic_like);
        } else {
            vh.getImageView(R.id.iv_like).setImageResource(R.drawable.ic_unlike);

        }

        if (data.comments_count > 0) {
            vh.setTextView(R.id.tv_commend, StringFromatUtils.handleNumber(data.comments_count));
        } else {
            vh.setTextView(R.id.tv_commend, vh.getConvertView().getContext().getString(R.string.comment));
        }

        if (data.compact) {
            vh.get(R.id.bottom).setVisibility(View.GONE);
        } else {
            vh.get(R.id.bottom).setVisibility(View.VISIBLE);
        }

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

            if (likeBean.comments_count == 0) {

                if (UIUtils.iStartLoginActivity(mContext)) {
                    return;
                }
                UIUtils.startAnimationActivity((Activity) mContext, PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_COMMENT, likeBean.id + "", likeBean.user.getUsername()));

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
