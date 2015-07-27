package com.dkhs.portfolio.bean.itemhandler;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */

public class TopicsDetailHandler implements ItemHandler<TopicsBean> {



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


    public TopicsDetailHandler(Context context) {

        mContext=context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_topics_detail;
    }

    @Override
    public void onBindView(ViewHolder vh, final TopicsBean data, int position) {
        vh.setTextView(R.id.tv_time, TimeUtils.getBriefTimeString(data.created_at));
        if(TextUtils.isEmpty(data.title)){
            vh.get(R.id.titleTV).setVisibility(View.GONE);
        }else {
            vh.get(R.id.titleTV).setVisibility(View.VISIBLE);
            vh.setTextView(R.id.titleTV, data.title);
        }
        ImageLoaderUtils.setHeanderImage(data.user.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
        vh.setTextView(R.id.content,data.text);
        vh.get(R.id.iv).setVisibility(View.GONE);

        if(false){
            vh.setTextView(R.id.tv_empty,"此贴已删除");
            vh.get(R.id.main_ll).setVisibility(View.GONE);
            vh.get(R.id.tv_empty).setVisibility(View.VISIBLE);
        }else{
            vh.get(R.id.main_ll).setVisibility(View.VISIBLE);
            vh.get(R.id.tv_empty).setVisibility(View.GONE);

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
