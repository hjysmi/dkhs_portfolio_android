package com.dkhs.portfolio.bean.itemhandler;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TopicsDetailRefreshEvent;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.mingle.bean.PhotoBean;

import java.util.ArrayList;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class TopicsDetailHandler implements ItemHandler<TopicsBean>, AdapterView.OnItemSelectedListener {



    private Context mContext;

    public TopicsDetailHandler(Context context) {
        mContext=context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_topics_detail;
    }

    @Override
    public void onBindView(ViewHolder vh, final TopicsBean data, int position) {
        setClickListener( vh.get(R.id.iv_avatar),data);
        setClickListener( vh.get(R.id.iv),data);

        vh.setTextView(R.id.tv_time, TimeUtils.getBriefTimeString(data.created_at));
        if(TextUtils.isEmpty(data.title)){
            vh.get(R.id.titleTV).setVisibility(View.GONE);
        }else {
            vh.get(R.id.titleTV).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.titleTV, data.title);
        }
        vh.setTextView(R.id.name,data.user.getUsername());

        if(data.user != null  && !TextUtils.isEmpty(data.user.getAvatar_md())) {
            ImageLoaderUtils.setHeanderImage(data.user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
        }
        vh.setTextView(R.id.content,data.text);
        vh.get(R.id.iv).setVisibility(View.GONE);

        if(data.medias != null && data.medias.size() > 0) {
            vh.get(R.id.iv).setVisibility(View.VISIBLE);
            ImageLoaderUtils.setImagDefault(data.medias.get(0).image_sm, vh.getImageView(R.id.iv));

        }else{
            vh.get(R.id.iv).setVisibility(View.GONE);
        }

        vh.setTextView(R.id.tv_like,mContext.getString(R.string.like)+" "+data.favorites_count);
        vh.setTextView(R.id.comment,mContext.getString(R.string.comment)+" "+data.comments_count);


        if(false){
            vh.setTextView(R.id.tv_empty,"此贴已删除");
            vh.get(R.id.main_ll).setVisibility(View.GONE);
            vh.get(R.id.emptyRl).setVisibility(View.VISIBLE);
        }else{
            vh.get(R.id.main_ll).setVisibility(View.VISIBLE);
            vh.get(R.id.emptyRl).setVisibility(View.GONE);

        }

        Spinner spinner=vh.get(R.id.spinner);
        spinner.setOnItemSelectedListener(this);


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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TopicsDetailRefreshEvent topicsDetailRefreshEvent=new TopicsDetailRefreshEvent();
        switch (position){
            case 0:
                topicsDetailRefreshEvent.sortType= TopicsCommendEngineImpl.SortType.latest;
                break;
            case 1:
                topicsDetailRefreshEvent.sortType= TopicsCommendEngineImpl.SortType.best;
                break;
            case 2:
                topicsDetailRefreshEvent.sortType= TopicsCommendEngineImpl.SortType.earliest;
                break;
        }
        BusProvider.getInstance().post(topicsDetailRefreshEvent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            ImageView imageView= (ImageView) v.findViewById(R.id.iv_like);
            if(topicsBean.like){
                imageView.setImageResource(R.drawable.ic_like);
            }else{
                imageView.setImageResource(R.drawable.ic_unlike);
            }
            topicsBean.like =!topicsBean.like;
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
            UIUtils.startAnimationActivity((Activity) mContext, (PostTopicActivity.getIntent(mContext, PostTopicActivity.TYPE_RETWEET, topicsBean.id + "", topicsBean.user.getUsername())));
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
