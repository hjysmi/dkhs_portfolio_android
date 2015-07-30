package com.dkhs.portfolio.bean.itemhandler;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.utils.SwitchLikeStateHandler;
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

public class TopicsHandler implements ItemHandler<TopicsBean> {

    private Context mContext;
    private  boolean mCompact;


    public TopicsHandler(Context context) {
        mContext=context;
    }


    public TopicsHandler(Context context, boolean compact) {
        mContext = context;
        this.mCompact = compact;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_topics;
    }

    @Override
    public void onBindView(ViewHolder vh, final TopicsBean data, int position) {
        setClickListener( vh.get(R.id.fl_commend),data);
        setClickListener( vh.get(R.id.iv_avatar),data);
        setClickListener( vh.get(R.id.iv),data);
        setClickListener( vh.get(R.id.main_ll),data);
        setClickListener( vh.get(R.id.fl_star),data);

        vh.setTextView(R.id.tv_time, TimeUtils.getBriefTimeString(data.created_at));
        if(TextUtils.isEmpty(data.title)){
            vh.get(R.id.titleTV).setVisibility(View.GONE);
        }else {
            vh.get(R.id.titleTV).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.titleTV, data.title);
        }
        if(data.user != null  && !TextUtils.isEmpty(data.user.getAvatar_md())) {
            ImageLoaderUtils.setHeanderImage(data.user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
        }
        vh.setTextView(R.id.content,data.text);
        vh.setTextView(R.id.name,data.user.getUsername());

        if(data.medias != null && data.medias.size() > 0) {
            vh.get(R.id.iv).setVisibility(View.VISIBLE);
            ImageLoaderUtils.setImagDefault(data.medias.get(0).image_sm, vh.getImageView(R.id.iv));

        }else{
            vh.get(R.id.iv).setVisibility(View.GONE);

        }
        if(data.favorites_count>0){
            vh.setTextView(R.id.tv_like, StringFromatUtils.handleNumber(data.favorites_count));
        }else{
            vh.setTextView(R.id.tv_like,vh.getConvertView().getContext().getString(R.string.like));
        }
        if(data.like){
            vh.getImageView(R.id.iv_like).setImageResource(R.drawable.ic_like);
        }else{
            vh.getImageView(R.id.iv_like).setImageResource(R.drawable.ic_unlike);

        }

        if(data.comments_count>0){
            vh.setTextView(R.id.tv_commend, StringFromatUtils.handleNumber(data.comments_count));
        }else{
            vh.setTextView(R.id.tv_commend, vh.getConvertView().getContext().getString(R.string.comment));
        }

        if(mCompact){
            vh.get(R.id.bottom).setVisibility(View.GONE);
        }else{
            vh.get(R.id.bottom).setVisibility(View.VISIBLE);
        }

    }

    public void setClickListener(View  view, TopicsBean data){
        ItemHandlerClickListenerImp<TopicsBean> itemHandlerClickListener=null;
        if(null !=  view.getTag() && view.getTag() instanceof  ItemHandlerClickListenerImp){
            itemHandlerClickListener= (ItemHandlerClickListenerImp<TopicsBean>) view.getTag();
        }else{
            switch (view.getId()){
                case  R.id.fl_star:
                    itemHandlerClickListener=new LikeClickListenerImp();

                    break;
                case  R.id.fl_commend:
                    itemHandlerClickListener=new CommendClickListenerImp();
                    break;
                case  R.id.iv_avatar:
                    itemHandlerClickListener=new AvatarClickListenerImp();
                    break;
                case  R.id.iv:
                    itemHandlerClickListener=new ImageViewClickListenerImp();
                    break;
                case  R.id.main_ll:
                    itemHandlerClickListener=new ItemClickListenerImp();
                    break;
                default:
                    itemHandlerClickListener=new ItemHandlerClickListenerImp<TopicsBean>();
                    break;
            }
            view.setOnClickListener(itemHandlerClickListener);
            view.setTag(itemHandlerClickListener);
        }
        itemHandlerClickListener.setDate(data);
    }



    class LikeClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> implements SwitchLikeStateHandler.StatusChangeI {



        private SwitchLikeStateHandler mSwitchLikeStateHandler;
        private View mView;


        private TopicsBean topicsBean;
        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            mSwitchLikeStateHandler =new SwitchLikeStateHandler(topicsBean);
            return this;
        }


        @Override
        public void onClick(View v) {
            mView=v;
            ImageView imageView= (ImageView) v.findViewById(R.id.iv_like);
            mSwitchLikeStateHandler.setStatusChangeI(this);
            mSwitchLikeStateHandler.attachLikeImage(imageView);
            mSwitchLikeStateHandler.toggleLikeState();
        }

        @Override
        public void likePre() {
            topicsBean.favorites_count+=1;
            TextView likeTV= (TextView) mView.findViewById(R.id.tv_like);
            if(topicsBean.favorites_count>0){

                likeTV.setText(StringFromatUtils.handleNumber(topicsBean.favorites_count));
            }else{
                likeTV.setText(mView.getContext().getString(R.string.like));
            }
        }

        @Override
        public void unLikePre() {
            topicsBean.favorites_count-=1;
            TextView likeTV= (TextView) mView.findViewById(R.id.tv_like);
            if(topicsBean.favorites_count>0){

                likeTV.setText(StringFromatUtils.handleNumber(topicsBean.favorites_count));
            }else{
                likeTV.setText(mView.getContext().getString(R.string.like));
            }

        }
    }
    class  CommendClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {


        private TopicsBean topicsBean;


        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {

            UIUtils.startAnimationActivity((Activity) mContext, PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_RETWEET, topicsBean.id + "", topicsBean.user.getUsername()));

        }
    }
    class  AvatarClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {


        private TopicsBean topicsBean;


        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {

        }
    }
    class  ImageViewClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {

        private TopicsBean topicsBean;
        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {

            ArrayList<PhotoBean> arrayList=new ArrayList<>();


            PhotoBean photoBean=new PhotoBean();
            photoBean.title=topicsBean.id+"";
            photoBean.loadingURl=topicsBean.medias.get(0).image_sm;
            photoBean.imgUrl=topicsBean.medias.get(0).image_lg;
            arrayList.add(photoBean);
            PhotoViewActivity.startPhotoViewActivity(mContext,arrayList,v, 0);
        }
    }
    class  ItemClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {


        private TopicsBean topicsBean;


        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            TopicsDetailActivity.startActivity(mContext, topicsBean);
        }


    }





}
