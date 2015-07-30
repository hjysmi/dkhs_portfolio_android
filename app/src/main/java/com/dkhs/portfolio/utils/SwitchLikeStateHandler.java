package com.dkhs.portfolio.utils;

import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.StatusBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;

/**
 * @author zwm
 * @version 2.0
 * @ClassName SwitchStarStateListener
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/30.
 */
public class SwitchLikeStateHandler {


    private TopicsBean mTopicsBean ;

    private ImageView mLikeIm;

    public SwitchLikeStateHandler(TopicsBean topicsBean) {
        mTopicsBean = topicsBean;
    }

    public void setTopicsBean(TopicsBean topicsBean) {
        mTopicsBean = topicsBean;
    }

    public void attachLikeImage(ImageView likeIm){
        mLikeIm =likeIm;
    }

    public interface  StatusChangeI{

       void likePre();
       void unLikePre();
    }

    private StatusChangeI mStatusChangeI;

    public void setStatusChangeI(StatusChangeI statusChangeI) {
        mStatusChangeI = statusChangeI;
    }

    public void toggleLikeState() {
        mTopicsBean.like =!mTopicsBean.like;
        if (!mTopicsBean.like) {
            //取消点赞
            if(mLikeIm != null){
                unLikeImage();
            }
            if(mStatusChangeI != null ){
                mStatusChangeI.unLikePre();
            }
            StatusEngineImpl.unstarTopic(mTopicsBean.id + "", new SimpleParseHttpListener() {
                @Override
                public Class getClassType() {
                    return StatusBean.class;
                }

                @Override
                protected void afterParseData(Object object) {
                    //do something
                }
            });
        } else {
            //点赞
            if(mLikeIm != null){
                likeImage();
            }
            if(mStatusChangeI != null ){
                mStatusChangeI.likePre();
            }
            StatusEngineImpl.starTopic(mTopicsBean.id + "", new SimpleParseHttpListener() {
                @Override
                public Class getClassType() {
                    return StatusBean.class;
                }

                @Override
                protected void afterParseData(Object object) {
                    //do something
                }
            });

        }

    }

    private void unLikeImage() {
        mLikeIm.setImageResource(R.drawable.ic_unlike);
    }

    private void likeImage() {
        mLikeIm.setImageResource(R.drawable.ic_like);
    }


}
