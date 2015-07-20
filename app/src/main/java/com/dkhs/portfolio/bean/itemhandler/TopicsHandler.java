package com.dkhs.portfolio.bean.itemhandler;


import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.adpter.listener.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopcsItem
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/16.
 */
public class TopicsHandler implements ItemHandler{


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

    @Override
    public int getLayoutResId() {
        return R.layout.layout_topics;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {


        StarClickListenerImp starClickListenerImp=null;
        if(null !=  vh.get(R.id.fl_star).getTag()){
            starClickListenerImp= (StarClickListenerImp) vh.get(R.id.fl_star).getTag();
        }else{
            starClickListenerImp=new StarClickListenerImp();
        }
        starClickListenerImp.setTopicsBean((TopicsBean) data);



    }

    @Override
    public Class<?> getDataClass() {
        return TopicsBean.class;
    }


    class  StarClickListenerImp implements View.OnClickListener{


        private TopicsBean topicsBean;

        public void setTopicsBean(TopicsBean topicsBean) {
            this.topicsBean = topicsBean;
        }

        @Override
        public void onClick(View v) {

        }
    }

}
