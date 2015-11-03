package com.dkhs.portfolio.ui.widget;

import android.content.Intent;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.LikeBean;
import com.dkhs.portfolio.bean.StatusBean;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.LoginActivity;

/**
 * @author zwm
 * @version 2.0
 * @ClassName SwitchLikeStateHandler
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

    public void attachLikeImage(ImageView likeIm) {
        mLikeIm = likeIm;
    }

    public interface StatusChangeI {

        void likePre();

        void unLikePre();
    }

    private StatusChangeI mStatusChangeI;

    public void setStatusChangeI(StatusChangeI statusChangeI) {
        mStatusChangeI = statusChangeI;
    }

    public void toggleLikeState() {
        mLikeBean.setLike(!mLikeBean.isLike());

        if (!PortfolioApplication.hasUserLogin()) {

            Intent intent = LoginActivity.loginActivityByAnnoy(PortfolioApplication.getInstance());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PortfolioApplication.getInstance().startActivity(
                    intent);
            return;
        }
        if (!mLikeBean.isLike()) {

            StatusEngineImpl.unstarTopic(mLikeBean.getId() + "", new SimpleParseHttpListener() {
                @Override
                public Class getClassType() {
                    return StatusBean.class;
                }

                @Override
                protected void afterParseData(Object object) {
                    //do something
                }

                @Override
                public void onSuccess(String jsonObject) {
                    super.onSuccess(jsonObject);
                    //取消点赞
                    if (mLikeIm != null) {
                        unLikeImage();
                    }
                    if (mStatusChangeI != null) {
                        mStatusChangeI.unLikePre();
                    }
                }
            });
        } else {

            StatusEngineImpl.starTopic(mLikeBean.getId() + "", new SimpleParseHttpListener() {
                @Override
                public Class getClassType() {
                    return StatusBean.class;
                }

                @Override
                protected void afterParseData(Object object) {
                    //do something
                }

                @Override
                public void onSuccess(String jsonObject) {
                    super.onSuccess(jsonObject);
                    //点赞
                    if (mLikeIm != null) {
                        likeImage();
                    }
                    if (mStatusChangeI != null) {
                        mStatusChangeI.likePre();
                    }
                }
            });

        }

    }

    private void unLikeImage() {
        mLikeIm.setImageResource(R.drawable.praise);
    }

    private void likeImage() {
        mLikeIm.setImageResource(R.drawable.praised);
    }


}
