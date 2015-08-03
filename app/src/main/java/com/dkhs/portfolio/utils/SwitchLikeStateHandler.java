package com.dkhs.portfolio.utils;

import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.LikeBean;
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


    private LikeBean mLikeBean;

    private ImageView mLikeIm;

    public SwitchLikeStateHandler(LikeBean likeBean) {
        mLikeBean = likeBean;
    }

    public void setLikeBean(LikeBean likeBean) {
        mLikeBean = likeBean;
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
        mLikeBean.setLike(!mLikeBean.isLike());
        if (!mLikeBean.isLike()) {
            //取消点赞
            if(mLikeIm != null){
                unLikeImage();
            }
            if(mStatusChangeI != null ){
                mStatusChangeI.unLikePre();
            }
            StatusEngineImpl.unstarTopic(mLikeBean.getId() + "", new SimpleParseHttpListener() {
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
            StatusEngineImpl.starTopic(mLikeBean.getId() + "", new SimpleParseHttpListener() {
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
