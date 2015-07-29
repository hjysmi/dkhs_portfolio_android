package com.dkhs.portfolio.bean.itemhandler;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.dkhs.adpter.handler.ItemHandlerClickListener;
import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

import org.parceler.Parcel;
import org.parceler.apache.commons.lang.StringUtils;

import java.util.ArrayList;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class TopicsHandler implements ItemHandler<TopicsBean> {



//    @ViewInject(R.id.iv_avatar)
//    ImageView mIvavatar;
//    @ViewInject(R.id.name)
//    TextView mName;
//    @ViewInject(R.id.tv_time)
//    TextView mTvtime;
//    @ViewInject(R.id.titleTV)
//    TextView mTitleTV;
//    @ViewInject(R.id.content)
//    TextView mContent;
//    @ViewInject(R.id.iv)
//    ImageView mIv;
//    @ViewInject(R.id.tv_commend)
//    TextView mTvcommend;
//    @ViewInject(R.id.fl_commend)
//    FrameLayout mFlcommend;
//    @ViewInject(R.id.tv_star)
//    TextView mTvstar;
//    @ViewInject(R.id.fl_layout)
//    FrameLayout mFllayout;

    private Context mContext;
    public TopicsHandler(Context context) {
        mContext=context;
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
        ImageLoaderUtils.setHeanderImage(data.user.getAvatar_md(),vh.getImageView(R.id.iv_avatar));
        vh.setTextView(R.id.content,data.text);

        if(data.medias != null && data.medias.size() > 0) {
            vh.get(R.id.iv).setVisibility(View.VISIBLE);
            ImageLoaderUtils.setImage(data.medias.get(0).image_sm,vh.getImageView(R.id.iv));

        }else{
            vh.get(R.id.iv).setVisibility(View.GONE);

        }
        if(data.favorites_count>0){
            vh.setTextView(R.id.tv_star, StringFromatUtils.handleNumber(data.favorites_count));
        }else{
            vh.setTextView(R.id.tv_star,vh.getConvertView().getContext().getString(R.string.star));
        }

        if(data.comments_count>0){
            vh.setTextView(R.id.tv_commend, StringFromatUtils.handleNumber(data.comments_count));
        }else{
            vh.setTextView(R.id.tv_commend, vh.getConvertView().getContext().getString(R.string.commend));
        }

    }

    public void setClickListener(View  view, TopicsBean data){
        ItemHandlerClickListenerImp<TopicsBean> itemHandlerClickListener=null;
        if(null !=  view.getTag() && view.getTag() instanceof  ItemHandlerClickListenerImp){
            itemHandlerClickListener= (ItemHandlerClickListenerImp<TopicsBean>) view.getTag();
        }else{
           switch (view.getId()){
               case  R.id.fl_star:
                   itemHandlerClickListener=new StarClickListenerImp();
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



    class  StarClickListenerImp extends ItemHandlerClickListenerImp<TopicsBean> {


        private TopicsBean topicsBean;


        @Override
        public View.OnClickListener setDate(TopicsBean o) {
            this.topicsBean = o;
            return this;
        }

        @Override
        public void onClick(View v) {
            ImageView imageView= (ImageView) v.findViewById(R.id.iv_star);
            if(topicsBean.star){
                imageView.setImageResource(R.drawable.ic_stared);
            }else{
                imageView.setImageResource(R.drawable.ic_star);
            }
            topicsBean.star=!topicsBean.star;
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

            ArrayList<String> arrayList=new ArrayList<>();
            arrayList.add(topicsBean.medias.get(0).image_lg);
            PhotoViewActivity.startPhotoViewActivity(mContext,arrayList,v,0);
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
